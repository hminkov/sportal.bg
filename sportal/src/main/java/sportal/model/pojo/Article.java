package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
@Component
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int views;
    private String heading;
    private String articleText;
    private LocalDateTime postDate;
<<<<<<< HEAD
//    @OneToMany(mappedBy = "parentArticle")
//    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ArticleCategory category;
=======
    @OneToMany(mappedBy = "parentArticle")
    private List<Comment> comments;
    @JsonManagedReference
>>>>>>> 00462a67683d659bd339ebd0c0c871dbe918f5da
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @OneToMany(mappedBy = "article")
    private List<ArticleImage> images;
<<<<<<< HEAD
=======
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ArticleCategory category;
>>>>>>> 00462a67683d659bd339ebd0c0c871dbe918f5da
}
