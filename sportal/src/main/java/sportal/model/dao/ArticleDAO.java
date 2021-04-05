package sportal.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.ArticleResponseWithoutComDTO;
import sportal.model.pojo.Article;
import sportal.model.repository.ICategoryRepository;
import sportal.model.repository.IUserRepository;
import sportal.util.OptionalResultVerifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ArticleDAO {

    private final String ADD_LIKE_QUERY = "INSERT INTO users_like_articles (user_id, article_id) VALUES (?, ?)";
    private final String REMOVE_DISLIKE_QUERY = "DELETE FROM users_dislike_articles WHERE user_id = ? AND article_id = ?";
    private final String ADD_DISLIKE_QUERY = "INSERT INTO users_dislike_articles (user_id, article_id) VALUES (?, ?)";
    private final String REMOVE_LIKE_QUERY = "DELETE FROM users_like_articles WHERE user_id = ? AND article_id = ?";
    private final String SELECT_LIKES_QUERY = "SELECT ula.user_id, ula.article_id FROM users_like_articles AS ula " +
            "WHERE ula.user_id = ? AND ula.article_id = ?";
    private final String SELECT_DISLIKES_QUERY = "SELECT uda.user_id, uda.article_id FROM users_dislike_articles AS uda " +
            "WHERE uda.user_id = ? AND uda.article_id = ?";
    private final String TOP_FIVE_ARTICLES =
            "SELECT id " +
                    "FROM articles " +
                    "ORDER BY views DESC LIMIT 5;";
    private final String ARTICLES_BY_CATEGORY =
            "SELECT a.id, a.heading, a.text, a.post_date, a.views, a.author_id AS author, a.category_id AS category " +
                    "FROM articles AS a " +
                    "JOIN article_categories AS ac " +
                    "ON a.category_id = ac.id " +
                    "WHERE ac.id = ? " +
                    "ORDER BY a.post_date DESC LIMIT ? OFFSET ?;";
    private final String SEARCH_FOR_HEADING =
            "SELECT id, heading " +
                    "FROM articles " +
                    "WHERE heading LIKE ? LIMIT ? OFFSET ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LikeAndDislikeDAO likeAndDislikeDAO;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private OptionalResultVerifier orv;

    public void likeArticle(int userId, int articleId) {
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_LIKES_QUERY)){
            throw new BadRequestException("User already likes this article");
        }
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_DISLIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, articleId, REMOVE_DISLIKE_QUERY, ADD_LIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_LIKE_QUERY, userId, articleId);
    }

    public void dislikeArticle(int userId, int articleId){
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_DISLIKES_QUERY)){
            throw new BadRequestException("User already dislikes this article");
        }
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_LIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, articleId, REMOVE_LIKE_QUERY, ADD_DISLIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_DISLIKE_QUERY, userId, articleId);
    }

    public void unlikeArticle(int userId, int articleId) {
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_LIKES_QUERY)){
            jdbcTemplate.update(REMOVE_LIKE_QUERY, userId, articleId);
        }
        else{
            throw new BadRequestException("Trying to remove an non-extant entry");
        }
    }

    public void undislikeArticle(int userId, int articleId){
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_DISLIKES_QUERY)){
            jdbcTemplate.update(REMOVE_DISLIKE_QUERY, userId, articleId);
        }
        else{
            throw new BadRequestException("Trying to remove an non-extant entry");
        }
    }

    public List<Article> topFiveMostViewedArticles(){
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(TOP_FIVE_ARTICLES);
        List<Article> topFiveArticles = new ArrayList<>();
        while (rowSet.next()) {
            Article article = new Article();
            article.setId(rowSet.getInt("id"));
            topFiveArticles.add(article);
        }
        return topFiveArticles;
    }

    public List<ArticleResponseWithoutComDTO> articlesByCategoryId(long categoryID, int page, int resultsPerPage){
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ARTICLES_BY_CATEGORY, categoryID, resultsPerPage, (page-1)*resultsPerPage);
        List<ArticleResponseWithoutComDTO> listOfArticles = new ArrayList<>();
        while (rowSet.next()) {
            Article article = new Article();
            article.setId(rowSet.getInt("id"));
            article.setHeading(rowSet.getString("heading"));
            article.setText(rowSet.getString("text"));
//            article.setPostDate(rowSet.getTimestamp("post_date").toLocalDateTime());
            article.setViews(rowSet.getInt("views"));
            article.setCategory(orv.verifyOptionalResult(iCategoryRepository.findById(rowSet.getInt("category"))));
            article.setAuthor(orv.verifyOptionalResult(iUserRepository.findById(rowSet.getInt("author"))));
            listOfArticles.add(new ArticleResponseWithoutComDTO(article));
        }
        return listOfArticles;
    }

    @SneakyThrows
    public List<Article> getArticleByHeading(String articleHeading, Pageable pageable){
        List<Article> articlesByHeading = new ArrayList<>();
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        int offset = pageable.getPageNumber()*pageable.getPageSize();
        try(PreparedStatement ps = connection.prepareStatement(SEARCH_FOR_HEADING)){
            ps.setString(1, "%" + articleHeading +"%");
            ps.setInt(2, pageable.getPageSize());
            ps.setInt(3, offset);
            ResultSet rowSet = ps.executeQuery();
            while (rowSet.next()) {
                Article article = new Article();
                article.setId(rowSet.getInt("id"));
                article.setHeading(rowSet.getString("heading"));
                articlesByHeading.add(article);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return articlesByHeading;
    }
}
