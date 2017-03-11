package cat.tecnocampus.security;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by roure on 19/09/2016.
 */
@Repository
public class UserSecurityRepository {

    private final JdbcOperations jdbcOperations;
    
    private final PasswordEncoder passwordEncoder;

    public UserSecurityRepository(JdbcOperations jdbcOperations, PasswordEncoder passwordEncoder) {
        this.jdbcOperations = jdbcOperations;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSecurity findOne(String username) {
        UserSecurity u = jdbcOperations.queryForObject("Select * from users where username = ?", new UserSecurityMapper(), username);
        u.addRoles(findRoles(username));

        return u;
    }

    public void save(String username, String password) {
        jdbcOperations.update("insert into users (username, password) values(?, ?)", username, passwordEncoder.encode(password));
        jdbcOperations.update("insert into user_roles (username, role) values(?, 'ROLE_USER')", username);
    }

    private List<String> findRoles(String username) {
        return jdbcOperations.query("Select * from user_roles where username = ?", 
        							(rs,i) -> rs.getString("role"), 
        							username);
    }

    /*public String getToken (String user) {
        return jdbcOperations.queryForObject("Select * from users where username = ?", new UserSecurityMapper(), user);
    }*/

    private final class UserSecurityMapper implements RowMapper<UserSecurity> {
        @Override
        public UserSecurity mapRow(ResultSet resultSet, int i) throws SQLException {
            UserSecurity user = new UserSecurity(resultSet.getString("username"), resultSet.getString("password"));
            return user;
        }
    }
}
