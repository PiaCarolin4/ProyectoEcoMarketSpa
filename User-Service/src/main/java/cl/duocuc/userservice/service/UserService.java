package cl.duocuc.userservice.service;

import cl.duocuc.userservice.model.User;
import cl.duocuc.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository UserRepository;

    public UserService(UserRepository userRepository) {
        this.UserRepository = userRepository;
    }

    public List<User> findAll() {
        return UserRepository.findAll();
    }

    public User findById(String id) {
        return UserRepository.findById(id).orElse(null);
    }

    public boolean removeUser(String id) {
        if (UserRepository.existsById(id)) {
            UserRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateUser(String id, User user) {
        if (UserRepository.existsById(id)) {
            UserRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean desactivateUser(String id) {
        User user = findById(id);
        if (user != null) {
            user.setActive(false);
            UserRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean addUser(User user) {
        if (UserRepository.existsById(user.getId())) {
            return false;
        }
        UserRepository.save(user);
        return true;
    }

    public void activateUser(String id) {
        User user = findById(id);
        if (user != null) {
            user.setActive(true);
            UserRepository.save(user);
        }
    }
}
