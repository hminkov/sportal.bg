package sportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sportal.exceptions.AuthenticationException;
import sportal.exceptions.BadRequestException;
import sportal.exceptions.NotFoundException;
import sportal.model.dto.ErrorDTO;

@RestController
public class AbstractController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException e){
        return new ErrorDTO(e.getMessage());
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException e){
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleNotAuthorized(AuthenticationException e){
        return new ErrorDTO(e.getMessage());
    }
}
