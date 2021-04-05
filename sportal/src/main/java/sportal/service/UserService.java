package sportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.model.dao.UserDAO;
import sportal.model.dto.UserLoginRequestDTO;
import sportal.model.dto.UserLoginResponseDTO;
import sportal.model.dto.UserRegisterRequestDTO;
import sportal.model.dto.UserRegisterResponseDTO;
import sportal.exceptions.WrongCredentialsException;

import sportal.model.dto.*;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;
import sportal.util.EmailService;
import sportal.util.OptionalResultVerifier;
import sportal.util.Validator;

import javax.persistence.EntityManager;
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
    @Autowired
    private OptionalResultVerifier orv;
    @Autowired
    EntityManager entityManager;


    public UserRegisterResponseDTO registerUser(UserRegisterRequestDTO userDTO){
        Validator.emailFormatValidator(userDTO.getEmail());
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new BadRequestException("Email already exists");
        }
        Validator.validateUsername(userDTO.getUsername());
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new BadRequestException("Username already exists");
        }
        Validator.passwordFormatValidator(userDTO.getPassword());
        if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        } else {
            throw new BadRequestException("The passwords do not match!");
        }
        User user = new User(userDTO);
        user = userRepository.save(user);
        userDao.insertUserInRolesTable(user.getId());
        UserRegisterResponseDTO responseUserDTO = new UserRegisterResponseDTO(user);
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



    public List<UserWithCommentsDTO> getAllUsersWithComments() {
        List<User> users = userRepository.findAllWithComments();
        List<UserWithCommentsDTO> userWithCommentsDTO = new ArrayList<>();
        for(User u : users){
            userWithCommentsDTO.add(new UserWithCommentsDTO(u));
        }
        return userWithCommentsDTO;
    }

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO dto) {
        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());
        if(optionalUser.isEmpty()){
            throw new AuthenticationException("Wrong credentials");
        }
        else{
            User user = optionalUser.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(dto.getPassword(), user.getPassword())){
                return new UserLoginResponseDTO(user);
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
                if(userRepository.existsByEmail(userDTO.getEmail())){
                    throw new BadRequestException("Email already exists");
                }
                if(userRepository.existsByUsername(userDTO.getUsername())){
                    throw new BadRequestException("Username already exists");
                }
//                check if email format is correct or if such email exists
                if(userDTO.getEmail() != null) {
                    Validator.emailFormatValidator(userDTO.getEmail());
                    userUpdate.setEmail(userDTO.getEmail());
                }
                //check if username format is correct
                if(userDTO.getUsername() != null) {
                    Validator.validateUsername(userDTO.getUsername());
                    userUpdate.setUsername(userDTO.getUsername());
                }
//                validate password format and change
                if(userDTO.getNewPassword() != null) {
                    if (userDTO.getNewPassword().equals(userDTO.getConfirmationPassword())) {
                        if (!userDTO.getNewPassword().equals(userBefore.getPassword())) {
                            Validator.passwordFormatValidator(userDTO.getNewPassword());
                            userUpdate.setPassword(encoder.encode(userDTO.getNewPassword()));
                        } else {
                            throw new BadRequestException("Your new password should not be the same as the old one! Try again");
                        }
                    } else {
                        throw new BadRequestException("Entered passwords must match!");
                    }
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

    public UserWithoutPasswordResponseDTO deleteProfile(UserDTO userDTO, User user) throws SQLException {
        Optional<User> u = userRepository.findById(user.getId());
        if (u.isPresent()) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
                userDao.deleteUser(user.getId());
                entityManager.detach(user);
            }else{
                throw new WrongCredentialsException("Passwords does not match. Try again!");
            }
            Optional<User> deletedUser = userRepository.findById(user.getId());
            return new UserWithoutPasswordResponseDTO(deletedUser.get());
        }
        else {
            throw new NotFoundException("User not found!");
        }
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

    public String resetPassword(String email) {
        return emailService.sendForgotPasswordMail(email);
    }
}
