package sportal.util;

import org.springframework.stereotype.Component;
import sportal.exceptions.NotFoundException;

import java.util.Optional;

@Component
public class OptionalResultVerifier {

    public <T> T verifyOptionalResult(Optional<T> optionalResult){
        if(optionalResult.isEmpty()){
            throw new NotFoundException("Cannot locate resource");
        }
        return optionalResult.get();
    }
}
