package sportal.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.ArticleCategory;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
@Component
public interface ICategoryRepository extends JpaRepository<ArticleCategory, Integer> {
    Optional<ArticleCategory> findByName(String catName);
}
