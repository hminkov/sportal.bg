package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ArticleEditRequestDTO {

    private int id;
    private String heading;
    private String text;
    private String category;

}
