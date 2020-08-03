package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import java.util.Collections;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private final Map<String, User> userMap = Collections.singletonMap("test",
            new User(1L, "test", "test", Collections.singleton(new Role(1L, "ROLE_USER")))); // name - уникальное значение, выступает в качестве ключа Map

    @Override
    public User getUserByName(String name) {
        if (!userMap.containsKey(name)) {
            return null;
        }

        return userMap.get(name);
    }
}