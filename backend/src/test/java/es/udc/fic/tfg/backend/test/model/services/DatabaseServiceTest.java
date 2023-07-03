package es.udc.fic.tfg.backend.test.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NoDatabaseConnectionException;
import es.udc.fic.tfg.backend.model.exceptions.PermissionException;
import es.udc.fic.tfg.backend.model.services.CatalogService;
import es.udc.fic.tfg.backend.model.services.DatabaseService;
import es.udc.fic.tfg.backend.model.services.UserService;
import es.udc.fic.tfg.backend.model.util.PasswordCipher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DatabaseServiceTest {

    @Value("${project.test.database}")
    private String DATABASE;

    @Value("${project.test.host}")
    private String HOST;

    @Value("${project.test.port}")
    private int PORT;

    @Value("${project.test.user}")
    private String USER;

    @Value("${project.test.password}")
    private String PASSWORD;

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);
    private final String USERNAME = "gayoso";

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogService catalogService;

    private User createUser(String userName) throws DuplicateInstanceException {
        User user = new User(userName, "password",
                userName + "@" + userName + ".com");
        userService.signUp(user);
        return user;
    }

    @Test
    public void testAddDatabase() throws InstanceNotFoundException, DuplicateInstanceException, NoDatabaseConnectionException {

        User user = createUser("username");

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Database foundDatabase = catalogService.findDatabaseById(database.getId());

        assertEquals(database, foundDatabase);

    }

    @Test
    public void testAddDatabaseParametersCorrect() throws InstanceNotFoundException, DuplicateInstanceException, NoDatabaseConnectionException {

        User user = createUser("username");

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        assertEquals(database.getDatabase(), DATABASE);
        assertEquals(database.getType(), types.get(1));
        assertEquals(database.getHost(), HOST);
        assertEquals(database.getPort(), PORT);
        assertEquals(database.getUserDb(), USER);
        assertEquals(PasswordCipher.decode(database.getPassword()), PASSWORD);
        assertEquals(database.getUser(), user);
    }

    @Test
    public void testAddDatabaseWithNonExistentUser(){

        List<Type> types = catalogService.findAllDatabaseTypes();

        assertThrows(InstanceNotFoundException.class, () -> databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, NON_EXISTENT_ID));
    }

    @Test
    public void testAddDatabaseWithNoExistentType() throws DuplicateInstanceException {

        User user = createUser(USERNAME);

        assertThrows(InstanceNotFoundException.class, () -> databaseService.addDatabase(DATABASE, NON_EXISTENT_ID, HOST, PORT, USER, PASSWORD, user.getId()));
    }

    @Test
    public void testAddDatabaseWithNoConnection() throws DuplicateInstanceException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        assertThrows(NoDatabaseConnectionException.class, () -> databaseService.addDatabase(DATABASE, types.get(0).getId(), HOST, PORT, USER, PASSWORD, user.getId()));
    }

    @Test
    public void testDeleteDatabase() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException, NoDatabaseConnectionException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());
        Long dbId = database.getId();

        databaseService.deleteDatabase(user.getId(), database.getId());

        assertThrows(InstanceNotFoundException.class, () -> catalogService.findDatabaseById(dbId));
    }

    @Test
    public void testDeleteNonExistentDatabase() throws DuplicateInstanceException{

        User user = createUser(USERNAME);

        assertThrows(InstanceNotFoundException.class, () -> databaseService.deleteDatabase(user.getId(), NON_EXISTENT_ID));
    }

    @Test
    public void testDeleteWithNonExistentUser() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        assertThrows(InstanceNotFoundException.class, () -> databaseService.deleteDatabase(NON_EXISTENT_ID, database.getId()));
    }

    @Test
    public void testDeleteWithNoPermission() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user1 = createUser(USERNAME);
        User user2 = createUser("pepito");

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user1.getId());

        assertThrows(PermissionException.class, () -> databaseService.deleteDatabase(user2.getId(), database.getId()));

    }

    @Test
    public void testUpdateDatabase() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException,
            NoDatabaseConnectionException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        database = databaseService.updateDatabase(database.getId(), user.getId(), DATABASE, types.get(1).getId(), HOST,
                PORT, "postgres", "postgres");

        assertEquals(database.getDatabase(), DATABASE);
        assertEquals(database.getType(), types.get(1));
        assertEquals(database.getHost(), HOST);
        assertEquals(database.getPort(), PORT);
        assertEquals(database.getUserDb(), "postgres");
        assertEquals(PasswordCipher.decode(database.getPassword()), "postgres");
    }

    @Test
    public void testUpdateNonExistentDatabase() throws DuplicateInstanceException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        assertThrows(InstanceNotFoundException.class, () -> databaseService.updateDatabase(NON_EXISTENT_ID, user.getId(), DATABASE,
                types.get(1).getId(), HOST, PORT, "postgres", "pass"));
    }

    @Test
    public void testUpdateWithNonExistentUser() throws DuplicateInstanceException, InstanceNotFoundException,
            NoDatabaseConnectionException {

        User user = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        assertThrows(InstanceNotFoundException.class, () -> databaseService.updateDatabase(database.getId(), NON_EXISTENT_ID,
                DATABASE, types.get(1).getId(), HOST, PORT, "postgres", "pass"));
    }

    @Test
    public void testUpdateWithNoPermission() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user1 = createUser(USERNAME);
        User user2 = createUser("pepito");

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user1.getId());

        assertThrows(PermissionException.class, () -> databaseService.updateDatabase(database.getId(), user2.getId(), DATABASE,
                types.get(1).getId(), HOST, PORT, "postgres", "pass"));
    }

    @Test
    public void testUpdateWithNoExistentType() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user1 = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user1.getId());

        assertThrows(InstanceNotFoundException.class, () -> databaseService.updateDatabase(database.getId(), user1.getId(), DATABASE,
                NON_EXISTENT_ID, HOST, PORT, "david", "password"));
    }

    @Test
    public void testUpdateWithNoConnection() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user1 = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user1.getId());

        assertThrows(NoDatabaseConnectionException.class, () -> databaseService.updateDatabase(database.getId(), user1.getId(), DATABASE,
                types.get(1).getId(), HOST, PORT, "postgres", "password"));
    }
}
