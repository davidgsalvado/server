/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.controllers;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.Log;
import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.services.CatalogService;
import es.udc.fic.tfg.backend.rest.dtos.TaskUserSummaryDto;
import es.udc.fic.tfg.backend.rest.dtos.TypeDto;
import es.udc.fic.tfg.backend.rest.dtos.DatabaseUserSummaryDto;
import es.udc.fic.tfg.backend.rest.dtos.TaskDto;
import es.udc.fic.tfg.backend.rest.dtos.LogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import static es.udc.fic.tfg.backend.rest.dtos.DatabaseConversor.toDatabaseUserSummaryDtos;
import static es.udc.fic.tfg.backend.rest.dtos.TypeConversor.toTypeDtos;
import static es.udc.fic.tfg.backend.rest.dtos.TaskConversor.toTaskUserSummaryDtos;
import static es.udc.fic.tfg.backend.rest.dtos.TaskConversor.toTaskDto;
import static es.udc.fic.tfg.backend.rest.dtos.LogConversor.toLogDtos;

@RestController
@RequestMapping("/catalog")
public class CatalogServiceController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/databaseTypes")
    public List<TypeDto> findAllDatabaseTypes(){
        return toTypeDtos(catalogService.findAllDatabaseTypes());
    }

    @GetMapping("/user/databases")
    public List<DatabaseUserSummaryDto> findAllUserDatabases(@RequestAttribute Long userId){

        List<Database> databaseList = catalogService.findAllUserDatabases(userId);

        return toDatabaseUserSummaryDtos(databaseList);
    }

    @GetMapping("/user/tasks")
    public List<TaskUserSummaryDto> findAllUserTasks(@RequestAttribute Long userId){

        List<Task> taskList = catalogService.findAllUserTasks(userId);

        return toTaskUserSummaryDtos(taskList);
    }

    @GetMapping("/tasks/{taskId}")
    public TaskDto findTaskById(@RequestAttribute Long userId, @PathVariable Long taskId) throws InstanceNotFoundException {

        return toTaskDto(catalogService.findTaskById(taskId));
    }

    @GetMapping("/tasks/{taskId}/logs")
    public List<LogDto> findAllLogsByTaskId(@RequestAttribute Long userId, @PathVariable Long taskId){

        List<Log> logList = catalogService.findAllLogsByTaskId(taskId);

        return toLogDtos(logList);

    }

    @GetMapping("/tasks/{taskId}/logs/{logId}")
    public List<LogDto> findAllLogsByTaskIdAfterLogId(@RequestAttribute Long userId, @PathVariable Long taskId,
                                                      @PathVariable Long logId){

        return toLogDtos(catalogService.findAllLogsByTaskIdAfterLogId(taskId, logId));

    }

}
