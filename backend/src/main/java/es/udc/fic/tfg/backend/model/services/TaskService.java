/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.exceptions.CannotCancelException;
import es.udc.fic.tfg.backend.model.exceptions.CannotDeleteException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;

public interface TaskService {

    Task createTask(Long userId, String taskName, Long databaseId, String taskString) throws InstanceNotFoundException;

    void cancelTask(Long userId, Long taskId) throws InstanceNotFoundException, CannotCancelException;

    void deleteTask(Long userId, Long taskId) throws InstanceNotFoundException, CannotDeleteException;

}
