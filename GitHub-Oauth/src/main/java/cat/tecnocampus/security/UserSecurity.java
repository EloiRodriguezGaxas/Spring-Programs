package cat.tecnocampus.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roure on 27/10/2016.
 */
public class UserSecurity {
	
    private String username;
    
    private String password;
    
    private List<String> roles;

    public UserSecurity(String username, String password) {
        this.username = username;
        this.password = password;
        roles = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRoles(List<String> roles) {
        this.roles.addAll(roles);
    }

    public List<String> getRoles() {
        return roles;
    }
}
