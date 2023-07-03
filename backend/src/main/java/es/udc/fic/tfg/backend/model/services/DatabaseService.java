/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NoDatabaseConnectionException;
import es.udc.fic.tfg.backend.model.exceptions.PermissionException;

import java.util.List;

public interface DatabaseService {

    Database addDatabase(String database, Long typeId, String host, int port, String userdb, String password, Long userId)
            throws InstanceNotFoundException, NoDatabaseConnectionException;

    void deleteDatabase(Long userId, Long databaseId) throws PermissionException, InstanceNotFoundException;

    Database updateDatabase(Long databaseId, Long userId, String database, Long typeId, String host, int port, String userdb,
                            String password) throws InstanceNotFoundException, PermissionException, NoDatabaseConnectionException;

    List<String> getDatabaseTables(Long databaseId);

}
