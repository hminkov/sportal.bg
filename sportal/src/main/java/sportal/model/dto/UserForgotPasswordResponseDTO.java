package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserForgotPasswordResponseDTO {
    private String message;

    public UserForgotPasswordResponseDTO(String message){
        this.message = message;
    }
}
