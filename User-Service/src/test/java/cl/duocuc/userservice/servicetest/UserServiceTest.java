package cl.duocuc.userservice.servicetest;

import cl.duocuc.userservice.model.User;
import cl.duocuc.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testAddUsers() {
        assertNull(userService.findById("50"));
        User newUser = new User("50", "test", "test", "test", "test", true, "test");
        userService.addUser(newUser);
        assertNotNull(userService.findById("50"));
    }

    @Test
    void testFindAll() {
        List<User> users = userService.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    void testFindById() {
        User user = userService.findById("1");
        assertNotNull(user);
        assertEquals("1", user.getId());
    }

    @Test
    void testRemoveUser() {
        boolean deleted = userService.removeUser("1");
        assertTrue(deleted);
        User user = userService.findById("1");
        assertNull(user);
    }

    @Test
    void testUpdateUser() {
        User user = userService.findById("1");
        assertNotNull(user);
        User modifiedUser = new User("1", "Benjamín Andrés Aguilar Ledezma", "20428923-9", "modified@mail.com", "clave001", true, "CLIENTE");
        userService.updateUser("1", modifiedUser);
        User newUser = userService.findById("1");
        assertEquals("modified@mail.com", newUser.getEmail());
        assertEquals("CLIENTE", newUser.getRol());
    }

    @Test
    void testDesactivateUser() {
        User user = userService.findById("1");
        assertTrue(user.isActive());
        userService.desactivateUser("1");
        User modifiedUser = userService.findById("1");
        assertFalse(modifiedUser.isActive());
    }

    @Test
    void testActivateUser() {
        User user = userService.findById("30");
        assertFalse(user.isActive());
        userService.activateUser("50");
        User modifiedUser = userService.findById("1");
        assertTrue(modifiedUser.isActive());
    }
}
