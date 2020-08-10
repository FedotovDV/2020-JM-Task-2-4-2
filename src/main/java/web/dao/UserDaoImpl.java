package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {


    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByName(String email) {
        System.out.println("getUserByName = "+ email);
        return entityManager.createQuery(
                "select U from User U where U.email = :email", User.class)
                .setParameter("email", email).getResultList().stream().findFirst().orElse(null);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {

        return entityManager.createQuery("select U from User U").getResultList();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public void deleteUser(Long id) {
        System.out.println("deleteUser ="+ id);
        entityManager.remove(getUserById(id));
    }

    @Override
    public void updateUser(User user) {

        entityManager.merge(user);
    }

    @Override
    public User getUserById(Long id) {

        return entityManager.find(User.class, id);
    }

    @Override
    public List getRoles() {
        return entityManager.createQuery("select R from Role R").getResultList();
    }

    @Override
    public Role getRoleById(Long id) {
        System.out.println("getRoleById = "+ id);
        return entityManager.find(Role.class, id);
    }

}