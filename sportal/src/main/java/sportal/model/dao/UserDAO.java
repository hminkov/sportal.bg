package sportal.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import sportal.model.pojo.User;
import java.sql.ResultSet;


@Component
@NoArgsConstructor
@Getter
@Setter
public class UserDAO {

    private final String DELETE_USER = "UPDATE users SET username = 'deleted', password = 'deleted', email = 'deleted' WHERE id = ?";

    private final String SELECT_ADMIN_BY_ID = "SELECT u.id, r.role_name FROM users AS u\n" +
            "JOIN users_have_roles AS uhr ON u.id = uhr.user_id\n" +
            "JOIN roles AS r ON uhr.role_id = r.id\n" +
            "WHERE u.id = ? AND role_name = \"admin\"";

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void deleteUser(int id) throws SQLException {
        String sql = "UPDATE users SET username = 'deleted-" + id +"'" + ", password = 'deleted-" + id +"'" + ", email = 'deleted-" + id +"'" + " WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }


    @SneakyThrows
    public boolean userIsAdmin(User user) {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(getSELECT_ADMIN_BY_ID())){
            ps.setInt(1, user.getId());
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
