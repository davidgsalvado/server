/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.controllers;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NoDatabaseConnectionException;
import es.udc.fic.tfg.backend.model.exceptions.PermissionException;
import es.udc.fic.tfg.backend.model.services.DatabaseService;
import es.udc.fic.tfg.backend.rest.common.ErrorsDto;
import es.udc.fic.tfg.backend.rest.dtos.AddDatabaseParamsDto;
import es.udc.fic.tfg.backend.rest.dtos.DatabaseDto;
import es.udc.fic.tfg.backend.rest.dtos.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static es.udc.fic.tfg.backend.rest.dtos.DatabaseConversor.toDatabaseDto;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/databases")
public class DatabaseServiceController {

    private final static String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";
    private final static String NO_DATABASE_CONNECTION_EXCEPTION_CODE = "project.exceptions.NoDatabaseConnectionException";

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handlePermissionException(PermissionException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE, null,
                PERMISSION_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    @ExceptionHandler(NoDatabaseConnectionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handleNoDatabaseConnectionException(NoDatabaseConnectionException exception, Locale locale){

        String errorMessage = messageSource.getMessage(NO_DATABASE_CONNECTION_EXCEPTION_CODE, null,
                NO_DATABASE_CONNECTION_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    @PostMapping("/add")
    public IdDto addDatabase(@RequestAttribute Long userId, @Validated @RequestBody AddDatabaseParamsDto params)
            throws InstanceNotFoundException, NoDatabaseConnectionException {

        Database database = databaseService.addDatabase(params.getDatabase(), params.getTypeId(), params.getHost(),
                params.getPort(), params.getUserDb(), params.getPassword(), userId);

        return new IdDto(database.getId());
    }

    @DeleteMapping("/{databaseId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDatabase(@RequestAttribute Long userId, @PathVariable Long databaseId) throws PermissionException, InstanceNotFoundException {

        databaseService.deleteDatabase(userId, databaseId);

    }

    @PutMapping("/{databaseId}/update")
    public DatabaseDto updateDatabase(@RequestAttribute Long userId, @PathVariable Long databaseId, @Validated @RequestBody DatabaseDto databaseDto)
            throws InstanceNotFoundException, PermissionException, NoDatabaseConnectionException {

        Database database = databaseService.updateDatabase(databaseId, userId, databaseDto.getDatabaseName(), databaseDto.getTypeId(), databaseDto.getHost(), databaseDto.getPort(),
                databaseDto.getUserDb(), databaseDto.getPassword());

        return toDatabaseDto(database);

    }

    @GetMapping("/{databaseId}/tables")
    public List<String> getDatabaseTables(@RequestAttribute Long userId, @PathVariable Long databaseId){

        return databaseService.getDatabaseTables(databaseId);

    }

}
