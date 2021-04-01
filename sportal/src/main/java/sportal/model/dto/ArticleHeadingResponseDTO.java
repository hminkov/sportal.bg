package sportal.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ArticleHeadingResponseDTO {

    private int id;
    private String heading;

    public ArticleHeadingResponseDTO(Article article){
        this.id = article.getId();
        this.heading = article.getHeading();
    }
}
