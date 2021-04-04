package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
@Setter
public class ArticleHeadingSearchRequestDTO {

    private String heading;
    private int page;
    private int resultsPerPage;
}
