package sportal.exceptions;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String msg){
        super(msg);
    }
}
