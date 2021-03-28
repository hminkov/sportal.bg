package sportal.util;

import sportal.exceptions.BadRequestException;

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
}
