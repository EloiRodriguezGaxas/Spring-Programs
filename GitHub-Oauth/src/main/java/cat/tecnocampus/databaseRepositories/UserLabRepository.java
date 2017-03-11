package cat.tecnocampus.databaseRepositories;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.domain.UserLabBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class UserLabRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private final NoteLabRepository noteLabRepository;

    public UserLabRepository(JdbcTemplate jdbcTemplate, NoteLabRepository noteLabRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.noteLabRepository = noteLabRepository;
    }

    public List<UserLab> findAll() {
        return jdbcTemplate.query("Select * from user_lab", new UserLabMapper());
    }

    public List<UserLab> findAllLazy() {
        return jdbcTemplate.query("Select * from user_lab", (rs, i) -> mapUserLab(rs));
    }

    public UserLab findOne(String userName) {
        return jdbcTemplate.queryForObject("Select * from user_lab where username = ?", new UserLabMapper(), userName);
    }

    public int save(UserLab userLab) {
        int userUpdate = jdbcTemplate.update("insert into user_lab values(?, ?, ?, ?)", userLab.getUsername(), userLab.getName(), userLab.getSecondName(), userLab.getEmail());
        noteLabRepository.saveUserNotes(userLab);

        return userUpdate;
    }

    public int delete(String username) {
        return jdbcTemplate.update("delete from user_lab where username = ?", username);
    }

    public int saveTok (String username, String token) {
        return jdbcTemplate.update("UPDATE user_lab SET token=? WHERE username=?", token, username);
    }

    public int saveTokType (String username, String token) {
        return jdbcTemplate.update("UPDATE user_lab SET tokenType=? WHERE username=?", token, username);
    }

    private final class UserLabMapper implements RowMapper<UserLab> {
        @Override
        public UserLab mapRow(ResultSet resultSet, int i) throws SQLException {
            UserLab userLab = mapUserLab(resultSet);
            
            List<NoteLab> notes = noteLabRepository.findAllFromUser(userLab.getUsername());
            userLab.addNotes(notes);
            return userLab;
        }
    }
    
    private UserLab mapUserLab(ResultSet resultSet) throws SQLException {
    	return new UserLabBuilder()
    				.username(resultSet.getString("username"))
    				.name(resultSet.getString("name"))
    				.secondname(resultSet.getString("second_name"))
    				.email(resultSet.getString("email"))
                    .token(resultSet.getString("token"))
                    .tokenType(resultSet.getString("tokenType"))
    				.createUserLab();
    }

}
