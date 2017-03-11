package cat.tecnocampus.domain;

public class UserBuilder {
    private String username;
    private String name;
    private String secondname;
    private String email;
    private String password;

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder secondname(String secondname) {
        this.secondname = secondname;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public User createUser() {
        User u = new User(username, name, secondname, email);
        u.setPassword(password);
        return u;
    }
}