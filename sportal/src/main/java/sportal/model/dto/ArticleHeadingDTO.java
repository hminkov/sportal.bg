package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ArticleHeadingDTO {

    private String heading;

    public ArticleHeadingDTO(Article article){
        this.heading = article.getHeading();
    }
}
