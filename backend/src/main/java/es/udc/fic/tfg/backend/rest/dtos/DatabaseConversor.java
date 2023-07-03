/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import es.udc.fic.tfg.backend.model.entities.Database;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseConversor {

    private DatabaseConversor(){}

    private static DatabaseUserSummaryDto toDatabaseUserSummaryDto(Database database){
        return new DatabaseUserSummaryDto(database.getId(), database.getDatabase(), database.getType().getId(), database.getHost(), database.getPort(), database.getUserDb(), database.getPassword());
    }

    public static List<DatabaseUserSummaryDto> toDatabaseUserSummaryDtos(List<Database> databases){
        return databases.stream().map(d -> toDatabaseUserSummaryDto(d)).collect(Collectors.toList());
    }

    public static DatabaseDto toDatabaseDto(Database database){
        return new DatabaseDto(database.getId(), database.getDatabase(), database.getType().getId(), database.getHost(), database.getPort(), database.getUserDb(), database.getPassword());
    }

}
