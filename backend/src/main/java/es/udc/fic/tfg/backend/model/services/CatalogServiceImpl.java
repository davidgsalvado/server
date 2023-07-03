/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.TypeDao;
import es.udc.fic.tfg.backend.model.entities.LogDao;
import es.udc.fic.tfg.backend.model.entities.Log;
import es.udc.fic.tfg.backend.model.entities.TaskDao;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.util.PasswordCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private LogDao logDao;

    @Override
    public List<Type> findAllDatabaseTypes(){

        Iterable<Type> types = typeDao.findAll(Sort.by(Sort.Direction.ASC, "typeName"));
        List<Type> typesAsList = new ArrayList<>();

        types.forEach(typesAsList::add);

        return typesAsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Database findDatabaseById(Long databaseId) throws InstanceNotFoundException {

        Optional<Database> database = databaseDao.findById(databaseId);

        if(!database.isPresent()){
            throw new InstanceNotFoundException("project.entities.database", database);
        }

        Database finalDatabase = database.get();

        finalDatabase.setPassword(PasswordCipher.decode(finalDatabase.getPassword()));

        return finalDatabase;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Database> findAllUserDatabases(Long userId) {

        List<Database> databaseList = databaseDao.findByUserIdOrderByDatabaseAsc(userId);

        databaseList.forEach(d -> d.setPassword(PasswordCipher.decode(d.getPassword())));

        return databaseList;

    }

    @Override
    public Task findTaskById(Long taskId) throws InstanceNotFoundException {

        Optional<Task> task = taskDao.findById(taskId);

        if(!task.isPresent())
            throw new InstanceNotFoundException("project.entities.task", taskId);

        return task.get();
    }

    @Override
    public List<Task> findAllUserTasks(Long userId){

        return taskDao.findByUserIdOrderByCreationDateDesc(userId);

    }

    @Override
    public List<Log> findAllLogsByTaskId(Long taskId) {

        return logDao.findByTaskIdOrderById(taskId);

    }

    @Override
    public List<Log> findAllLogsByTaskIdAfterLogId(Long taskId, Long id){

        return logDao.findByTaskIdAndIdIsGreaterThanOrderById(taskId, id);

    }

}
