package cat.tecnocampus.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by roure on 31/10/2016.
 */
@Component
public class WebSecurity {

    public boolean checkUserId(Authentication authentication, String id) {
        return id.equals(authentication.getName());
    }
}
