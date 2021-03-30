package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.LoginRequestUserDTO;
import sportal.model.dto.LoginResponseUserDTO;
import sportal.model.dto.RegisterRequestUserDTO;
import sportal.model.dto.RegisterResponseUserDTO;
import sportal.model.pojo.User;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.DBException;
import sportal.model.dto.*;
import sportal.service.UserService;
import sportal.util.SessionManager;

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

    @PostMapping("/users")
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

    @PutMapping("/users/edit")
    public UserWithoutPasswordResponseDTO editUser(@RequestBody UserDTO requestDto, HttpSession session){
        User user = sessionManager.getLoggedUser(session);
        return userService.editProfile(requestDto,user);
    }

    @PutMapping("/users")
    public UserWithoutPasswordResponseDTO changePassword(@RequestBody UserDTO requestDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            return userService.changePassword(requestDto, user);
        }
    }

    @DeleteMapping("/users")
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
    public List<UserWithoutPasswordResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    public boolean userIsAdmin(User user){
        return userService.userIsAdmin(user);
    }
}
