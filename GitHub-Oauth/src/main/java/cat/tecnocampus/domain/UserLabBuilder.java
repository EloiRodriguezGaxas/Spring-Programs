package cat.tecnocampus.domain;

public class UserLabBuilder {
    private String username;
    private String name;
    private String secondname;
    private String email;
    private String password;
    private String token;
    private String tokenType;

    public UserLabBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserLabBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserLabBuilder token(String toekn) {
        this.token = toekn;
        return this;
    }

    public UserLabBuilder tokenType(String toeknType) {
        this.tokenType = toeknType;
        return this;
    }

    public UserLabBuilder secondname(String secondname) {
        this.secondname = secondname;
        return this;
    }

    public UserLabBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserLabBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserLab createUserLab() {
        UserLab u = new UserLab(username, name, secondname, email, token, tokenType);
        u.setPassword(password);
        return u;
    }
}