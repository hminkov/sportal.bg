package sportal.model.dto;

import sportal.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class LoginResponseUserDTO {

    private int id;
    private String username;
    private String email;

    public LoginResponseUserDTO(User u){
        id = u.getId();
        username = u.getUsername();
        email = u.getEmail();
    }
}
