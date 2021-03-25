package sportal.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.RegisterRequestUserDTO;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    public User(RegisterRequestUserDTO userDTO){
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        email = userDTO.getEmail();
    }
    @ManyToMany
    @JoinTable(
            name = "users_have_roles",
            joinColumns = { @JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles;
}
