package es.udc.fic.tfg.backend.test.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.*;
import es.udc.fic.tfg.backend.model.services.DatabaseService;
import es.udc.fic.tfg.backend.model.services.CatalogService;
import es.udc.fic.tfg.backend.model.services.TaskService;
import es.udc.fic.tfg.backend.model.services.GeoJsonService;
import es.udc.fic.tfg.backend.model.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GeoJsonServiceTest {

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

    @Value("${project.test.taskString}")
    private String TASK_STRING;

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    @Autowired
    private TaskService taskService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private GeoJsonService geoJsonService;

    private User createUser(String userName) throws DuplicateInstanceException {
        User user = new User(userName, "password",
                userName + "@" + userName + ".com");
        userService.signUp(user);
        return user;
    }

    private Database createDatabase(User user, Type type) throws InstanceNotFoundException, NoDatabaseConnectionException {
        return databaseService.addDatabase(DATABASE, type.getId(), HOST, PORT, USER, PASSWORD, user.getId());
    }

    @Test
    public void testGetGeoJSONNoExistentDatabase() {
        assertThrows(InstanceNotFoundException.class, () -> geoJsonService.getGeoJson(NON_EXISTENT_ID, "tableName"));
    }

    @Test
    public void testGetGeoJSONNoExistentEntity() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {
        User user = createUser("gayoso");
        List<Type> types = catalogService.findAllDatabaseTypes();
        Database database = createDatabase(user, types.get(1));
        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_STRING);

        assertThrows(NotFoundEntityUsedByDatabaseException.class, () -> geoJsonService.getGeoJson(database.getId(), "tableName"));
    }
}
