package sportal.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class PagedSearchRequestDTO {

    @NotNull(message = "Page cannot be null!")
    @Min(value=1, message="Min page value should be 1")
    private int page;
    @NotNull(message = "Results cannot be null!")
    @Min(value=1, message="Min results per page should be 1")
    private int resultsPerPage;
}
