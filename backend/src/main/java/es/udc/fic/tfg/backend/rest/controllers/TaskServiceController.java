/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.controllers;

import es.udc.fic.tfg.backend.model.entities.Task;
import es.udc.fic.tfg.backend.model.exceptions.CannotCancelException;
import es.udc.fic.tfg.backend.model.exceptions.CannotDeleteException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.services.TaskService;
import es.udc.fic.tfg.backend.rest.common.ErrorsDto;
import es.udc.fic.tfg.backend.rest.dtos.CreateTaskParamsDto;
import es.udc.fic.tfg.backend.rest.dtos.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Locale;

@RestController
@RequestMapping("/tasks")
public class TaskServiceController {

    private final static String CANNOT_CANCEL_EXCEPTION_CODE = "project.exceptions.CannotCancelException";
    private final static String CANNOT_DELETE_EXCEPTION_CODE = "project.exceptions.CannotDeleteException";

    @Autowired
    private TaskService taskService;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(CannotCancelException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleCannotCancelException(CannotCancelException exception, Locale locale){

        String errorMessage = messageSource.getMessage(CANNOT_CANCEL_EXCEPTION_CODE, null,
                CANNOT_CANCEL_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    @ExceptionHandler(CannotDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleCannotDeleteException(CannotDeleteException exception, Locale locale){

        String errorMessage = messageSource.getMessage(CANNOT_DELETE_EXCEPTION_CODE, null,
                CANNOT_DELETE_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    @PostMapping("/add")
    public IdDto createTask(@RequestAttribute Long userId, @Validated @RequestBody CreateTaskParamsDto params)
            throws InstanceNotFoundException {

        Task task = taskService.createTask(userId, params.getTaskName(), params.getDatabaseId(), params.getTaskString());

        return new IdDto(task.getId());

    }

    @PutMapping("/{taskId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTask(@RequestAttribute Long userId, @PathVariable Long taskId) throws InstanceNotFoundException,
            CannotCancelException {

        taskService.cancelTask(userId, taskId);

    }

    @DeleteMapping("/{taskId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@RequestAttribute Long userId, @PathVariable Long taskId) throws InstanceNotFoundException,
            CannotDeleteException {

        taskService.deleteTask(userId, taskId);

    }
}
