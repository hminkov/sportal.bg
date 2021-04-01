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
public class LikeAndDislikeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    void removeDislikeAndAddLikeOrViceVersa(int userId, int entityId, String removeQuery, String addQuery) {
        Connection connection = getConnection();
        try(
                PreparedStatement removeStatement = connection.prepareStatement(removeQuery);
                PreparedStatement addStatement = connection.prepareStatement(addQuery);
        ) {
            connection.setAutoCommit(false);
            removeStatement.setInt(1, userId);
            removeStatement.setInt(2, entityId);
            addStatement.setInt(1, userId);
            addStatement.setInt(2, entityId);
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
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    
    boolean checkIfAlreadyLikedOrAlreadyDisliked(int userId, int entityId, String query) {
        Connection connection = getConnection();
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, userId);
            ps.setInt(2, entityId);
            ResultSet rs =  ps.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    int getLikesOrDislikes(int entityId, String selectLikesQuery){
        Connection connection = getConnection();
        try(PreparedStatement ps = connection.prepareStatement(selectLikesQuery)){
            ps.setInt(1, entityId);
            ResultSet rs = ps.executeQuery(selectLikesQuery);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @SneakyThrows
    private Connection getConnection(){
        return jdbcTemplate.getDataSource().getConnection();
    }

}
