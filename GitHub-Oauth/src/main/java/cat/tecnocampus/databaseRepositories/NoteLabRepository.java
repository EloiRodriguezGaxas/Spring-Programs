package cat.tecnocampus.databaseRepositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import cat.tecnocampus.domain.BagNoteLab;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.NoteLabBuilder;
import cat.tecnocampus.domain.UserLab;

/**
 * Created by roure on 20/09/2016.
 */
@Repository
public class NoteLabRepository {
	
    private final JdbcTemplate jdbcTemplate;

    public NoteLabRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<NoteLab> findAll() {
        return jdbcTemplate.query("Select * from note_lab", new NoteLabMapper());
    }

    public NoteLab findOne(long id) {
        return jdbcTemplate.queryForObject("Select * from note_lab where id = ?", new NoteLabRepository.NoteLabMapper(), id);
    }

    public List<NoteLab> findAllFromUser(String username) {
        return jdbcTemplate.query("select * from note_lab where owner = ?", new NoteLabMapper(), username);
    }

    public int save(NoteLab noteLab, UserLab owner) {
        return jdbcTemplate.update("insert into note_lab (title, content, date_creation, date_edit, owner) values(?, ?, ?, ?, ?)",
                noteLab.getTitle(), noteLab.getContent(), Timestamp.valueOf(noteLab.getDateCreation()), Timestamp.valueOf(noteLab.getDateEdit()), owner.getUsername());
    }

    public int delete(String id) {
        return jdbcTemplate.update("DELETE FROM note_lab WHERE title = ?", id);
    }

    public int[] saveUserNotes(UserLab owner) {
        return saveNotes(owner, owner.getNotesAsList());
    }

    public int[] saveUserBag(UserLab userLab, BagNoteLab bagNoteLab) {
        return saveNotes(userLab, bagNoteLab.getNotes());
    }

    private int[] saveNotes(UserLab userLab, List<NoteLab> listNotes) {
        return jdbcTemplate.batchUpdate("INSERT INTO note_lab (title, content, date_creation, date_edit, owner) values(?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                NoteLab note = listNotes.get(i);
                preparedStatement.setString(1, note.getTitle());
                preparedStatement.setString(2, note.getContent());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(note.getDateCreation()));
                preparedStatement.setTimestamp(4, Timestamp.valueOf(note.getDateEdit()));
                preparedStatement.setString(5, userLab.getUsername());
            }

            @Override
            public int getBatchSize() {
                return listNotes.size();
            }
        });
    }

    public int updateNote(NoteLab note) {
        return jdbcTemplate.update("update note_lab set title = ?, content = ?, date_edit = ? where date_ creation = ?",
                note.getTitle(), note.getContent(), LocalDateTime.now(), note.getDateCreation());
    }

    public boolean existsNoteTitle(String title) {
        int countOfNotes = jdbcTemplate.queryForObject(
                "select count(*) from note_lab where title = ?", Integer.class, title);
        return countOfNotes > 0;
    }

    private final class NoteLabMapper implements RowMapper<NoteLab> {
        @Override
        public NoteLab mapRow(ResultSet resultSet, int i) throws SQLException {
            return new NoteLabBuilder()
            		.title(resultSet.getString("title"))
                    .content(resultSet.getString("content"))
                    .time(resultSet.getTimestamp("date_creation").toLocalDateTime())
                    .timeEdit(resultSet.getTimestamp("date_edit").toLocalDateTime())
                    .createNoteLab();
        }
    }

}
