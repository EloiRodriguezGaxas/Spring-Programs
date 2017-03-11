package cat.tecnocampus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roure on 22/10/2016.
 */
public class BagNoteLab implements Serializable {
    private ArrayList<NoteLab> notes;

    public BagNoteLab() {
        this.notes = new ArrayList<>();
    }

    public void addNote(NoteLab note) {
        notes.add(note);
    }

    public List<NoteLab> getNotes() {
        return notes;
    }
}
