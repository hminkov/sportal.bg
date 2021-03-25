package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
