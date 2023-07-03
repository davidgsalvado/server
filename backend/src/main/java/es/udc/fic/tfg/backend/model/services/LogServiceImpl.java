/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.LogDao;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.entities.TaskDao;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LogServiceImpl implements LogService{

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private LogDao logDao;

    @Override
    public void deleteByTaskId(Long userId, Long taskId) throws InstanceNotFoundException {

        permissionChecker.checkUser(userId);

        Optional<Task> optionalTask = taskDao.findById(taskId);

        if (!optionalTask.isPresent()){
            throw new InstanceNotFoundException("project.entities.task", taskId);
        }

        logDao.deleteByTaskId(taskId);
    }
}
