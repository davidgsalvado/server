/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.util;

import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.TaskDao;
import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.LogDao;
import es.udc.fic.tfg.backend.model.entities.TypeDao;
import es.udc.fic.tfg.backend.model.entities.Log;
import es.udc.fic.tfg.backend.model.entities.EntityTableDao;
import es.udc.fic.tfg.backend.model.entities.EntityTable;
import es.udc.fic.tfg.backend.model.entities.EntityTableIdentity;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.udc.fic.tfg.osmparser.backend.model.util.ServerCallback;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
@Component
public class ServerCallbackImpl implements ServerCallback {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private EntityTableDao entityTableDao;

    @Override
    public void parseStart(Long taskId) {

        Optional<Task> task = taskDao.findById(taskId);

        Task finalTask = task.get();

        finalTask.setState(Task.State.RUNNING.toString());
        taskDao.save(finalTask);
    }

    @Override
    public void parseFinishWithError(Long taskId, String error) {

        Optional<Task> task = taskDao.findById(taskId);

        Task finalTask = task.get();

        finalTask.setState(Task.State.ERROR.toString());
        finalTask.setError(error);
        finalTask.setEndDate(LocalDateTime.now().withNano(0));
        taskDao.save(finalTask);

    }

    @Override
    public void parseFinishedOk(Long taskId) {

        Optional<Task> task = taskDao.findById(taskId);

        Task finalTask = task.get();

        finalTask.setState(Task.State.OK.toString());
        finalTask.setEndDate(LocalDateTime.now().withNano(0));
        taskDao.save(finalTask);

    }

    @Override
    public String getTaskState(Long taskId){

        Optional<Task> task = taskDao.findById(taskId);

        Task finalTask = task.get();

        return finalTask.getState();

    }

    @Override
    public void sendLogs(Long taskId, String log, String type) {

        LocalDateTime now = LocalDateTime.now().withNano(0);

        String time = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Optional<Task> task = taskDao.findById(taskId);

        Task finalTask = task.get();

        Log newLog = new Log(time.replace("T", " ") + ": " + log, Log.LogType.valueOf(type).toString(), finalTask);

        logDao.save(newLog);
    }

    @Override
    public void checkAndAddDatabase(Long taskId, String database, int port, String host, String userDb, String password, String typename){

        Task task = taskDao.findById(taskId).get();

        Type type = typeDao.getTypeByTypeName(typename);

        String encodedPassword = PasswordCipher.encode(password);

        int count = databaseDao.countByDatabaseAndPortAndHostAndUserDbAndPasswordAndType(database, port, host, userDb,
                encodedPassword, type);

        if (count == 0){
            Database newDatabase = new Database(task.getUser(), database, type, host, port, userDb, encodedPassword);
            databaseDao.save(newDatabase);
        }
    }

    @Override
    public void addNewEntityTable(Long taskId, String database, int port, String host, String userDb, String password, String typename,
                                  String entityName) {

        Task task = taskDao.findById(taskId).get();

        Type type = typeDao.getTypeByTypeName(typename);

        Database foundDatabase = databaseDao.findByDatabaseAndPortAndHostAndUserDbAndPasswordAndTypeAndUserId(database, port, host, userDb,
                PasswordCipher.encode(password), type, task.getUser().getId());

        if (foundDatabase == null){
            EntityTable entityTable = new EntityTable(new EntityTableIdentity(foundDatabase, entityName));
            entityTableDao.save(entityTable);
        }else{
            Optional<EntityTable> optionalEntityTable = entityTableDao.findById(new EntityTableIdentity(foundDatabase, entityName));

            if (!optionalEntityTable.isPresent()){
                EntityTable entityTable = new EntityTable(new EntityTableIdentity(foundDatabase, entityName));
                entityTableDao.save(entityTable);
            }
        }

    }
}
