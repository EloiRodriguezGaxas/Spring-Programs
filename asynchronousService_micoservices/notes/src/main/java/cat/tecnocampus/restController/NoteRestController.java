package cat.tecnocampus.restController;

import cat.tecnocampus.domain.Note;
import cat.tecnocampus.useCases.NoteUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by eloit on 12/1/2017.
 */

@Controller
@RestController
public class NoteRestController {

    @Autowired
    private NoteUseCases useCases;

    @GetMapping("/api/notes/{user}")
    public ResponseEntity<List<Note>> findNotesFromUser (@PathVariable(value = "user") String owner) {
        List<Note> notes = useCases.getUserNotes(owner);
        if(notes.isEmpty()){
            return new ResponseEntity<List<Note>>(HttpStatus.NO_CONTENT);
        }
        for(Note note: notes) {
            note.add(linkTo(NoteRestController.class).slash("api").slash(owner).withSelfRel());
            note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
        }
        return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
    }

    @GetMapping("/api/{title}")
    public ResponseEntity<Note> findNote (@PathVariable(value = "title") String id) {
        Note note = useCases.getNote(id);
        if(note == null){

            note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
            return new ResponseEntity<Note>(HttpStatus.NO_CONTENT);

        }
        note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
        return new ResponseEntity<Note>(note, HttpStatus.OK);
    }


    @PostMapping("/api/{user}/createNote")
    public ResponseEntity<Note> createNote (@PathVariable(value = "user") String owner, @RequestBody Note note, UriComponentsBuilder ucBuilder){

        useCases.addUserNote(owner, note);
        note.add(linkTo(NoteRestController.class).slash("api").slash(owner).withSelfRel());
        note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
        return new ResponseEntity<Note>(note, HttpStatus.CREATED);

    }

    @DeleteMapping("/api/{user}/{note}")
    public ResponseEntity<List<Note>> deleteNote(@PathVariable(value = "note") String owner) {
        useCases.deleteNote(owner);
        List<Note> notes = useCases.getUserNotes(owner);
        if(notes.isEmpty()){
            return new ResponseEntity<List<Note>>(HttpStatus.NO_CONTENT);
        }
        for(Note note: notes) {
            note.add(linkTo(NoteRestController.class).slash("api").slash(owner).withSelfRel());
            note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
        }
        return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
    }

    @PutMapping("/api/{user}/updateNote")
    public ResponseEntity<Note> update (@RequestBody Note note, @PathVariable(value = "user") String owner) {
        try {
            useCases.updateUserNote(note, owner);
            note.add(linkTo(NoteRestController.class).slash("api").slash(owner).withSelfRel());
            note.add(linkTo(NoteRestController.class).slash("api").slash(note.getTitle()).withSelfRel());
            return new ResponseEntity<Note>(note, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
        }
    }

}
