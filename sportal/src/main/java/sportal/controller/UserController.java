package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.SportalApplication;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.UserLoginRequestDTO;
import sportal.model.dto.UserLoginResponseDTO;
import sportal.model.dto.UserRegisterRequestDTO;
import sportal.model.dto.UserRegisterResponseDTO;
import sportal.model.pojo.User;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.DBException;
import sportal.model.dto.*;
import sportal.service.UserService;
import sportal.util.SessionManager;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO userDTO, HttpSession ses){
        if(sessionManager.userAlreadyLogged(ses)){
            throw new BadRequestException("Trying to register while already logged");
        }
        return userService.registerUser(userDTO);
    }

    @PostMapping("/users/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO userDTO, HttpSession ses){
        if(sessionManager.userAlreadyLogged(ses)){
            throw new BadRequestException("Already logged");
        }
        UserLoginResponseDTO responseDto = userService.loginUser(userDTO);
        sessionManager.loginUser(ses, responseDto.getId());
        return responseDto;
    }

    @PostMapping("/users/logout")
    public UserLogoutDTO logout(HttpSession ses){
        return sessionManager.logoutUser(ses);
    }

    @PutMapping("/users")
    public UserWithoutPasswordResponseDTO editUser(@RequestBody UserDTO requestDto, HttpSession session){
        User user = sessionManager.getLoggedUser(session);
        return userService.editProfile(requestDto,user);
    }

    @PatchMapping("/users")
    public UserWithoutPasswordResponseDTO changePassword(@RequestBody UserDTO requestDto, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            return userService.changePassword(requestDto, user);
        }
    }

    @DeleteMapping("/users")
    public UserWithoutPasswordResponseDTO deleteProfile(@RequestBody UserDTO userDTO, HttpSession ses){
        if (sessionManager.getLoggedUser(ses) == null) {
            throw new AuthenticationException("You have to be logged in!");
        } else {
            User user = sessionManager.getLoggedUser(ses);
            try {
                UserWithoutPasswordResponseDTO deletedUser = userService.deleteProfile(userDTO, user);
                ses.invalidate();
                return deletedUser;
            } catch (SQLException e) {
                log(e);
                throw new DBException("Something went wrong");
            }
        }
    }

    @GetMapping("/users/{id}")
    public UserWithCommentsDTO getUserByID(@PathVariable int id, HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(userIsAdmin(loggedUser)) {
            return userService.getUserById(id);
        }
        else{
            throw new AuthenticationException("Request requires admin privileges");
        }
    }

    @GetMapping("/users")
    public List<UserWithoutPasswordResponseDTO> getAllUsers(HttpSession ses){
        User loggedUser = sessionManager.getLoggedUser(ses);
        if(userIsAdmin(loggedUser)) {
            return userService.getAllUsers();
        }
        else{
            throw new AuthenticationException("Request requires admin privileges");
        }
    }


    @GetMapping("/users/comments")
    public List<UserWithCommentsDTO> getAllUsersWithComments(HttpSession ses) {
        User loggedUser = sessionManager.getLoggedUser(ses);
        if (userIsAdmin(loggedUser)) {
            return userService.getAllUsersWithComments();
        } else {
            throw new AuthenticationException("Request requires admin privileges");
        }
    }

    @PutMapping("/users/reset-password")
    public void resetPassword(@RequestBody UserLoginRequestDTO request){
        userService.resetPassword(request.getUsername());
    }

    public boolean userIsAdmin(User user){
        return userService.userIsAdmin(user);
    }
}
