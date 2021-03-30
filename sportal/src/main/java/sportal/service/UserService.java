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
import sportal.util.EmailService;
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
    @Autowired
    private EmailService emailService;


    public RegisterResponseUserDTO registerUser(RegisterRequestUserDTO userDTO){
        //check if email format is correct
        Validator.emailFormatValidator(userDTO.getEmail());
        //check if email exists
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new BadRequestException("Email already exists");
        }
        //check if username is empty or too short
        Validator.validateUsername(userDTO.getUsername());
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
            throw new BadRequestException("The passwords do not match!");
        }
        //save new user to DB by userRepository
        User user = new User(userDTO);
        user = userRepository.save(user);
        RegisterResponseUserDTO responseUserDTO = new RegisterResponseUserDTO(user);
        emailService.sendConfirmationEmail(user);
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

    public List<UserWithoutPasswordResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserWithoutPasswordResponseDTO> userIDResponseDTO = new ArrayList<>();
        for(User u : users){
            userIDResponseDTO.add(new UserWithoutPasswordResponseDTO(u));
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


    public UserWithoutPasswordResponseDTO editProfile(UserDTO userDTO, User userBefore) {
        Optional<User> u = userRepository.findById(userBefore.getId());
        if (u.isPresent()) {
            User userUpdate = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDTO.getPassword(), userBefore.getPassword())) {
                //validate if username or email already exists
                List<User> users = userRepository.findAll();
                for(User user : users){
                    if(user.getEmail().equals(userDTO.getEmail())){
                        throw new BadRequestException("Email already exists");
                    }
                    if(user.getUsername().equals(userDTO.getUsername())){
                        throw new BadRequestException("Username already exists");
                    }
                }
                //check if email format is correct or if such email exists
                if(userDTO.getEmail() != null) {
                    Validator.emailFormatValidator(userDTO.getEmail());
                    userUpdate.setEmail(userDTO.getEmail());
                }
                //check if username format is correct
                if(userDTO.getUsername() != null) {
                    Validator.validateUsername(userDTO.getUsername());
                    userUpdate.setUsername(userDTO.getUsername());
                }
                //validate password format and change
                if (userDTO.getNewPassword().equals(userDTO.getConfirmationPassword())) {
                    if(!userDTO.getNewPassword().equals(userBefore.getPassword())) {
                        Validator.passwordFormatValidator(userDTO.getNewPassword());
                        userUpdate.setPassword(encoder.encode(userDTO.getNewPassword()));
                    }else{
                        throw new BadRequestException("Your new password should not be the same as the old one! Try again");
                    }
                } else {
                    throw new BadRequestException("Entered passwords must match!");
                }
                UserWithoutPasswordResponseDTO currentUser = new UserWithoutPasswordResponseDTO(userUpdate);
                userRepository.save(userUpdate);
                return currentUser;
            } else {
                throw new WrongCredentialsException("Passwords does not match. Try again!");
            }
        } else {
            throw new WrongCredentialsException("Bad credentials. Try again!");
        }
    }

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

    public UserWithoutPasswordResponseDTO changePassword(UserDTO userDTO, User userBefore){
        Optional<User> u = userRepository.findById(userBefore.getId());
        if (u.isPresent()) {
            User userUpdate = u.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDTO.getPassword(), userUpdate.getPassword())) {
                if (userDTO.getNewPassword().equals(userDTO.getConfirmationPassword())) {
                    if(!userDTO.getNewPassword().equals(userUpdate.getPassword())) {
                        Validator.passwordFormatValidator(userDTO.getNewPassword());
                        userUpdate.setPassword(encoder.encode(userDTO.getNewPassword()));
                        UserWithoutPasswordResponseDTO currentUser = new UserWithoutPasswordResponseDTO(userUpdate);
                        userRepository.save(userUpdate);
                        return currentUser;
                    }else{
                        throw new BadRequestException("Your new password should not be the same as the old one! Try again");
                    }
                } else {
                    throw new BadRequestException("Entered passwords must match!");
                }
            } else {
                throw new WrongCredentialsException("Passwords does not match. Try again!");
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public boolean userIsAdmin(User userBefore) {
        return userDao.userIsAdmin(userBefore);
    }

}
