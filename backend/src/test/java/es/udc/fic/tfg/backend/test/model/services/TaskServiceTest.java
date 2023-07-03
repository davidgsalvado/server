package es.udc.fic.tfg.backend.test.model.services;

import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.*;
import es.udc.fic.tfg.backend.model.services.TaskService;
import es.udc.fic.tfg.backend.model.services.UserService;
import es.udc.fic.tfg.backend.model.services.CatalogService;
import es.udc.fic.tfg.backend.model.services.DatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceTest {

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

    @Value("${project.test.taskString}")
    private String TASK_STRING;

    @Value("${project.test.taskCancelString}")
    private String TASK_CANCEL_STRING;

    private final String GAYOSO_USERNAME = "gayoso";

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private DatabaseService databaseService;

    private User createUser(String userName) throws DuplicateInstanceException {
        User user = new User(userName, "password",
                userName + "@" + userName + ".com");
        userService.signUp(user);
        return user;
    }

    @Test
    public void testCreateTask() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException {

        User user = createUser(GAYOSO_USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_STRING);

        Task foundTask = catalogService.findTaskById(task.getId());

        assertEquals(task, foundTask);
    }

    @Test
    public void testCreateTaskNoExistentUser(){

        assertThrows(InstanceNotFoundException.class, () -> taskService.createTask(NON_EXISTENT_ID, "Name",
                1L, TASK_STRING));
    }

    @Test
    public void testCreateTaskNoExistentDatabase() throws DuplicateInstanceException {

        User user = createUser(GAYOSO_USERNAME);

        assertThrows(InstanceNotFoundException.class, () -> taskService.createTask(user.getId(), "Name",
                NON_EXISTENT_ID, TASK_STRING));
    }

    @Test
    public void testCancelTask() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException,
            CannotCancelException {

        User user = createUser(GAYOSO_USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_CANCEL_STRING);

        taskService.cancelTask(user.getId(), task.getId());

        Task foundTask = catalogService.findTaskById(task.getId());
        assertEquals(foundTask.getState(), "CANCELED");
        assertNotNull(foundTask.getEndDate());
    }

    @Test
    public void testCancelTaskWithNoExistentUsers(){

        assertThrows(InstanceNotFoundException.class, () -> taskService.cancelTask(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testCancelNonExistentTask() throws DuplicateInstanceException {

        User user = createUser(GAYOSO_USERNAME);

        assertThrows(InstanceNotFoundException.class, () -> taskService.cancelTask(user.getId(), NON_EXISTENT_ID));
    }

    @Test
    public void testCannotCancel() throws DuplicateInstanceException, InstanceNotFoundException, CannotCancelException,
            NoDatabaseConnectionException {

        User user = createUser(GAYOSO_USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_CANCEL_STRING);

        taskService.cancelTask(user.getId(), task.getId());

        assertThrows(CannotCancelException.class, () -> taskService.cancelTask(user.getId(), task.getId()));
    }

    @Test
    public void testDeleteTask() throws DuplicateInstanceException, InstanceNotFoundException, NoDatabaseConnectionException,
            CannotDeleteException, CannotCancelException {

        User user = createUser(GAYOSO_USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_CANCEL_STRING);
        Task foundTask = catalogService.findTaskById(task.getId());
        taskService.cancelTask(user.getId(), task.getId());

        taskService.deleteTask(user.getId(), foundTask.getId());
        assertThrows(InstanceNotFoundException.class, () -> catalogService.findTaskById(foundTask.getId()));
    }

    @Test
    public void testDeleteTaskNoExistentUser() {
        assertThrows(InstanceNotFoundException.class, () -> taskService.deleteTask(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testDeleteNoExistentTask() throws DuplicateInstanceException {

        User user = createUser(GAYOSO_USERNAME);

        assertThrows(InstanceNotFoundException.class, () -> taskService.deleteTask(user.getId(), NON_EXISTENT_ID));
    }

    @Test
    public void testCannotDeleteTask() throws DuplicateInstanceException, InstanceNotFoundException,
            NoDatabaseConnectionException {

        User user = createUser(GAYOSO_USERNAME);
        List<Type> types = catalogService.findAllDatabaseTypes();

        Database database = databaseService.addDatabase(DATABASE, types.get(1).getId(), HOST, PORT, USER, PASSWORD, user.getId());

        Task task = taskService.createTask(user.getId(), "Task 1", database.getId(), TASK_CANCEL_STRING);

        assertThrows(CannotDeleteException.class, () -> taskService.deleteTask(user.getId(), task.getId()));
    }

}
