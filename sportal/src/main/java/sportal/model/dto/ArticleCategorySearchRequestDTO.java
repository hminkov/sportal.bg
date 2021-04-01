package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleCategorySearchRequestDTO {

    private int categoryId;
    private int page;
    private int resultsPerPage;
}
