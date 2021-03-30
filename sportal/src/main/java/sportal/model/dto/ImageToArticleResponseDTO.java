package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.ArticleImage;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ImageToArticleResponseDTO {
    private int id;
    private String url;
    private ArticleHeadingDTO article;

    public ImageToArticleResponseDTO(ArticleImage articleImage) {
        this.id = articleImage.getId();
        this.url = articleImage.getUrl();
        this.article = new ArticleHeadingDTO(articleImage.getArticle());
    }
}
