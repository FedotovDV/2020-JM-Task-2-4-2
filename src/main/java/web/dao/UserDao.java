package web.dao;

import web.model.Role;
import web.model.User;

import java.util.List;

public interface UserDao {
    User getUserByName(String username);

    List<User> getUsers();

    void addUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User getUserById(Long id);

    List<Role> getRoles();

    Role getRoleById(Long id);
}