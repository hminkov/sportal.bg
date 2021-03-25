package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.User;


@Setter
@Getter
@NoArgsConstructor
@Component
public class LoginResponseUserDTO {

    private int id;
    private String username;
    private String email;

    public LoginResponseUserDTO(User u) {
        id = u.getId();
        username = u.getUsername();
        email = u.getEmail();
    }
}
