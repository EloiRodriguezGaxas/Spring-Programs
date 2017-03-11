package cat.tecnocampus.dataBaseRepositories;

import cat.tecnocampus.domain.Note;
import cat.tecnocampus.domain.NoteBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by roure on 20/09/2016.
 */
@Repository
public class NoteRepository {
	
    private final JdbcTemplate jdbcTemplate;

    public NoteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Note> findAll() {
        return jdbcTemplate.query("Select * from note", new NoteMapper());
    }

    public Note findOne(String id) {
        return jdbcTemplate.queryForObject("Select * from note where title = ?", new NoteRepository.NoteMapper(), id);
    }

    public List<Note> findAllFromUser(String username) {
        return jdbcTemplate.query("select * from note where owner = ?", new NoteMapper(), username);
    }

    public int save(Note Note, String owner) {
        return jdbcTemplate.update("insert into note (title, content, date_creation, date_edit, owner) values(?, ?, ?, ?, ?)",
                Note.getTitle(), Note.getContent(), Timestamp.valueOf(Note.getDateCreation()), Timestamp.valueOf(Note.getDateEdit()), owner);
    }

    public int delete(String id) {
        return jdbcTemplate.update("DELETE FROM note WHERE title = ?", id);
    }

    public int deleteFromUser(String id) throws Exception{

        try {
            return jdbcTemplate.update("DELETE FROM note WHERE owner = ?", id);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public int updateNote(Note note, String owner) {
        return jdbcTemplate.update("update note set content = ?, date_edit = ? where title = ? AND owner = ?",
                note.getContent(), LocalDateTime.now(), note.getTitle(), owner);
    }

    public boolean existsNoteTitle(String title) {
        int countOfNotes = jdbcTemplate.queryForObject(
                "select count(*) from note where title = ?", Integer.class, title);
        return countOfNotes > 0;
    }

    private final class NoteMapper implements RowMapper<Note> {
        @Override
        public Note mapRow(ResultSet resultSet, int i) throws SQLException {
            return new NoteBuilder()
            		.title(resultSet.getString("title"))
                    .content(resultSet.getString("content"))
                    .time(resultSet.getTimestamp("date_creation").toLocalDateTime())
                    .timeEdit(resultSet.getTimestamp("date_edit").toLocalDateTime())
                    .createNote();
        }
    }

}
