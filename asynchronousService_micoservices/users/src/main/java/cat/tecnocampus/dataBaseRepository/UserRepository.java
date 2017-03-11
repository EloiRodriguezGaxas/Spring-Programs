package cat.tecnocampus.dataBaseRepository;

import cat.tecnocampus.domain.User;
import cat.tecnocampus.domain.UserBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by roure on 19/09/2016.
 */
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query("Select * from users", new UserMapper());
    }

    public List<User> findAllLazy() {
        return jdbcTemplate.query("Select * from users", (rs, i) -> mapUser(rs));
    }

    public User findOne(String userName) {
        return jdbcTemplate.queryForObject("Select * from users where username = ?", new UserMapper(), userName);
    }

    public int updateUser(User user) {
        return jdbcTemplate.update("UPDATE users SET name = ?, second_name = ?, email = ? WHERE username = ?",
                user.getName(), user.getSecondName(), user.getEmail(), user.getUsername());
    }

    public int save(User user) {
        int userUpdate = jdbcTemplate.update("insert into users values(?, ?, ?, ?, ?, ?)", user.getUsername(), user.getName(), user.getSecondName(),
                user.getEmail(), user.getPassword(), user.getToken());
        return userUpdate;
    }

    public int delete(String username) throws Exception{
        try {
            return jdbcTemplate.update("delete from users where username = ?", username);
        } catch (Exception e) {
            throw new Exception("User not Found");
        }
    }


    private final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User userLab = mapUser(resultSet);

            return userLab;
        }
    }
    
    private User mapUser(ResultSet resultSet) throws SQLException {
    	return new UserBuilder()
    				.username(resultSet.getString("username"))
    				.name(resultSet.getString("name"))
    				.secondname(resultSet.getString("second_name"))
    				.email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
    				.createUser();
    }

}
