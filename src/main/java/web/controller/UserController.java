package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import web.util.UserValidator;

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

    @GetMapping("/user")
    public String carForm( ModelMap model) {

               List<User> users = userService.getUsers();
        model.addAttribute("title", "Users");
        model.addAttribute("users", users);
        return "user";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("user") User user) {

        userService.addUser(user);

        return "redirect:/user";
    }
//    @PostMapping("/save")
//    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
//        userValidator.validate(user, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }
//
//        userService.addUser(user);
//
//        securityService.autoLogin(user.getUsername(), user.getConfirmPassword());
//
//        return "redirect:/user";
//    }

    @GetMapping("/login")
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

    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin";
    }
}