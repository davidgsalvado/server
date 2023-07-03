/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Log;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.Type;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;

import java.util.List;

public interface CatalogService {

    List<Type> findAllDatabaseTypes();

    Database findDatabaseById(Long databaseId) throws InstanceNotFoundException;

    List<Database> findAllUserDatabases(Long userId);

    Task findTaskById(Long taskId) throws InstanceNotFoundException;

    List<Task> findAllUserTasks(Long userId);

    List<Log> findAllLogsByTaskId(Long taskId);

    List<Log> findAllLogsByTaskIdAfterLogId(Long taskId, Long lastLogId);

}
