package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Component
public class UserLoginRequestDTO {

    @NotNull(message = "Username cannot be null!")
    private String username;
    @NotNull(message = "Password cannot be null!")
    @Min(value=6, message="The password should be with at least 6 symbols")
    private String password;

}
