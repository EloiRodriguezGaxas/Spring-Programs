package cat.tecnocampus.webControllers;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cat.tecnocampus.domain.UserLab;
import cat.tecnocampus.security.SecurityService;
import cat.tecnocampus.security.UserSecurityRepository;
import cat.tecnocampus.useCases.UserUseCases;

/**
 * Created by roure on 28/10/2016.
 */
@Profile("custom_jdbc_auth")
@Controller
public class CreateUserControllerCustomJdbcAuth {
	
    private final UserUseCases userUseCases;
    
    private final SecurityService securityService;
    
    private final UserSecurityRepository userSecurityRepository;

    public CreateUserControllerCustomJdbcAuth(UserUseCases userUseCases, SecurityService securityService, UserSecurityRepository userSecurityRepository) {
        this.userUseCases = userUseCases;
        this.securityService = securityService;
        this.userSecurityRepository = userSecurityRepository;
    }

    @GetMapping("/createuser")
    public String createUser(Model model) {
        model.addAttribute(new UserLab());
        return "userform";
    }

    @PostMapping("/createuser")
    public String processCreateUser(@Valid UserLab user, BindingResult errors, Model model,
                                    RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
        	return "userform";
        }

        userUseCases.registerUser(user);

        loggingInUser(user.getUsername(), user.getPassword());
        
        redirectAttributes.addAttribute("username", user.getUsername());
        redirectAttributes.addAttribute("pepe", "pepe"); // this attribute shows in the calling url as a parameter
        redirectAttributes.addFlashAttribute("userLab", user);

        //return "redirect:users/" + user.getUsername(); //this is dangerous because username can contain a dangerous string (sql injection)
        
        return "redirect:/users/{username}"; //in this way username is escaped and dangerous chars changed
    }

    @GetMapping("/loggedInUser")
    public String getAuthenticatedUser(@AuthenticationPrincipal User user, Model model) {
        
    	model.addAttribute("username", user.getUsername());
        return "hello";
    }

    private void loggingInUser(String username, String password) {
        //saving to authoritation database
        userSecurityRepository.save(username, password);

        //actually logging in
        securityService.login(username,password);
    }

}
