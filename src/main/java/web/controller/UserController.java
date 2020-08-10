package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;
import web.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

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
        model.addAttribute("roles", userService.getRoles());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute @Valid User user, @RequestParam Long roleId, BindingResult result) {

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "/registration";
        }
        Set<Role> roleSet = Collections.singleton(userService.getRoleById(roleId));
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/user";
    }


//    @GetMapping(value = "/user/{email:.+}")
//    public String userForm(@PathVariable("email") String email, ModelMap model) {
//        System.out.println("in controller " + email);//при передаче email пропадает точка и далее -> (value = "/user/{email:.+}")
//
//        User user = userService.getUserByName(email);
//        String titleRole = "USER";
//        for (Role role : user.getRoles()) {
//            if (role.equals("ROLE_ADMIN")) {
//                titleRole = "ADMIN";
//            }
//        }
//        model.addAttribute("titleRole", titleRole);
//        model.addAttribute("user", user);
//        return "user";
//    }

    @GetMapping(value = "/user")
    public ModelAndView userForm(ModelAndView modelAndView, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByName(email);
        String titleRole = "USER";
        for (Role role : user.getRoles()) {
            if (role.equals("ROLE_ADMIN")) {
                titleRole = "ADMIN";
            }
        }
        modelAndView.addObject("titleRole", titleRole);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user");
        return modelAndView;
    }


    @GetMapping("/admin")
    public ModelAndView admin(ModelAndView modelAndView, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByName(email);
        String titleRole = "ADMIN";
        List<User> users = userService.getUsers();
        modelAndView.addObject("titleRole", titleRole);
        modelAndView.addObject("title", "Users");
        modelAndView.addObject("users", users);
        modelAndView.setViewName("user");
        return modelAndView;
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