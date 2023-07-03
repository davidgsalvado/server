/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.LogDao;
import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.TaskDao;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.CannotCancelException;
import es.udc.fic.tfg.backend.model.exceptions.CannotDeleteException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.util.PasswordCipher;
import es.udc.fic.tfg.backend.model.util.ServerCallbackImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.udc.fic.tfg.osmparser.backend.model.util.Parser;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ServerCallbackImpl serverCallback;

    @Autowired
    private LogDao logDao;

    private final static Parser parser = new Parser();

    @Override
    public Task createTask(Long userId, String taskName, Long databaseId, String taskString) throws InstanceNotFoundException {

        User user = permissionChecker.checkUser(userId); //check if user exists

        Task task = new Task(user, taskName, Task.State.WAITING, taskString, LocalDateTime.now().withNano(0));

        if(databaseId != null){
            Optional<Database> database = databaseDao.findById(databaseId);

            if(!database.isPresent())
                throw new InstanceNotFoundException("project.entities.database", databaseId);

            Database finalDatabase = database.get();
            task.setDatabase(finalDatabase);

            String conn = "CONNECT TO DBASE=" + finalDatabase.getDatabase() + " OF TYPEDB=" + finalDatabase.getType().getTypeName() +
                    " FROM PORTDB=" + finalDatabase.getPort() + " OF HOSTDB=" + finalDatabase.getHost() + " WITH USERDB=" + finalDatabase.getUserDb() +
                    " AND PASSWORDDB=" + PasswordCipher.decode(finalDatabase.getPassword()) + "\n\n";

            task.setTaskString(conn + task.getTaskString());
        }

        taskDao.save(task);

        try{
            parser.parseQuery(task.getId(), task.getTaskString(),  serverCallback);
        }catch (Exception e){
            e.printStackTrace();
        }

        return task;

    }

    @Override
    public void cancelTask(Long userId, Long taskId) throws InstanceNotFoundException, CannotCancelException {

        permissionChecker.checkUser(userId); //check if user exists

        Optional<Task> task = taskDao.findById(taskId);

        if(!task.isPresent())
            throw new InstanceNotFoundException("project.entities.task", taskId);

        Task finalTask = task.get();
        if(!(Objects.equals(finalTask.getState(), "WAITING") || Objects.equals(finalTask.getState(), "RUNNING")))
            throw new CannotCancelException("You cannot cancel a task which is not in a waiting or running state");

        parser.cancelTask(taskId, serverCallback); //wait here until the task is successfully canceled

        System.out.println("TASK WITH ID " + taskId + " CANCELED SUCCESSFULLY!");

        finalTask.setState(Task.State.CANCELED.toString());
        finalTask.setEndDate(LocalDateTime.now().withNano(0));
        taskDao.save(finalTask);

    }

    @Override
    public void deleteTask(Long userId, Long taskId) throws InstanceNotFoundException, CannotDeleteException {

        permissionChecker.checkUser(userId);

        Optional<Task> task = taskDao.findById(taskId);

        if(!task.isPresent())
            throw new InstanceNotFoundException("project.entities.task", taskId);

        Task finalTask = task.get();
        if((Objects.equals(finalTask.getState(), "WAITING") || Objects.equals(finalTask.getState(), "RUNNING")))
            throw new CannotDeleteException("You cannot delete a task which is in a waiting or running state");

        logDao.deleteByTaskId(finalTask.getId()); //delete logs associated with the task
        taskDao.delete(finalTask);
    }
}
