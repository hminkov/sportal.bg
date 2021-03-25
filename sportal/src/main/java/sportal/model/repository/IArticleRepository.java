package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Article;

@Repository
public interface IArticleRepository extends JpaRepository<Article, Integer> {
}
