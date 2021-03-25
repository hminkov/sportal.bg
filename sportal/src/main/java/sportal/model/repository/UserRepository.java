package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
