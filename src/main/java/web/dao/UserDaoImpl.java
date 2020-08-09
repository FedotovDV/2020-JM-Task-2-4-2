package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import web.model.User;
import web.service.UserDetailsServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {


    @PersistenceContext
    private EntityManager entityManager;


//
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByName(String email) {
        TypedQuery<User> q = entityManager.createQuery(
                "select u from User u where u.email = :email",
                User.class
        );
        q.setParameter("email", email);
        return q.getResultList().stream().findAny().orElse(null);
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

//    @Override
//    public User getUserByEmail(String email) {
//        return userDetailsService.loadUserByUsername(email);
//    }
}