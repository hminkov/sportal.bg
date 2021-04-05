package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.User;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Query("SELECT DISTINCT(user) FROM User user JOIN Comment comment ON user.id = comment.user.id")
    List<User> findAllWithComments();
}
