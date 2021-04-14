package sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sportal.controller.AbstractController;
import sportal.exceptions.AuthenticationException;
import sportal.model.dto.UserLogoutDTO;
import sportal.model.pojo.User;
import sportal.model.repository.IUserRepository;

import javax.servlet.http.HttpSession;

@Component
public class SessionManager extends AbstractController {

    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";

    @Autowired
    private IUserRepository repository;

    public User getLoggedUser(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You have to log in!");
        }
        else{
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            return repository.findById(userId).get();
        }
    }

    public void loginUser(HttpSession ses, int id) {
        ses.setAttribute(LOGGED_USER_ID, id);
    }

    public UserLogoutDTO logoutUser(HttpSession ses) {
        ses.invalidate();
        UserLogoutDTO logoutUserDTO = new UserLogoutDTO();
        return logoutUserDTO;
    }


    public boolean userAlreadyLogged(HttpSession ses) {
        return ses.getAttribute(LOGGED_USER_ID) != null;
    }
}
