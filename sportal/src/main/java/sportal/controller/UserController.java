package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sportal.controller.validations.LoginValidator;
import sportal.model.dto.LoginRequestUserDTO;
import sportal.model.dto.LoginResponseUserDTO;
import sportal.model.dto.RegisterRequestUserDTO;
import sportal.model.dto.RegisterResponseUserDTO;
import sportal.model.pojo.User;
import sportal.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
@Component
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users/register")
    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO, HttpSession ses){
//        LoginValidator.validateUser(ses, "Registration is redundant, you are already an active user");
        return userService.registerUser(userDTO);
    }

    @PostMapping("/users/login")
    public LoginResponseUserDTO login(@RequestBody LoginRequestUserDTO userDTO, HttpSession ses){
//        LoginValidator.validateUser(ses,"This user has already logged in");
        LoginResponseUserDTO responseDto = userService.loginUser(userDTO);
        sessionManager.loginUser(ses, responseDto.getId());
        return responseDto;
    }

    @PostMapping("/users/{id}/logout")
    public void logout(HttpSession ses){
        sessionManager.logoutUser(ses);
    }

    @GetMapping("/users/{id}")
    public LoginResponseUserDTO userInfoById(@PathVariable int id){
        return userService.getUserById(id);
    }

    public boolean userIsAdmin(User user){
        return userService.userIsAdmin(user);
    }

}
