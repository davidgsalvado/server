package es.udc.fic.tfg.backend.test.model.services;

import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.Log;
import es.udc.fic.tfg.backend.model.exceptions.CannotCancelException;
import es.udc.fic.tfg.backend.model.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NoDatabaseConnectionException;
import es.udc.fic.tfg.backend.model.services.UserService;
import es.udc.fic.tfg.backend.model.services.CatalogService;
import es.udc.fic.tfg.backend.model.services.TaskService;
import es.udc.fic.tfg.backend.model.services.DatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CatalogServiceTest {

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

    private final String USERNAME = "gayoso";

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private TaskService taskService;

    private User createUser(String userName) throws DuplicateInstanceException {
        User user = new User(userName, "password",
                userName + "@" + userName + ".com");
        userService.signUp(user);
        return user;
    }

    private Database createDatabase(User user, Type type) throws InstanceNotFoundException, NoDatabaseConnectionException {
        return databaseService.addDatabase(DATABASE, type.getId(), HOST, PORT, USER, PASSWORD, user.getId());
    }

    private Database createDatabaseTask(User user, Type type) throws InstanceNotFoundException, NoDatabaseConnectionException {
        return databaseService.addDatabase(DATABASE, type.getId(), HOST, PORT, USER, PASSWORD, user.getId());
    }

    private Task createTask(User user, Database database, String taskName) throws InstanceNotFoundException {
        return taskService.createTask(user.getId(), taskName, database.getId(), TASK_STRING);
    }

    @Test
    public void testFindAllUserDatabases() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {
        User user1 = createUser("pepito");
        User user2 = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database1 = createDatabase(user1, types.get(1));
        Database database2 = createDatabase(user1, types.get(1));
        Database database3 = createDatabase(user2, types.get(1));

        List<Database> user1Databases = catalogService.findAllUserDatabases(user1.getId());
        assertEquals(2, user1Databases.size());
        assertEquals(user1Databases.get(0), database1);

        List<Database> user2Databases = catalogService.findAllUserDatabases(user2.getId());
        assertEquals(1, user2Databases.size());
    }

    @Test
    public void testFindAllUserTasks() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user1 = createUser("pepito");
        User user2 = createUser(USERNAME);

        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database1 = createDatabaseTask(user1, types.get(1));

        Task task = createTask(user1, database1, "Task1_Pepito");

        List<Task> taskList = catalogService.findAllUserTasks(user1.getId());
        assertEquals(1, taskList.size());
        assertEquals(taskList.get(0), task);

        Task task2 = createTask(user2, database1, "Task1_Gayoso");
        Task task3 = createTask(user2, database1, "Task2_Gayoso");

        taskList = catalogService.findAllUserTasks(user2.getId());
        assertEquals(taskList.size(), 2);
    }

    @Test
    public void testFindTaskById() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user = createUser(USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database1 = createDatabaseTask(user, types.get(1));

        Task task = createTask(user, database1, "Task1");

        Task foundTask = catalogService.findTaskById(task.getId());

        assertEquals(foundTask, task);
    }

    @Test
    public void testFindDatabaseById() throws DuplicateInstanceException, InstanceNotFoundException,
            NoDatabaseConnectionException {

        User user = createUser(USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = createDatabaseTask(user, types.get(1));

        Database foundDatabase = catalogService.findDatabaseById(database.getId());

        assertEquals(database, foundDatabase);
    }

    @Test
    public void testFindAllDatabaseTypes() {

        List<Type> types = catalogService.findAllDatabaseTypes();

        assertEquals(types.size(), 2);
        assertEquals(types.get(0).getTypeName(), "MySQL");
        assertEquals(types.get(1).getTypeName(), "PostgreSQL");
    }

    @Test
    public void testFindAllLogsByTaskId() throws DuplicateInstanceException, InstanceNotFoundException,
            NoDatabaseConnectionException, InterruptedException, CannotCancelException {

        User user = createUser(USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database1 = createDatabaseTask(user, types.get(1));

        Task task = taskService.createTask(user.getId(), "Task1", database1.getId(), TASK_STRING);

        taskService.cancelTask(user.getId(), task.getId());
        assertTrue(catalogService.findAllLogsByTaskId(task.getId()).size() > 0);
    }

    @Test
    public void testFindAllLogsByTaskIdAfterLogId() throws DuplicateInstanceException, InstanceNotFoundException,
            NoDatabaseConnectionException, CannotCancelException {

        User user = createUser(USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database1 = createDatabaseTask(user, types.get(1));

        Task task = taskService.createTask(user.getId(), "Task1", database1.getId(), TASK_STRING);

        taskService.cancelTask(user.getId(), task.getId());
        List<Log> logList = catalogService.findAllLogsByTaskId(task.getId());
        Log firstLog = logList.get(0);
        System.out.println(firstLog.getLog());
        assertTrue(firstLog.getId() <
                catalogService.findAllLogsByTaskIdAfterLogId(task.getId(), firstLog.getId())
                        .get(0).getId());
    }

}
