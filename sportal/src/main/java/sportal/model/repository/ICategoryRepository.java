package sportal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sportal.model.pojo.ArticleCategory;

@Repository
@Component
public interface ICategoryRepository extends JpaRepository<ArticleCategory, Integer> {

//    default void saveIfNonExistent(ArticleCategory category){
//        if(!this.existsByName(category)){
//            save(category);
//        }
//    }
//
//    boolean existsByName(ArticleCategory category);
}
