package cat.tecnocampus.domain;

import java.time.LocalDateTime;

public class NoteBuilder {
    private String title;
    private String content;
    private LocalDateTime time;
    private LocalDateTime timeEdit;

    public NoteBuilder title(String title) {
        this.title = title;
        return this;
    }

    public NoteBuilder content(String content) {
        this.content = content;
        return this;
    }

    public NoteBuilder time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public NoteBuilder timeEdit(LocalDateTime timeEdit) {
        this.timeEdit = timeEdit;
        return this;
    }

    public Note createNote() {
        return new Note(title,content,time,timeEdit);
    }
}