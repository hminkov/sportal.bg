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
public class UserIDResponseDTO {

    private int id;
    private String username;
    private String email;

    public UserIDResponseDTO(User u){
        id = u.getId();
        username = u.getUsername();
        email = u.getEmail();
    }
}
