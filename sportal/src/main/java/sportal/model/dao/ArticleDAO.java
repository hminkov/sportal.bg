package sportal.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sportal.exceptions.NotFoundException;
import sportal.model.pojo.Article;
import sportal.model.pojo.User;

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
//    private final String FIND_ALL_ARTICLES_BY_AUTHOR = "SELECT a.id, u.username, c.category_name, a.views, a.heading, a.article_text, a.post_date FROM articles AS a LEFT JOIN users AS u ON a.author_id = u.id LEFT JOIN article_categories AS c ON a.category_id = c.category_name WHERE u.username LIKE \"boris\";";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LikeAndDislikeDAO likeAndDislikeDAO;

//    public List<Article> findArticleByAuthor(String authorName) throws SQLException {
//        Connection connection = jdbcTemplate.getDataSource().getConnection(); //getDataSource looks for registered datasources in application.properties
//        List<Article> articlesByAuthor = new ArrayList<>();
//        String SQLquery = FIND_ALL_ARTICLES_BY_AUTHOR;
//        try(PreparedStatement ps = connection.prepareStatement(SQLquery)) {
//            ps.setInt(1, id);
//            ResultSet result = ps.executeQuery();
//            if (result.next()) {
//                Article article = new User(result.getInt("a.id"),
//                        result.getString("u.username"),
//                        result.getString("c.category_name"),
//                        result.getString("a.views"),
//                        result.getString("a.views"));
//                articlesByAuthor.add(article);
//            }
//            if(articlesByAuthor.size() <= 0) {
//                throw new NotFoundException("There is no article by this user");
//            }
//        }catch (NotFoundException e){
//            throw new NotFoundException("Something went wrong - " + e.getMessage());
//        }
//        return articlesByAuthor;
//    }

    public void likeArticle(int userId, int articleId) {
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_DISLIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, articleId, REMOVE_DISLIKE_QUERY, ADD_LIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_LIKE_QUERY, userId, articleId);
    }

    public void dislikeArticle(int userId, int articleId){
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, articleId, SELECT_LIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, articleId, REMOVE_LIKE_QUERY, ADD_DISLIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_DISLIKE_QUERY);
    }
}
