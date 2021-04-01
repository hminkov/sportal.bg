package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleHeadingSearchRequestDTO {

    private String heading;
    private int page;
    private int resultsPerPage;
}
