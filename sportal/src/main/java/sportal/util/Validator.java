package sportal.util;

import sportal.exceptions.BadRequestException;

public class Validator {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static void validateEmail(String email){
        if(email == null){
            throw new NullPointerException("Email cannot be empty");
        }
        if(email.isEmpty()){
            throw new BadRequestException("Email cannot be empty");
        }
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

    public static void validatePassword(String password) {
        if(password == null){
            throw new NullPointerException("Password cannot be empty");
        }
        if(password.isEmpty()){
            throw new BadRequestException("Password cannot be empty");
        }
        passwordFormatValidator(password);
    }

    public static void validateUsername(String username){
        if(username == null){
            throw new NullPointerException("Username cannot be empty");
        }
        if(username.isEmpty()){
            throw new BadRequestException("Username cannot be empty");
        }
        username = username.trim();
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
        if(page < 1){
            throw new BadRequestException("Result should be a positive number");
        }
        if(result < 1){
            throw new BadRequestException("Result should be a positive number");
        }
    }
}
