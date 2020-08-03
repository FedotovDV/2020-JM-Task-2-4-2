package web.dao;

import web.model.User;

public interface UserDao {
    User getUserByName(String name);
}