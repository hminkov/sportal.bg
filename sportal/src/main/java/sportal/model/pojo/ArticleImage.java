package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "article_images")
public class ArticleImage extends POJO{

    private String url;
    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;
}
