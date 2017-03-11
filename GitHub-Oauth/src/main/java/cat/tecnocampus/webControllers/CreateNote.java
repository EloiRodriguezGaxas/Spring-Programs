package cat.tecnocampus.webControllers;

import cat.tecnocampus.domain.NoteLab;
import cat.tecnocampus.useCases.UserUseCases;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * Created by josep on 10/1/17.
 */
@Controller
public class CreateNote {
    private final UserUseCases userUseCases;

    public CreateNote(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @GetMapping("/users/{user}/createNote")
    public String getCreateNote(@PathVariable String user, Model model) {

        model.addAttribute("noteLab", new NoteLab());
        model.addAttribute("user", user);

        return "noteForm";
    }

    @PostMapping("/users/{user}/createNote")
    public String postCreateNote(NoteLab noteLab, @PathVariable String user, Model model) {

        userUseCases.addUserNote(userUseCases.getUser(user), noteLab);

        return "redirect:/users/{user}/notes";
    }
}
