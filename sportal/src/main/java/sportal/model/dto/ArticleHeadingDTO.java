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

    private int id;
    private String heading;
    private String text;

    public ArticleHeadingDTO(Article article){
        this.id = article.getId();
        this.heading = article.getHeading();
        this.text = article.getArticleText();
    }
}
