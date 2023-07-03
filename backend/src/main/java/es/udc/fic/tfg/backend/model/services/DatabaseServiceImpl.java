/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.EntityTable;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.entities.TypeDao;
import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.EntityTableDao;
import es.udc.fic.tfg.backend.model.entities.TaskDao;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NoDatabaseConnectionException;
import es.udc.fic.tfg.backend.model.exceptions.PermissionException;
import es.udc.fic.tfg.backend.model.util.PasswordCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.udc.fic.tfg.osmparser.backend.model.util.DbConnectionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private EntityTableDao entityTableDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public Database addDatabase(String database, Long typeId, String host, int port, String userDb, String password, Long userId) throws InstanceNotFoundException, NoDatabaseConnectionException {

        User user = permissionChecker.checkUser(userId); //check if user exists
        Optional<Type> type = typeDao.findById(typeId);

        if(!type.isPresent()){
            throw new InstanceNotFoundException("project.entities.type", typeId);
        }

        Type finalType = type.get();

        DbConnectionContext context = new DbConnectionContext(finalType.getTypeName());

        boolean canConnect = context.createAndTest(database, host, port, userDb, password); // check database connection

        if(!canConnect) // if we cannot connect using the given connection parameters, we cannot save the database
            throw new NoDatabaseConnectionException();

        Database newDatabase = new Database(user, database, finalType, host, port, userDb, PasswordCipher.encode(password));

        databaseDao.save(newDatabase);

        return newDatabase;
    }

    @Override
    public void deleteDatabase(Long userId, Long databaseId) throws PermissionException, InstanceNotFoundException{

        Optional<Database> database = databaseDao.findById(databaseId);

        if(!database.isPresent()){
            throw new InstanceNotFoundException("project.entities.database", database);
        }

        boolean dbBelongs = permissionChecker.checkDatabaseBelongsToUser(userId, databaseId);

        if(!dbBelongs)
            throw new PermissionException();
        else {
            Database finalDatabase = database.get();
            entityTableDao.deleteByEntityTableIdentity_Database_Id(finalDatabase.getId());
            List<Task> taskList = taskDao.findByDatabaseId(databaseId);

            for (Task task : taskList){
                task.setDatabase(null);
                taskDao.save(task);
            }

            databaseDao.delete(finalDatabase);
        }
    }

    @Override
    public Database updateDatabase(Long databaseId, Long userId, String databaseName, Long typeId, String host, int port, String userDb, String password) throws InstanceNotFoundException, PermissionException, NoDatabaseConnectionException {

        Optional<Database> database = databaseDao.findById(databaseId);
        Optional<Type> type = typeDao.findById(typeId);

        if(!type.isPresent()){
            throw new InstanceNotFoundException("project.entities.type", typeId);
        }

        if(!database.isPresent()){
            throw new InstanceNotFoundException("project.entities.database", database);
        }

        boolean dbBelongs = permissionChecker.checkDatabaseBelongsToUser(userId, databaseId);

        if(!dbBelongs)
            throw new PermissionException();
        else{
            Database finalDatabase = database.get();
            Type finalType = type.get();

            DbConnectionContext context = new DbConnectionContext(finalType.getTypeName());

            boolean canConnect = context.createAndTest(databaseName, host, port, userDb, password); // check database connection

            if(!canConnect) // if we cannot connect using the given connection parameters, we cannot save the database
                throw new NoDatabaseConnectionException();

            finalDatabase.setDatabase(databaseName);
            finalDatabase.setHost(host);
            finalDatabase.setPort(port);
            finalDatabase.setUserDb(userDb);
            finalDatabase.setPassword(PasswordCipher.encode(password));
            finalDatabase.setType(finalType);

            databaseDao.save(finalDatabase);

            return finalDatabase;
        }
    }

    @Override
    public List<String> getDatabaseTables(Long databaseId) {

        List<EntityTable> entityTableList = entityTableDao.findByEntityTableIdentity_Database_Id(databaseId);
        List<String> tables = new ArrayList<>();

        for (EntityTable entityTable : entityTableList)
            tables.add(entityTable.getEntityTableIdentity().getEntityName());

        return tables;
    }

}
