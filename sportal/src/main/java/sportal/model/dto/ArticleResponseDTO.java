package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.*;

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
    private int[] imageIds;

    public ArticleResponseDTO(Article article){
        id = article.getId();
        author = new UserIDResponseDTO(article.getAuthor());
        heading = article.getHeading();
        text = article.getArticleText();
        category = article.getCategory();
        views = article.getViews();
        comments = commentWithoutParents(article.getComments());
        imageIds = extractImageIds(article.getImages());
    }

    private int[] extractImageIds(List<ArticleImage> images) {
        int[] ids = new int[images.size()];
        for(int i = 0; i < ids.length; i++){
            ids[i] = images.get(i).getId();
        }
        return ids;
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
