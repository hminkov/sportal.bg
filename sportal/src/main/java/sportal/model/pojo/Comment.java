package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_comments")
@Component
public class Comment extends POJO{


    private String commentText;
    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replies;

    public Comment(String commentText, LocalDateTime postDate, Article article, User user){
        this.commentText = commentText;
        this.postDate = postDate;
        this.article = article;
        this.user = user;
    }

    public Comment(String commentText, LocalDateTime postDate, Article article, User user, Comment parentComment){
        this.commentText = commentText;
        this.postDate = postDate;
        this.article = article;
        this.user = user;
        this.parentComment = parentComment;
    }


}
