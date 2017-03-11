package cat.tecnocampus.domain;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by roure on 19/09/2016.
 */

public class Note extends ResourceSupport implements Serializable{

    private String title;
    
    private String content;

    private final LocalDateTime dateCreation;

    private LocalDateTime dateEdit;

    public Note(String title, String content, LocalDateTime time, LocalDateTime timeEdit) {
        this.setTitle(title);
        this.setContent(content);
        this.dateCreation = time;
        this.setDateEdit(timeEdit);
    }

    public Note() {
        dateCreation = LocalDateTime.now();
        dateEdit=dateCreation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(LocalDateTime date_edit) {
        this.dateEdit = date_edit;
    }

    public String toString(){
        return "Note: "+this.title+", Content: "+ this.content;
    }
}
