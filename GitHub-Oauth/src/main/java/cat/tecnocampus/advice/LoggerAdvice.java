package cat.tecnocampus.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by roure on 17/10/2016.
 */
@Aspect
@Component
public class LoggerAdvice {
	
    private static final Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);

    //A pointcut that matches one single method
    @Pointcut("execution(* cat.tecnocampus.webControllers.UserUseCaseController.listUsers(..))")
    public void pointcutListUsers() {}

    @Before("pointcutListUsers()")
    public void beforeListUsers() {
        logger.info("Going to list all users");
    }

    @After("pointcutListUsers()")
    public void afterListUsers() {
        logger.info("Already listed all users");
    }

    //A pointcut that matches all methods having the word "Notes" in any position of methods' name
    @Pointcut("execution(* cat.tecnocampus.webControllers.UserUseCaseController.*Notes*(..))")
    public void pointcutNotes() {}

    @Before("pointcutNotes()")
    public void beforeDealingNotes() {
        logger.info("Going to deal with notes");
    }

    //Around pointcut. Note that this method must return what the proxied method is supposed to return
    @Around("execution(* *.showUserRequestParameter(..))")
    public String dealRequestParam(ProceedingJoinPoint jp) {

        try {
            logger.info("Before showing user (request parameter)");
            //note that showUserRequestParameter is proxied and it must return a string
            // representing the thymeleaf file name
            String res = (String)jp.proceed();
            logger.info("After showing user (request parameter)");
            return res;
        } catch (Throwable throwable) {
            logger.info("Something went wrong (request parameter)");
            throwable.printStackTrace();
            return "error";
        }
    }

    //Getting the parameters of the proxied method
    //@Pointcut("execution(* cat.tecnocampus.webControllers.UserUseCaseController.showUser(String, ..))&&args(name,..)")
    @Pointcut("execution(* cat.tecnocampus.webControllers.UserUseCaseController.showUser(..)) && args(name,..)")
    public void showUserPointcut(String name) {}

    @Before("showUserPointcut(name)")
    public void showUserAdvice(String name) {
        logger.info("Going to show user: " + name);
    }


}
