package sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String imgUrl;
    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;
}
