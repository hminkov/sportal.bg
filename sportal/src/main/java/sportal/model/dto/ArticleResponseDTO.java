package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.ArticleCategory;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import java.util.ArrayList;
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
    private UserIDResponseDTO author;
    private ArticleCategory category;
    private List<Comment> comments;

    public ArticleResponseDTO(Article article){
        id = article.getId();
        author = new UserIDResponseDTO(article.getAuthor());
        heading = article.getHeading();
        text = article.getArticleText();
        category = article.getCategory();
        views = article.getViews();
        comments = commentWithoutParents(article.getComments());
    }

    private List<Comment> commentWithoutParents(List<Comment> allComments){
        List<Comment> orphanComments = new ArrayList<>();
        if(allComments.size() == 0){
            return orphanComments;
        }
        for(Comment c : allComments){
            if(c.getParentComment() == null){
                orphanComments.add(c);
            }
        }
        return orphanComments;
    }
}
