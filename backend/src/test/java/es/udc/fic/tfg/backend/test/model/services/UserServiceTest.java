package es.udc.fic.tfg.backend.test.model.services;

import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.backend.model.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    @Autowired
    private UserService userService;

    private User createUser(String userName) {
        return new User(userName, "password", userName + "@" + userName + ".com");
    }

    @Test
    public void testSignUpAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {

        User user = createUser("user");

        userService.signUp(user);

        User loggedInUser = userService.loginFromId(user.getId());

        assertEquals(user, loggedInUser);
        assertEquals(User.RoleType.USER, user.getRole());

    }

    @Test
    public void testSignUpDuplicatedUserName() throws DuplicateInstanceException {

        User user = createUser("user");

        userService.signUp(user);
        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(user));

    }

    @Test
    public void testLoginFromNonExistentId(){
        assertThrows(InstanceNotFoundException.class, () -> userService.loginFromId(NON_EXISTENT_ID));
    }

    @Test
    public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {

        User user = createUser("userName");
        String clearPassword = user.getPassword();

        userService.signUp(user);

        User loggedUser = userService.login(user.getUserName(), clearPassword);

        assertEquals(user, loggedUser);

    }

    @Test
    public void testLoginNonExistentUser(){
        assertThrows(IncorrectLoginException.class, () -> userService.login("userName", "password"));
    }

    @Test
    public void testLoginWithIncorrectPassword() throws DuplicateInstanceException {

        User user = createUser("userName");

        userService.signUp(user);

        assertThrows(IncorrectLoginException.class, () -> userService.login(user.getUserName(), "wrongPassword"));

    }
}

