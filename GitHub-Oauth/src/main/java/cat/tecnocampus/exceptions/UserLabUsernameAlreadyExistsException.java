package cat.tecnocampus.exceptions;

/**
 * Created by roure on 12/10/2016.
 */
public class UserLabUsernameAlreadyExistsException extends RuntimeException {
	
	private String username;
	
    public UserLabUsernameAlreadyExistsException(String username) {
        super("Username: " + username + " already exists");
        this.username = username;
    }

	public String getUsername() {
		return username;
	}
}
