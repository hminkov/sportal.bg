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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String ADD_LIKE_QUERY = "INSERT INTO users_like_comments (user_id, comment_id) VALUES (?, ?)";
    private final String REMOVE_DISLIKE_QUERY = "DELETE FROM users_dislike_comments WHERE user_id = ? AND comment_id = ?";
    private final String ADD_DISLIKE_QUERY = "INSERT INTO users_dislike_comments (user_id, comment_id) VALUES (?, ?)";
    private final String REMOVE_LIKE_QUERY = "DELETE FROM users_like_comments WHERE user_id = ? AND comment_id = ?";
    private final String SELECT_LIKES_QUERY = "SELECT ulc.user_id, ulc.comment_id FROM users_like_comments AS ulc " +
            "WHERE ulc.user_id = ? AND ulc.comment_id = ?";
    private final String SELECT_DISLIKES_QUERY = "SELECT udc.user_id, udc.comment_id FROM users_dislike_comments AS udc " +
            "WHERE udc.user_id = ? AND udc.comment_id = ?";

    public void likeComment(int userId, int commentId) {
        if(checkIfAlreadyLikedOrAlreadyDisliked(userId, commentId, SELECT_DISLIKES_QUERY)){
            removeDislikeAndAddLikeOrViceVersa(userId, commentId, REMOVE_DISLIKE_QUERY, ADD_LIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_LIKE_QUERY, userId, commentId);
    }

    public void dislikeComment(int userId, int commentId){
        if(checkIfAlreadyLikedOrAlreadyDisliked(userId, commentId, SELECT_LIKES_QUERY)){
            removeDislikeAndAddLikeOrViceVersa(userId, commentId, REMOVE_LIKE_QUERY, ADD_DISLIKE_QUERY);
            return;
        }
        jdbcTemplate.update(ADD_DISLIKE_QUERY);
    }

    private void removeDislikeAndAddLikeOrViceVersa(int userId, int commentId, String removeQuery, String addQuery) {
        Connection connection = getConnection();
        try(
                PreparedStatement removeStatement = connection.prepareStatement(removeQuery);
                PreparedStatement addStatement = connection.prepareStatement(addQuery);
        ) {
            connection.setAutoCommit(false);
            removeStatement.setInt(1, userId);
            removeStatement.setInt(2, commentId);
            addStatement.setInt(1, userId);
            addStatement.setInt(2, commentId);
            removeStatement.executeUpdate();
            addStatement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private boolean checkIfAlreadyLikedOrAlreadyDisliked(int userId, int commentId, String query) {
        Connection connection = getConnection();
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, userId);
            ps.setInt(2, commentId);
            ResultSet rs =  ps.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @SneakyThrows
    private Connection getConnection(){
        return jdbcTemplate.getDataSource().getConnection();
    }
}
