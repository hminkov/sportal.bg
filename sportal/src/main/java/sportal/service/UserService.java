package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.model.dao.UserDAO;
import sportal.model.dto.LoginRequestUserDTO;
import sportal.model.dto.LoginResponseUserDTO;
import sportal.model.dto.RegisterRequestUserDTO;
import sportal.model.dto.RegisterResponseUserDTO;
import sportal.exceptions.WrongCredentialsException;

import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;
import sportal.util.Validator;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserDAO userDao;


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

    public UserWithCommentsDTO getUserById(int id) {
        Optional<User> schrodingerUser = userRepository.findById(id);
        if(schrodingerUser.isPresent()){
            return new UserWithCommentsDTO(schrodingerUser.get());
        }
        else{
            throw new NotFoundException("User not found");
        }
    }

    public List<UserIDResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserIDResponseDTO> userIDResponseDTO = new ArrayList<>();
        for(User u : users){
            userIDResponseDTO.add(new UserIDResponseDTO(u));
        }
        return userIDResponseDTO;
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

//    public String editProfile(UserDTO userDTO, int id){
//        Optional<User> u = userRepository.findById(id);
//        if (u.isPresent()) {
//            User user = u.get();
//            PasswordEncoder encoder = new BCryptPasswordEncoder();
//            if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
//                //check if email format is correct
//                //validate if email already exists
//                user.setEmail();
//                //validate if username already exists
//                user.setUsername(u);
//                Validator.changePassword(userDTO, user, userRepository);
//            } else {
//                throw new WrongCredentialsException("Passwords does not match. Try again!");
//            }
//        } else {
//            throw new NotFoundException("User not found");
//        }
//    }

    public String deleteProfile(int userId, HttpSession ses) throws SQLException {
        Optional<User> u = userRepository.findById(userId);
        if (u.isPresent()) {
            userDao.deleteUser(userId);
        }
        else {
            throw new NotFoundException("User not found!");
        }
        ses.invalidate();
        return "Profile successfully deleted. Hope we will see you again soon.";
    }

    public String changePassword(UserDTO userDTO, int userId){
        Optional<User> u = userRepository.findById(userId);
        if (u.isPresent()) {
            User user = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
                return Validator.changePassword(userDTO, user, userRepository);
            } else {
                throw new WrongCredentialsException("Passwords does not match. Try again!");
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public boolean userIsAdmin(User user) {
        return userDao.userIsAdmin(user);
    }
}
