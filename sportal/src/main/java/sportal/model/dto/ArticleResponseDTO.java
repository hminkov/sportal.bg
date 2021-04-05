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
    private UserWithoutPasswordResponseDTO author;
    private ArticleCategory category;
    private List<CommentResponseDTO> comments;
    private int[] imageIds;
    private int likes;
    private int dislikes;

    public ArticleResponseDTO(Article article){
        id = article.getId();
        author = new UserWithoutPasswordResponseDTO(article.getAuthor());
        heading = article.getHeading();
        text = article.getText();
        category = article.getCategory();
        views = article.getViews();
        comments = new ArrayList<>();
        List<Comment> comments = commentWithoutParents(article.getComments());
        for(Comment comment : comments){
            this.comments.add(new CommentResponseDTO(comment));
        }
        imageIds = extractImageIds(article.getImages());
        likes = article.getLikes().size();
        dislikes = article.getDislikes().size();
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
