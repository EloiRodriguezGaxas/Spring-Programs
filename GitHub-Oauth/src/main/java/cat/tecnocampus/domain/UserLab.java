package cat.tecnocampus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by roure on 19/09/2016.
 */
public class UserLab extends ResourceSupport implements Serializable{

    @Size(min=5, max=15)
    private String username;

    @Size(min=5, max=15)
    private String name;

    @Size(min=5, max=15)
    private String secondName;

    @Email
    private String email;

    private Map<String, NoteLab> noteLabs;

    private String password;

    private String token;
    private String tokenType;

    public UserLab() {
        noteLabs = new HashMap<>();
    }

    public UserLab(String username, String name, String secondname, String email) {
        this(username, name, secondname, email, null, null);
    }

    public UserLab(String username, String name, String secondname, String email, String token, String tokenType) {
        this();
        this.setUsername(username);
        this.setName(name);
        this.setSecondName(secondname);
        this.setEmail(email);
        this.token = token;
        this.tokenType = tokenType;
    }
    public String getToken() {return this.token;}

    public String getTokenType() {return this.tokenType;}

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

    public Map<String, NoteLab> getNotes() {
        return this.noteLabs;
    }

    public List<NoteLab> getNotesAsList() {
        Collection<NoteLab> coll = noteLabs.values();

        if (coll instanceof List) {
            return (List) coll;
        }
        else {
            return new ArrayList<>(coll);
        }
    }

    public NoteLab getNote(String title) {
        return noteLabs.get(title);
    }

    public void setnotes(Map<String, NoteLab> noteLabs) {
        this.noteLabs = noteLabs;
    }

    public NoteLab addNote(NoteLab noteLab) {
        if (!noteLabs.containsKey(noteLab.getTitle())) {
            noteLabs.put(noteLab.getTitle(),noteLab);
        } else {
            throw new IllegalArgumentException("Note's title is repeated");
        }

        return noteLab;
    }

    public NoteLab removeNote(NoteLab noteLab) {
        getNotes().remove(noteLab);

        return noteLab;
    }

    public NoteLab removeNote(String title) {
        return noteLabs.remove(title);
    }

    public void addNotes(List<NoteLab> notes) {
        notes.forEach(n -> noteLabs.put(n.getTitle(),n));
    }

    public String toString() {
        return "Usuari: " + this.username + ", " + this.name + " " + this.secondName + " " + this.email + " " + this.password;
    }

    public boolean existsNote(String title) {
        return noteLabs.containsKey(title);
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
        if (o == null || getClass() != o.getClass()) return false;

        UserLab userLab = (UserLab) o;

        if (username != null ? !username.equals(userLab.username) : userLab.username != null) return false;
        if (name != null ? !name.equals(userLab.name) : userLab.name != null) return false;
        if (secondName != null ? !secondName.equals(userLab.secondName) : userLab.secondName != null) return false;
        if (email != null ? !email.equals(userLab.email) : userLab.email != null) return false;
//        if (noteLabs != null ? !noteLabs.equals(userLab.noteLabs) : userLab.noteLabs != null) return false;
        return password != null ? password.equals(userLab.password) : userLab.password == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
//        result = 31 * result + (noteLabs != null ? noteLabs.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}