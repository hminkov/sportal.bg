package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.User;

@NoArgsConstructor
@Getter
@Setter
@Component
public class CreateArticleResponseDTO {

    private int id;
    private User author;
    private String heading;
    private String text;
    private ArticleCategory category;

    public CreateArticleResponseDTO(Article article){
        id = article.getId();
        author = article.getAuthor();
        heading = article.getHeading();
        text = article.getArticleText();
        category = article.getCategory();
    }
}
