package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class UserRegisterRequestDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
