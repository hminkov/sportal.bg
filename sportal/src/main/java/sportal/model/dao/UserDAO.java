package sportal.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserDAO {

    private final String DELETE_USER = "UPDATE users SET username = 'deleted', password = 'deleted', email = 'deleted' WHERE id = ?";

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void deleteUser(int id) throws SQLException {
        String sql = DELETE_USER;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
