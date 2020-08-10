package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    void addUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User getUserById(Long id);

    User getUserByName(String username);

    List<Role> getRoles();

    Role getRoleById(Long id);
}
