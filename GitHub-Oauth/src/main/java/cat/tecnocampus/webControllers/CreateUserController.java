package cat.tecnocampus.webControllers;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.security.UserSecurityRepository;
import cat.tecnocampus.useCases.UserUseCases;

/**
 * Created by roure on 28/10/2016.
 */
@Profile({"jdbc_auth","memory"})
@Controller
@RequestMapping("/createuser")
public class CreateUserController {
	
    private final UserUseCases userUseCases;
    
    private final UserSecurityRepository userSecurityRepository;

    public CreateUserController(UserUseCases userUseCases, UserSecurityRepository userSecurityRepository) {
        this.userUseCases = userUseCases;
        this.userSecurityRepository = userSecurityRepository;
    }

    @GetMapping
    public String createUser(Model model) {
        model.addAttribute(new UserLab());
        return "userform";
    }

    @PostMapping
    public String processCreateUser(@Valid UserLab user, Errors errors, Model model,
                                    RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) { 
        	return "userform";
        }

        userUseCases.registerUser(user);

        //saving to authorization database
        userSecurityRepository.save(user.getUsername(), user.getPassword());

        redirectAttributes.addAttribute("username", user.getUsername());
        redirectAttributes.addAttribute("pepe", "pepe"); // this attribute shows in the calling url as a parameter
        redirectAttributes.addFlashAttribute("userLab", user);

        
        //return "redirect:users/" + user.getUsername(); //this is dangerous because username can contain a dangerous string (sql injection)

        return "redirect:/users/{username}"; //in this way username is scaped and dangerous chars changed
    }
}
