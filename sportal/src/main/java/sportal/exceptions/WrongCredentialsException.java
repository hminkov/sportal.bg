package sportal.exceptions;

public class WrongCredentialsException extends RuntimeException{

    public WrongCredentialsException(String msg){
        super(msg);
    }

}
