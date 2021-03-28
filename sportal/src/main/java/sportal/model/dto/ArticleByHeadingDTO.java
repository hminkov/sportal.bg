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
public class ArticleByHeadingDTO {
    private String heading;

    public ArticleByHeadingDTO(Article article){
        this.heading = article.getHeading();
    }
}
