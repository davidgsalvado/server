/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.EntityTable;
import es.udc.fic.tfg.backend.model.entities.EntityTableDao;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NotFoundEntityUsedByDatabaseException;
import es.udc.fic.tfg.backend.model.util.EntityGeoJson;
import es.udc.fic.tfg.backend.model.util.GeoJsonConnector;
import es.udc.fic.tfg.backend.model.util.PasswordCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class GeoJsonServiceImpl implements GeoJsonService{

    @Autowired
    private EntityTableDao entityTableDao;

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    @Transactional(readOnly = true)
    public EntityGeoJson getGeoJson(Long databaseId, String tableName) throws InstanceNotFoundException, SQLException,
            NotFoundEntityUsedByDatabaseException {

        Optional<Database> database = databaseDao.findById(databaseId);

        if (!database.isPresent()){
            throw new InstanceNotFoundException("project.entities.database", databaseId);
        }

        Database finalDatabase = database.get();
        finalDatabase.setPassword(PasswordCipher.decode(finalDatabase.getPassword()));

        List<EntityTable> entityTableList = entityTableDao.findByEntityTableIdentity_Database_Id(databaseId);
        EntityTable entityTable = entityTableList.stream().filter(e -> e.getEntityTableIdentity().getEntityName().equalsIgnoreCase(tableName))
                .findFirst().orElse(null);
        if (entityTable == null){
            throw new NotFoundEntityUsedByDatabaseException("Entity " + tableName + " not used by " + finalDatabase.getDatabase());
        }

        GeoJsonConnector geoJsonConnector = new GeoJsonConnector();

        return geoJsonConnector.connectAndGetGeoJson(entityTable, finalDatabase);
    }

}
