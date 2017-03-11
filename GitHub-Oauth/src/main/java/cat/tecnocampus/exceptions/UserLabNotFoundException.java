package cat.tecnocampus.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by roure on 12/10/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="UserLab not found")
public class UserLabNotFoundException extends RuntimeException {
    public UserLabNotFoundException(String message) {
        super(message);
    }
}
