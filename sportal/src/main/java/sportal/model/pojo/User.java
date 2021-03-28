package sportal.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class User extends POJO{

    private String username;
    @JsonIgnore
    private String password;
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<Article> articles;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Comment> comments;

    public User(RegisterRequestUserDTO userDTO){
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        email = userDTO.getEmail();
    }
    @ManyToMany
    @JoinTable(
            name = "users_have_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles;

}
