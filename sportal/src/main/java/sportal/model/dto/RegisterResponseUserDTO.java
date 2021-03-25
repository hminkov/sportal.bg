package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.User;

@NoArgsConstructor
@Setter
@Getter
@Component
public class RegisterResponseUserDTO {

    private int id;
    private String username;
    private String email;

    public RegisterResponseUserDTO(User user){
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
    }
}
