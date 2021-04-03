package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "articles")
@Component
public class Article extends POJO{

    private int views;
    private String heading;
    private String text;
    private LocalDateTime postDate;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany(mappedBy = "article")
    private List<Comment> comments;
    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<ArticleImage> articleImages;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ArticleCategory category;
    @OneToMany(mappedBy = "article")
    private List<ArticleImage> images;
    @ManyToMany(mappedBy = "likedArticles")
    @JsonBackReference
    private Set<User> likes;
    @ManyToMany(mappedBy = "dislikedArticles")
    @JsonBackReference
    private Set<User> dislikes;

    public Article(){
        comments = new ArrayList<>();
        images = new ArrayList<>();
        likes = new HashSet<>();
        dislikes = new HashSet<>();
    }
}
