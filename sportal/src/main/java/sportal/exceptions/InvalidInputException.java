package sportal.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String messages){
        super(messages);
    }
}
