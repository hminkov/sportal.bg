package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_categories")
@Component
public class ArticleCategory extends POJO{

    @Column(name = "category_name")
    private String name;
    @JsonBackReference
    @OneToMany(mappedBy = "category")
    private List<Article> articles;
}
