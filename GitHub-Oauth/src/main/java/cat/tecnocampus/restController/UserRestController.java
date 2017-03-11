package cat.tecnocampus.restController;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.useCases.UserUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;

/**
 * Created by eloit on 12/1/2017.
 */

@Controller
@RestController
public class UserRestController {

    @Autowired
    private UserUseCases useCases;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserLab>> listAllUsers () {
        List<UserLab> users = useCases.getUsers();

        for (UserLab user: users) {
            user.add(linkTo(UserRestController.class).withSelfRel());
            user.add(linkTo(UserRestController.class).slash("api").slash("users").slash(user.getUsername()).withSelfRel());
            user.add(linkTo(UserRestController.class).slash("api").slash("users").slash(user.getUsername()).slash("/notes").withSelfRel());


            user.add(linkTo(UserRestController.class).slash("api").slash("users").slash(user.getUsername()).withSelfRel());
        }


        if(users.isEmpty()){
            return new ResponseEntity<List<UserLab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserLab>>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{user}")
    public ResponseEntity<UserLab> findOneUser (@PathVariable(value = "user") String id) {
        UserLab user = useCases.getUser(id);
        if(user == null){
            return new ResponseEntity<UserLab>(HttpStatus.NOT_FOUND);
        }

        user.add(linkTo(UserRestController.class).slash("api").slash("users").slash(id).slash("notes").withSelfRel());
        user.add(linkTo(UserRestController.class).slash("api").slash("users").slash(id).withSelfRel());
        user.add(linkTo(UserRestController.class).slash("api").slash("users").withSelfRel());

        return new ResponseEntity<UserLab>(user, HttpStatus.OK);
    }

    @GetMapping("/api/users/{user}/notes")
    public ResponseEntity<List<NoteLab>> findNotesFromUser (@PathVariable(value = "user") String id) {
        List<NoteLab> notes = useCases.getUserNotes(id);
        if(notes.isEmpty()){
            return new ResponseEntity<List<NoteLab>>(HttpStatus.NO_CONTENT);
        }
        for(NoteLab noteLab: notes) {
            noteLab.add(linkTo(UserRestController.class).slash("api").slash("users").slash(id).withSelfRel());
            noteLab.add(linkTo(UserRestController.class).slash("api").slash("users").withSelfRel());
        }
        return new ResponseEntity<List<NoteLab>>(notes, HttpStatus.OK);
    }

    @PostMapping("/api/createUser")
    public ResponseEntity<Void> createUser(@RequestBody UserLab user, UriComponentsBuilder ucBuilder) {
        if (useCases.getUser(user.getUsername()) == null) {
            useCases.saveUser(user);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/users/{user}").buildAndExpand(user.getUsername()).toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);

        }

        else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

    }



   /* @DeleteMapping("api/users/{user}")
    public ResponseEntity<List<UserLab>> deleteUser(@PathVariable(value = "user") String id ) {

        useCases.deleteUser(id);

        List<UserLab> users = useCases.getUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<UserLab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserLab>>(users, HttpStatus.OK);
    }*/

    @PutMapping("/api/users/{user}/notes/{note}")
    public ResponseEntity<List<NoteLab>> updateNote (@PathVariable(value = "user") String id, @PathVariable(value = "note") String tittle, @RequestBody NoteLab noteLab, UriComponentsBuilder ucBuilder) {

        useCases.deleteNote(tittle);

        UserLab userLab = useCases.getUser(id);
        useCases.addUserNote(userLab, noteLab);

        List<NoteLab> notes = useCases.getUserNotes(id);
        if(notes.isEmpty()){
            return new ResponseEntity<List<NoteLab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<NoteLab>>(notes, HttpStatus.OK);
    }

    @DeleteMapping("api/users/{user}")
    public ResponseEntity<List<UserLab>> deleteUser(@PathVariable(value = "user") String id ) {

        useCases.deleteUser(id);

        List<UserLab> users = useCases.getUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<UserLab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserLab>>(users, HttpStatus.OK);
    }

    @DeleteMapping("/api/users/{user}/notes/{note}")
    public ResponseEntity<List<NoteLab>> deleteNote(@PathVariable(value = "note") String id) {
        useCases.deleteNote(id);
        List<NoteLab> notes = useCases.getUserNotes(id);
        if(notes.isEmpty()){
            return new ResponseEntity<List<NoteLab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<NoteLab>>(notes, HttpStatus.OK);
    }

}
