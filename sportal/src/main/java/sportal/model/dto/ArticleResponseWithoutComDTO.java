package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import java.time.LocalDateTime;


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
    private String category;
    private LocalDateTime localDateTime;

    public ArticleResponseWithoutComDTO(Article article){
        id = article.getId();
        author = new UserWithoutPasswordResponseDTO(article.getAuthor());
        heading = article.getHeading();
        text = article.getText();
        category = article.getCategory().getName();
        views = article.getViews();
        localDateTime = article.getPostDate();
    }
}
