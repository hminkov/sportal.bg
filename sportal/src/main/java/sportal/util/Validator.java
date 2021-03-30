package sportal.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sportal.exceptions.BadRequestException;
import sportal.model.dto.UserDTO;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;

public class Validator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static void emailFormatValidator(String email){
        if (!email.matches(EMAIL_REGEX)) {
            throw new BadRequestException("Email is not valid");
        }
    }

    public static void passwordFormatValidator(String password){
        if (!password.matches(PASSWORD_REGEX)) {
            throw new BadRequestException("Password format is not valid. " +
                    "Please make sure your password contains at least 8 characters, " +
                    "one digit, one lower alpha char and one upper alpha char");
        }
    }

    public static String changePassword(UserDTO userDTO, User user, IUserRepository userRepository){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userDTO.getNewPassword().equals(userDTO.getConfirmationPassword())) {
            if(!userDTO.getNewPassword().equals(user.getPassword())) {
                passwordFormatValidator(userDTO.getNewPassword());
                user.setPassword(encoder.encode(userDTO.getNewPassword()));
                userRepository.save(user);
                return "Password changed";
            }else{
                return "Your new password should not be the same as the old one! Try again";
            }
        } else {
            throw new BadRequestException("Entered passwords must match!");
        }
    }

    public static void validateUsername(String username){
        username = username.trim();
        if(username.isEmpty()){
            throw new BadRequestException("Username cannot be empty");
        }
        if(username.length() < 3){
            throw new BadRequestException("Username too short");
        }
    }
}
