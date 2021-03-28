package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String newPassword;
    private String confirmationPassword;
}
