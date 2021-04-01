package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleSerchByAuthorRequestDTO {

    private String username;
    private int page;
    private int resultsPerPage;
}
