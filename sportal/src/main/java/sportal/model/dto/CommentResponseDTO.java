package sportal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {

    private int id;
    private String text;
    private LocalDateTime postDate;
    private List<CommentResponseDTO> replies;
    private int likes;
    private int dislikes;

    public CommentResponseDTO(Comment comment){
        this.id = comment.getId();
        this.text = comment.getCommentText();
        this.postDate = comment.getPostDate();
        this.likes = comment.getLikes().size();
        this.dislikes = comment.getDislikes().size();
        replies = new ArrayList<>();
        for(Comment reply : comment.getReplies()){
            replies.add(new CommentResponseDTO(reply));
        }
    }
}
