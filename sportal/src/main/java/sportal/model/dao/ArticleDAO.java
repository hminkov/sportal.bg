package sportal.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.UserDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            "SELECT id, heading, article_text, views " +
                    "FROM articles " +
                    "ORDER BY views DESC LIMIT 5;";
    private final String SEARCH_FOR_HEADING = "SELECT * FROM articles WHERE heading LIKE (?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LikeAndDislikeDAO likeAndDislikeDAO;
    @Autowired
    private IUserRepository iUserRepository;

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

    private Article packArticle(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getInt("id"));
        article.setHeading(rowSet.getString("heading"));
        article.setArticleText(rowSet.getString("article_text"));
        article.setViews(rowSet.getInt("views"));
        return article;
    }

    public List<Article> topFiveMostViewedArticles(){
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(TOP_FIVE_ARTICLES);
        List<Article> topFiveArticles = new ArrayList<>();
        while (rowSet.next()) {
            topFiveArticles.add(this.packArticle(rowSet));
        }
        return topFiveArticles;
    }

    public List<Article> getArticleByHeading(String articleHeading){
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM articles WHERE heading LIKE '%" + articleHeading + "%'");
        List<Article> articlesByHeading = new ArrayList<>();
        while (rowSet.next()) {
            Article article = new Article();
            article.setId(rowSet.getInt("id"));
            articlesByHeading.add(article);
        }
        return articlesByHeading;
    }


}
