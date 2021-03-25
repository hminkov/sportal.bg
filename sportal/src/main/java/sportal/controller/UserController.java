package sportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hi")
    public String hi(){
        return "Privet";
    }

//    @PutMapping("/users/register")
//    public RegisterResponseUserDTO register(@RequestBody RegisterRequestUserDTO userDTO){
//        return userService.addUser(userDTO);
//    }
}
