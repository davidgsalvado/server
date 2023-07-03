/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.controllers;

import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NotFoundEntityUsedByDatabaseException;
import es.udc.fic.tfg.backend.model.services.GeoJsonService;
import es.udc.fic.tfg.backend.model.util.EntityGeoJson;
import es.udc.fic.tfg.backend.rest.common.ErrorsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Locale;

@RestController
@RequestMapping("/geoJson")
public class GeoJsonServiceController {

    private final static String SQL_EXCEPTION_CODE = "project.exceptions.SQLException";
    private final static String NOT_FOUND_ENTITY_EXCEPTION_CODE = "project.exceptions.NotFoundEntityUsedByDatabaseException";

    @Autowired
    private GeoJsonService geoJsonService;

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleSQLException(SQLException exception, Locale locale){

        String errorMessage = messageSource.getMessage(SQL_EXCEPTION_CODE, null,
                SQL_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    @ExceptionHandler(NotFoundEntityUsedByDatabaseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handleNotFoundEntityUsedByDatabaseException(NotFoundEntityUsedByDatabaseException exception, Locale locale){

        String errorMessage = messageSource.getMessage(NOT_FOUND_ENTITY_EXCEPTION_CODE, null,
                NOT_FOUND_ENTITY_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);
    }

    @GetMapping("/database/{databaseId}/{tableName}")
    public EntityGeoJson getGeoJson(@RequestAttribute Long userId, @PathVariable Long databaseId,
                                    @PathVariable String tableName)
            throws SQLException, InstanceNotFoundException, NotFoundEntityUsedByDatabaseException {

        return geoJsonService.getGeoJson(databaseId, tableName);

    }

}
