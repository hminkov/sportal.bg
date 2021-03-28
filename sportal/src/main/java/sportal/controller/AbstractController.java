package sportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.exceptions.WrongCredentialsException;
import sportal.model.dto.ErrorDTO;

import java.sql.SQLException;

@RestController
public class AbstractController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e){
        return new ErrorDTO(e.getMessage() + " - error " + HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e){
        return new ErrorDTO(e.getMessage() + " - error " + HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleNotAuthorized(AuthenticationException e){
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleWrongCredentials(WrongCredentialsException e){
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDTO handleSQL(SQLException e){
        return new ErrorDTO(e.getMessage()  + " - error " + HttpStatus.BAD_REQUEST);
    }

}
