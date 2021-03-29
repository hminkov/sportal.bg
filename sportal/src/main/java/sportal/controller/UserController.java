package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.controller.validations.LoginValidator;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.LoginRequestUserDTO;
import sportal.model.dto.LoginResponseUserDTO;
import sportal.model.dto.RegisterRequestUserDTO;
import sportal.model.dto.RegisterResponseUserDTO;
import sportal.model.pojo.User;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.DBException;
import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.service.UserService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
@Component
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users/register")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO, HttpSession ses){
        if(sessionManager.userAlreadyLogged(ses)){
            throw new BadRequestException("Trying to register while already logged");
        }
        return userService.registerUser(userDTO);
    }

    @PostMapping("/users/login")
    public LoginResponseUserDTO login(@RequestBody LoginRequestUserDTO userDTO, HttpSession ses){
        if(sessionManager.userAlreadyLogged(ses)){
            throw new BadRequestException("Already logged");
        }
        LoginResponseUserDTO responseDto = userService.loginUser(userDTO);
        sessionManager.loginUser(ses, responseDto.getId());
        return responseDto;
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession ses){
        return sessionManager.logoutUser(ses);
    }

    @PutMapping("/users/{id}/password/change")
    public String changePassword(@RequestBody UserDTO userDTO, @PathVariable int id, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            return userService.changePassword(userDTO, id);
        }
    }

//    @PutMapping("/users/{id}/edit")
//    public String editProfile(@RequestBody UserDTO userDTO, @PathVariable int id){
//        if (sessionManager.getLoggedUser(ses) == null) {
//            throw new AuthenticationException("You have to be logged in!");
//        } else {
//            return userService.editProfile(userDTO, id);
//        }
//    }

    @PutMapping("/users/delete")
    public String deleteProfile(HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            try {
                return userService.deleteProfile(user.getId(), ses);
            } catch (SQLException e) {
                throw new DBException("Something went wrong");
            }
        }
    }

    @GetMapping("/users/{id}")
    public UserWithCommentsDTO getUserByID(@PathVariable int id){
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public List<UserIDResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    public boolean userIsAdmin(User user){
        return userService.userIsAdmin(user);
    }

}
