package sportal.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.UserRegisterRequestDTO;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "users_like_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    @JsonManagedReference
    private Set<Comment> likedComments;
    @ManyToMany
    @JoinTable(
            name = "users_like_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    @JsonManagedReference
    private Set<Article> likedArticles;
    @ManyToMany
    @JoinTable(
            name = "users_dislike_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    @JsonManagedReference
    private Set<Comment> dislikedComments;
    @ManyToMany
    @JoinTable(
            name = "users_dislike_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    @JsonManagedReference
    private Set<Article> dislikedArticles;
    @ManyToMany
    @JoinTable(
            name = "users_have_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @JsonBackReference
    private List<Role> roles;

    public User(UserRegisterRequestDTO userDTO){
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        email = userDTO.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
