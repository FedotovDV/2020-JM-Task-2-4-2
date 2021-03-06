package web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import web.model.User;
import web.service.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        System.out.println("validate User " +user.getEmail());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required");
        if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
            System.out.println("Size.userForm.username " + user.getUsername());
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (userService.getUserByName(user.getUsername()) != null) {
            System.out.println("Duplicate.userForm.username "+ user.getUsername());
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required");
        if (user.getPassword().length() < 3 || user.getPassword().length() > 32) {
            System.out.println("Size.userForm.password " + user.getPassword());
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmPassword().equals(user.getPassword())) {
            System.out.println("Different.userForm.password " + user.getConfirmPassword());
            errors.rejectValue("confirmPassword", "Different.userForm.password");
        }
    }
}