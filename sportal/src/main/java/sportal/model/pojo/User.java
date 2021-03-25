package sportal.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.RegisterRequestUserDTO;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="users")
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    public User(RegisterRequestUserDTO userDTO){
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        email = userDTO.getEmail();
    }
//    @ManyToOne
//    @JoinColumn("id")
//    private Role role;
}
