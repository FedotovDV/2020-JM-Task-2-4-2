package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import web.util.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;


    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute @Valid User user, BindingResult result) {
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            System.out.println("result.hasErrors()" + result.toString());
            return "/registration";
        }
        System.out.println("addUser " + user.getEmail());
        userService.addUser(user);
        return "redirect:/user";
    }

    //    @GetMapping("/user?email={email}")
    @GetMapping(value = "/user/{email:.+}")
    public String userForm(@PathVariable("email") String email, ModelMap model) {
        System.out.println("in controller " + email);//при передаче email пропадает точка и далее -> (value = "/user/{email:.+}")

        User user = userService.getUserByName(email);

        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(ModelMap model) {

        List<User> users = userService.getUsers();
        model.addAttribute("title", "Users");
        model.addAttribute("users", users);
        return "admin-page";
    }


    @GetMapping(value = "/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }
        return "login";
    }


    @GetMapping(value = {"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }


}