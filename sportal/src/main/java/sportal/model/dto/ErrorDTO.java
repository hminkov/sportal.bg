package sportal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Component
@AllArgsConstructor
public class ErrorDTO {

    private Date timestamp;
    private String msg;
    private String details;

}
