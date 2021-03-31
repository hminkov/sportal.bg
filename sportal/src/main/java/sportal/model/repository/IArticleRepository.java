package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Article;

import java.awt.print.Pageable;
import java.util.List;

@Repository
@Component
public interface IArticleRepository extends JpaRepository<Article, Integer> {
    Article findByHeading(String heading);
//    List<Article> findAll(Pageable pageable);
}
