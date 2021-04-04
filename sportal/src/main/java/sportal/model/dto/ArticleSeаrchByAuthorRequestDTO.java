package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ArticleSeаrchByAuthorRequestDTO {

    private String username;
    private int page;
    private int resultsPerPage;
}
