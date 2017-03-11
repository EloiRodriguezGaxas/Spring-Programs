package cat.tecnocampus.webControllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cat.tecnocampus.exceptions.UserLabUsernameAlreadyExistsException;

import java.util.Map;

@ControllerAdvice
public class UserControllerAdvice {

	/*
    This method is called whenever a UserLabUsernameAlreadyExistsException is signalled
    We can have Advising Controllers that handle exceptions from all the controllers (no just one).
    The advising controllers must be annotated with @ControllerAdvice and have one or more methods annotated
    with @ExceptionHandler
     */
    @ExceptionHandler
    public String handleUsernameAlreadyExists(UserLabUsernameAlreadyExistsException exception, Model model) {
        model.addAttribute("username", exception.getUsername());

        
        return "error/usernameAlreadyExists";
    }
}