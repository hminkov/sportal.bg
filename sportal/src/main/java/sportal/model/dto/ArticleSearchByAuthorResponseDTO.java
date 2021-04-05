package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;

@NoArgsConstructor
@Getter
@Setter
public class ArticleSearchByAuthorResponseDTO {

    private int id;
    private String heading;
    private String author;

    public ArticleSearchByAuthorResponseDTO(Article article) {
        id = article.getId();
        heading = article.getHeading();
        author = article.getAuthor().getUsername();
    }
}
