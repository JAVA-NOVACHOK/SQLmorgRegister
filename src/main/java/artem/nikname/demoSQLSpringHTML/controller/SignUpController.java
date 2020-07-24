package artem.nikname.demoSQLSpringHTML.controller;

import artem.nikname.demoSQLSpringHTML.model.User;
import artem.nikname.demoSQLSpringHTML.repository.UsersRepository;
import artem.nikname.demoSQLSpringHTML.security.SecurityCheck;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ControllerAdvice
public class SignUpController {

    private final SecurityCheck securityCheck;
    private final UsersRepository usersRepository;

    @Autowired
    public SignUpController(SecurityCheck securityCheck,
            UsersRepository usersRepository) {
        this.securityCheck = securityCheck;
        this.usersRepository = usersRepository;
    }

    
    
    @GetMapping("showLoginForm")
    public String startSignUp(Model model) {
        System.out.println("SIGNup GET!");
        model.addAttribute("massege", null);
        System.out.println("SIGNup GET out!");
        return "loginForm";
    }

    @PostMapping
    public String processSignUp(Model model,@RequestParam String login, String psw) {
        model.addAttribute("massege", "1");
        System.out.println("In Post signup");
        User user = null;
        if (securityCheck.checkForInjection(login, psw)) {
            user = usersRepository.getUserByLoginAndPassword(login, psw);
            System.out.println("User from POST signup: " + user + " login:" + login + " psw:" + psw);
        }
        if (user == null) {
            return "loginForm";
        }
        model.addAttribute("currentDate", PatientController.getCurrentDate());
        model.addAttribute("id", user.getId());
        System.out.println("Successful signup!");
        System.out.println(user);
        return "patient/add";
    }
}
