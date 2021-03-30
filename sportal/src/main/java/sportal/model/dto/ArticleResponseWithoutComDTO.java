package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.Comment;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Component
public class ArticleResponseWithoutComDTO {
    private int id;
    private String heading;
    private String text;
    private int views;
    private UserWithoutPasswordResponseDTO author;
    private ArticleCategory category;

    public ArticleResponseWithoutComDTO(Article article){
        id = article.getId();
        author = new UserWithoutPasswordResponseDTO(article.getAuthor());
        heading = article.getHeading();
        text = article.getArticleText();
        category = article.getCategory();
        views = article.getViews();
    }
}
