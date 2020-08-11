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
    public ModelAndView saveUser(@ModelAttribute("user") @Valid User user,  BindingResult result,
                                 @RequestParam(value = "userRole", required = false) String userRole,
                                 @RequestParam(value = "adminRole",  required = false) String adminRole) {
        ModelAndView modelAndView = new ModelAndView();
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            modelAndView.setViewName("/registration");
            return  modelAndView;
        }
        setUserRoles(user, userRole, adminRole);
        userService.addUser(user);
        modelAndView.setViewName("/user");
        return modelAndView;
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
    public ModelAndView userForm(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();//(ModelAndView modelAndView, Principal principal) {
//        String email = principal.getName();
        String email = authentication.getName();
        System.out.println("email = "+ email);
        User user = userService.getUserByName(email);
        String titleRole = "USER";
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("ROLE_ADMIN")) {
                titleRole = "ADMIN";
                break;
            }
        }
        System.out.println(" titleRole = "+  titleRole);
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
        modelAndView.addObject("user", user);
        modelAndView.addObject("titleRole", titleRole);
        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin-page");
        return modelAndView;
    }


    @GetMapping("/admin/add")
    public ModelAndView addGet() {
        ModelAndView model = new ModelAndView();
        model.setViewName("add-new-user");
        model.addObject("user", new User());
        model.addObject("roles", userService.getRoles());
        return model;
    }


    @PostMapping( {"/admin/add"})
    public ModelAndView addPost(@ModelAttribute("user") @Valid User user,  BindingResult result,
                                 @RequestParam(value = "userRole", required = false) String userRole,
                                 @RequestParam(value = "adminRole",  required = false) String adminRole) {

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add-new-user");
            return  modelAndView;
        }
        setUserRoles(user, userRole, adminRole);
        userService.addUser(user);
        return new ModelAndView("redirect:/admin");
    }
//    public ModelAndView addPost(@ModelAttribute("user") User user, @RequestParam Long roleId ) {
//        Set<Role> roleSet = Collections.singleton(userService.getRoleById(roleId));
//        user.setRoles(roleSet);
//        userService.addUser(user);
//        return new ModelAndView("redirect:/admin");
//    }

    @GetMapping("/admin/update")
    public ModelAndView updateGet(@RequestParam Long id, ModelAndView model) {
        model.setViewName("edit-user");
        User user = userService.getUserById(id);
        model.addObject("user", user);
        List<Role> roles = userService.getRoles();
        model.addObject("roles", roles);
        return model;
    }

    @PostMapping("/admin/update")
    public ModelAndView updatePost(@ModelAttribute("admin/user") User user,
                                   @RequestParam(value = "userRole", required = false) String userRole,
                                   @RequestParam(value = "adminRole",  required = false) String adminRole){

        setUserRoles(user, userRole, adminRole);
        userService.updateUser(user);
        return new ModelAndView("redirect:/admin");
    }

    private void setUserRoles(@ModelAttribute("admin/user") User user, @RequestParam(value = "userRole", required = false) String userRole, @RequestParam(value = "adminRole", required = false) String adminRole) {
        Set<Role> roles = new HashSet<>();
        if (userRole != null) {
            roles.add(userService.getRoleById(2L));
        }
        if (adminRole != null) {
            roles.add(userService.getRoleById(1L));
        }
        user.setRoles(roles);
    }


    @GetMapping("/admin/delete{id}")
    public ModelAndView deleteGet(@RequestParam("id") Long id, ModelAndView model) {
        model.setViewName("delete-user");
        User user = userService.getUserById(id);
        model.addObject("user", user);
        List<Role> roles = userService.getRoles();
        model.addObject("roles", roles);
        return model;
    }


    @PostMapping("/admin/delete{id}")
    public ModelAndView deletePost(@RequestParam("id") Long id, ModelAndView model) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
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