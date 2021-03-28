package sportal.controller.validations;

import sportal.controller.SessionManager;
import sportal.exceptions.BadRequestException;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;

public class LoginValidator {

    public static SessionManager sessionManager;

    public static void validateUser(HttpSession session, String message){
        User loggedUser = sessionManager.getLoggedUser(session);
        if(loggedUser != null){
            throw new BadRequestException(message);
        }
    }
}
