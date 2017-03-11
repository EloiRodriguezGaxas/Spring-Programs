package cat.tecnocampus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * Created by roure on 19/09/2016.
 */
public class User extends ResourceSupport implements Serializable{

    @Size(min=5, max=15)
    private String username;

    @Size(min=5, max=15)
    private String name;

    @Size(min=5, max=15)
    private String secondName;

    @Email
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String token;

    public User () {

    }

    public User(String username, String name, String secondname, String email) {

        this.setUsername(username);
        this.setName(name);
        this.setSecondName(secondname);
        this.setEmail(email);
        this.setToken(null);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String toString() {
        return "Usuari: " + this.username + ", " + this.name + " " + this.secondName + " " + this.email + " " + this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (!getUsername().equals(user.getUsername())) return false;
        if (!getName().equals(user.getName())) return false;
        if (!getSecondName().equals(user.getSecondName())) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        return getPassword() != null ? getPassword().equals(user.getPassword()) : user.getPassword() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSecondName().hashCode();
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}