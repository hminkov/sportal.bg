package sportal.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;

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
            "SELECT id, heading, article_text, views " +
                    "FROM articles " +
                    "ORDER BY views DESC LIMIT 5;";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LikeAndDislikeDAO likeAndDislikeDAO;

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
}
