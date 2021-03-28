package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class CreateArticleRequestDTO {

    private int creatorId;
    private String category;
    private String heading;
    private String text;
}
