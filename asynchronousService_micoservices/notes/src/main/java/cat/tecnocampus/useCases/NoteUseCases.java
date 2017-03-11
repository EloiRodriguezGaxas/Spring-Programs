package cat.tecnocampus.useCases;

import cat.tecnocampus.dataBaseRepositories.NoteRepository;
import cat.tecnocampus.domain.Note;
import cat.tecnocampus.domain.NoteBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by roure on 20/09/2016.
 *
 * All methods update the database
 */
@Service("userUseCases")
public class NoteUseCases {
	
    private final NoteRepository noteRepository;

    public NoteUseCases(NoteRepository NoteRepository) {
        this.noteRepository = NoteRepository;
    }

    public Note addUserNote(String userLab, String title, String contents) {
        LocalDateTime now = LocalDateTime.now();
        Note note = new NoteBuilder().title(title).content(contents).
                time(now).timeEdit(now).createNote();
        noteRepository.save(note, userLab);
        return note;
    }

    public Note addUserNote(String userLab, Note Note) {
        noteRepository.save(Note, userLab);

        return Note;
    }

    public Note updateUserNote(Note note, String owner) {
        note.setDateEdit(LocalDateTime.now());
        noteRepository.updateNote(note, owner);
        return note;
    }

    public List<Note> getUserNotes(String userName) {
        return noteRepository.findAllFromUser(userName);
    }

    public Note getNote (String title) {
        return noteRepository.findOne(title);
    }

    public void deleteNote(String owner) {
        noteRepository.delete(owner);
    }

    public void deleteNotesFromUser (String nick) throws Exception {
        try {
            this.noteRepository.deleteFromUser(nick);
        } catch (Exception e) {
            throw new Exception("User Not Found");
        }
    }

    public boolean existsTitle(String title) {
        return noteRepository.existsNoteTitle(title);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
}
