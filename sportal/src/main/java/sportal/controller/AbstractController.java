package sportal.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sportal.SportalApplication;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.exceptions.WrongCredentialsException;
import sportal.model.dto.ErrorDTO;

import java.sql.SQLException;
import java.util.Arrays;

@RestController
public class AbstractController {


    protected static final Logger LOGGER = LogManager.getLogger(SportalApplication.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e){
        log(e);
        return new ErrorDTO(e.getMessage() + " - error " + HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e){
        log(e);
        return new ErrorDTO(e.getMessage() + " - error " + HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleNotAuthorized(AuthenticationException e){
        log(e);
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleWrongCredentials(WrongCredentialsException e){
        log(e);
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleSQL(SQLException e){
        log(e);
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.BAD_REQUEST);
    }

    protected void log(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < stackTraceElements.length; i++) {
            str.append(Arrays.toString(stackTraceElements) + " ");
        }
        LOGGER.log(Level.ALL, String.valueOf(str));
    }
}
