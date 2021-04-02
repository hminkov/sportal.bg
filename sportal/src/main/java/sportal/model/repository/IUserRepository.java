package sportal.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.User;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
@Component
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
