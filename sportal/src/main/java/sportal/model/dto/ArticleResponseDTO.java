package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ArticleResponseDTO {

    private int id;
    private String heading;
    private String text;
    private int views;
    private User author;
    private ArticleCategory category;
    private List<Comment> comments;

    public ArticleResponseDTO(Article article){
        id = article.getId();
        author = article.getAuthor();
        heading = article.getHeading();
        text = article.getArticleText();
        category = article.getCategory();
        views = article.getViews();
        comments = article.getComments();
    }
}
