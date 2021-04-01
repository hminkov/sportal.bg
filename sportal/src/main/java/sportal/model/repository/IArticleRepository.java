package sportal.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Article;
import sportal.model.pojo.User;

import java.util.List;

@Repository
@Component
public interface IArticleRepository extends JpaRepository<Article, Integer> {

    Page<Article> findArticlesByAuthor(User user, Pageable pageable);
}
