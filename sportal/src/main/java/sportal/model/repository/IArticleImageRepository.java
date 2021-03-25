package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.ArticleImage;

@Repository
public interface IArticleImageRepository extends JpaRepository<ArticleImage, Integer> {
}
