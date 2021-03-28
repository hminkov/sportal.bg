package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Article;

@Repository
@Component
public interface IArticleRepository extends JpaRepository<Article, Integer> {
}
