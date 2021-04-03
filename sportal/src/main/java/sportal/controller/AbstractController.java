package sportal.controller;

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

@RestController
public class AbstractController {


    private static final Logger LOGGER = LogManager.getLogger(SportalApplication.class);

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

    private void log(Exception e) {
        LOGGER.error(e.getMessage());
        LOGGER.trace(e.getStackTrace());
    }
}
