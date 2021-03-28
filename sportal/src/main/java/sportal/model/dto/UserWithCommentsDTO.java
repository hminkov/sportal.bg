package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserWithCommentsDTO {

    private int id;
    private String username;
    private String email;
    List<Comment> commentList;


    public UserWithCommentsDTO(User u) {
        id = u.getId();
        username = u.getUsername();
        email = u.getEmail();
        commentList = u.getComments();
    }
}
