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
public class ImageUploadResponseDTO {
    private int id;

    public ImageUploadResponseDTO(ArticleImage articleImage) {
        this.id = articleImage.getId();
    }
}
