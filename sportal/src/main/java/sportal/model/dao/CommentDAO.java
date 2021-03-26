package sportal.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
@Getter
@Setter
public class CommentDAO {

    private final String ADD_LIKE_QUERY = "INSERT INTO users_like_comments (user_id, comment_id) VALUES (?, ?)";
    private final String REMOVE_DISLIKE_QUERY = "DELETE FROM users_dislike_comments WHERE user_id = ? AND comment_id = ?";
    private final String ADD_DISLIKE_QUERY = "INSERT INTO users_dislike_comments (user_id, comment_id) VALUES (?, ?)";
    private final String REMOVE_LIKE_QUERY = "DELETE FROM users_like_comments WHERE user_id = ? AND comment_id = ?";
    private final String SELECT_LIKES_QUERY = "SELECT ulc.user_id, ulc.comment_id FROM users_like_comments AS ulc " +
            "WHERE ulc.user_id = ? AND ulc.comment_id = ?";
    private final String SELECT_DISLIKES_QUERY = "SELECT udc.user_id, udc.comment_id FROM users_dislike_comments AS udc " +
            "WHERE udc.user_id = ? AND udc.comment_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LikeAndDislikeDAO likeAndDislikeDAO;


    public void likeComment(int userId, int commentId) {
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, commentId, SELECT_DISLIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, commentId, REMOVE_DISLIKE_QUERY, ADD_LIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_LIKE_QUERY, userId, commentId);
    }

    public void dislikeComment(int userId, int commentId){
        if(likeAndDislikeDAO.checkIfAlreadyLikedOrAlreadyDisliked(userId, commentId, SELECT_LIKES_QUERY)){
            likeAndDislikeDAO.removeDislikeAndAddLikeOrViceVersa(userId, commentId, REMOVE_LIKE_QUERY, ADD_DISLIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_DISLIKE_QUERY);
    }


}
