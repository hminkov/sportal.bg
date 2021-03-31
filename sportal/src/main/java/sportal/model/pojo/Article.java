package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "articles")
@Component
public class Article extends POJO{

    private int views;
    private String heading;
    private String articleText;
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

    public Article(){
        comments = new ArrayList<>();
        images = new ArrayList<>();
    }
}
