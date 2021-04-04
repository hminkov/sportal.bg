package sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import sportal.controller.UserController;
import sportal.exceptions.BadRequestException;
import sportal.model.pojo.User;

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

    public static void validateUsername(String username){
        username = username.trim();
        if(username.isEmpty()){
            throw new BadRequestException("Username cannot be empty");
        }
        if(username.length() < 3){
            throw new BadRequestException("Username too short");
        }
    }

    public static void validateText(String text){
        String textTrimmed = text.trim();
        if(textTrimmed.isEmpty()){
            throw new BadRequestException("Text field can't be empty");
        }
    }

    public static void validatePaging(int page, int result){
        if(page < 0){
            throw new BadRequestException("Page should be greater than 0");
        }
        if(result < 0){
            throw new BadRequestException("Result should be greater than 0");
        }
    }
}
