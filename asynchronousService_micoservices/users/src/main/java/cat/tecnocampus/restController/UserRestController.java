package cat.tecnocampus.restController;

import cat.tecnocampus.domain.User;
import cat.tecnocampus.sourceBin.CustomOutputEventSource;
import cat.tecnocampus.useCase.UserUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
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
public class UserRestController {

    @Autowired
    private UserUseCases useCases;

    /*@Autowired
    private CustomOutputEventSource output;
*/
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> listAllUsers () {
        List<User> Users = useCases.getUsers();

        for (cat.tecnocampus.domain.User User: Users) {
           // User.add(linkTo(UserRestController.class).withSelfRel());
            User.add(linkTo(UserRestController.class).slash("users").slash("api").slash(User.getUsername()).withSelfRel());
            User.add(linkTo(UserRestController.class).slash("users").slash("api").slash(User.getUsername()).withSelfRel());
        }
        if(Users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(Users, HttpStatus.OK);
    }

    @GetMapping("/api/{user}")
    public ResponseEntity<User> findOneUser (@PathVariable(value = "user") String id) {
        User User = useCases.getUser(id);
        if(User == null){
            return new ResponseEntity<cat.tecnocampus.domain.User>(HttpStatus.NOT_FOUND);
        }

        User.add(linkTo(UserRestController.class).slash("users").slash("api").slash(id).withSelfRel());
        User.add(linkTo(UserRestController.class).slash("users").slash("api").slash("users").withSelfRel());

        return new ResponseEntity<cat.tecnocampus.domain.User>(User, HttpStatus.OK);
    }

    @PutMapping("/api/updateUser")
    public ResponseEntity updateUser(@RequestBody User user) {
        if (useCases.getUser(user.getUsername()) != null) {
            useCases.updateUser(user);

            user.add(linkTo(UserRestController.class).slash("api").slash(user.getUsername()).withSelfRel());
            user.add(linkTo(UserRestController.class).slash("api").slash("users").withSelfRel());

            return new ResponseEntity<cat.tecnocampus.domain.User>(user, HttpStatus.CREATED);

        }
        else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/createUser")
    public ResponseEntity createUser(@RequestBody User user) {
        if (useCases.getUser(user.getUsername()) == null) {
            useCases.saveUser(user);

            user.add(linkTo(UserRestController.class).slash("api").slash(user.getUsername()).withSelfRel());
            user.add(linkTo(UserRestController.class).slash("api").slash("users").withSelfRel());

            return new ResponseEntity<cat.tecnocampus.domain.User>(user, HttpStatus.CREATED);

        }
        else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

    }

    @DeleteMapping("/api/deleteUser/{user}")
    public ResponseEntity deleteUser (@PathVariable(value = "user") String id) {

        try {

            this.useCases.deleteUser(id);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
