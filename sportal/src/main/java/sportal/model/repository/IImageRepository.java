package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.ArticleImage;

@Repository
@Component
public interface IImageRepository extends JpaRepository<ArticleImage, Integer> {
}
