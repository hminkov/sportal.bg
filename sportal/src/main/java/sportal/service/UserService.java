package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.model.dto.LoginRequestUserDTO;
import sportal.model.dto.LoginResponseUserDTO;
import sportal.model.dto.RegisterRequestUserDTO;
import sportal.model.dto.RegisterResponseUserDTO;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;
import sportal.util.Validator;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    IUserRepository userRepository;


    public RegisterResponseUserDTO registerUser(RegisterRequestUserDTO userDTO){
        //check if email format is correct
        Validator.emailFormatValidator(userDTO.getEmail());
        //check if email exists
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new BadRequestException("Email already exists");
        }
        //check if username exists
        if(userRepository.findByUsername(userDTO.getUsername()) != null){
            throw new BadRequestException("Username already exists");
        }
        //check if password format is correct
        Validator.passwordFormatValidator(userDTO.getPassword());
        //check if passwords are equal
        if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            //set cached version of the password for this user
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        } else {
            throw new BadRequestException("The passwords does not match!");
        }
        //save new user to DB by userRepository
        User user = new User(userDTO);
        user = userRepository.save(user);
        RegisterResponseUserDTO responseUserDTO = new RegisterResponseUserDTO(user);
        return responseUserDTO;
    }



    public LoginResponseUserDTO getUserById(int id) {
        Optional<User> schrodingerUser = userRepository.findById(id);
        if(schrodingerUser.isPresent()){
            return new LoginResponseUserDTO(schrodingerUser.get());
        }
        else{
            throw new NotFoundException("User not found");
        }
    }

    public LoginResponseUserDTO loginUser(LoginRequestUserDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername());
        if(user == null){
            throw new AuthenticationException("Wrong credentials");
        }
        else{
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(dto.getPassword(), user.getPassword())){
                return new LoginResponseUserDTO(user);
            }
            else{
                throw new AuthenticationException("Wrong credentials");
            }
        }
    }
}
