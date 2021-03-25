package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
//    @OneToMany(mappedBy = "parentArticle")
//    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ArticleCategory category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @OneToMany(mappedBy = "article")
    private List<ArticleImage> images;
}
