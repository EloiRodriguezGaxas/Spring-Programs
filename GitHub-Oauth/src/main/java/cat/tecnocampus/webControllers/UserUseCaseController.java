package cat.tecnocampus.webControllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.useCases.UserUseCases;

/**
 * Created by roure on 05/10/2016.
 */
@Controller
public class UserUseCaseController {
	
    private final UserUseCases userUseCases;

    public UserUseCaseController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @GetMapping
    public String welcome() {
        return "welcome";
    }

    //same as @RequestMapping(path="notes", method= RequestMethod.GET)
/*
    //This is the most implicit it's best to use a more explicit one
    @GetMapping("notes")
    public List<NoteLab> listNotes() {
        return userUseCases.getAllNotes();
    }
*/

    //same as @RequestMapping(path="notes", method= RequestMethod.GET)
    //This is the most explicit
    @GetMapping("notes")
    public String listNotes(Model model) {
        model.addAttribute("noteLabList", userUseCases.getAllNotes());
        return "notes";
    }


/*
    @GetMapping("notes")
    public void listNotes(Model model) {
        model.addAttribute("noteLabList", userUseCases.getAllNotes());
    }
*/

/*
    @GetMapping("/notes")
    public Model listNotes(Model model) {
        model.addAttribute("noteLabList", userUseCases.getAllNotes());
        return model;
    }
*/

/*
    @GetMapping("/notes")
    public ModelAndView listNotes(ModelAndView model) {
        model.addObject("noteLabList", userUseCases.getAllNotes());
        model.setViewName("notes");
        return model;
    }
*/

/*
    //most implicit
    @GetMapping("users")
    public List<UserLab> listUsers() {
        return userUseCases.getUsers();
    }
*/

    //most explicit
    @GetMapping("users")
    public String listUsers(Model model) {
        model.addAttribute("userLabList", userUseCases.getUsers());
        return "users";
    }

    @GetMapping("/usersReqParam")
    public String showUserRequestParameter(@RequestParam String username, Model model) {
        model.addAttribute("userLab", userUseCases.getUser(username));
        return "showUser";
    }
    
    @GetMapping("/users/{user}")
    public String showUser(@PathVariable("user") String user, Model model) {
        //we're going to ask to UserUseCases for a user only if the model
        //doesn't already carry one (from a redirect)
        if (!model.containsAttribute("userLab")) {
            model.addAttribute("userLab",userUseCases.getUser(user));
        }
        return "showUser";
    }

    @GetMapping("/users/{user}/notes")
    public String listUserNotes(@PathVariable String user, Model model) {

        model.addAttribute("userNotesList", userUseCases.getUserNotes(user));
        model.addAttribute("user", user);

        return "userNotes";
    }

    @GetMapping("byebye")
    public String byebye() {
        return "byebye";
    }
}
