package cat.tecnocampus.domain;

import java.time.LocalDateTime;

public class NoteLabBuilder {
    private String title;
    private String content;
    private LocalDateTime time;
    private LocalDateTime timeEdit;

    public NoteLabBuilder title(String title) {
        this.title = title;
        return this;
    }

    public NoteLabBuilder content(String content) {
        this.content = content;
        return this;
    }

    public NoteLabBuilder time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public NoteLabBuilder timeEdit(LocalDateTime timeEdit) {
        this.timeEdit = timeEdit;
        return this;
    }

    public NoteLab createNoteLab() {
        return new NoteLab(title,content,time,timeEdit);
    }
}