/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import es.udc.fic.tfg.backend.model.entities.Log;

import java.util.List;
import java.util.stream.Collectors;

public class LogConversor {

    private LogConversor(){}

    private static LogDto toLogDto(Log log){
        return new LogDto(log.getId(), log.getLog(), log.getLogType());
    }

    public static List<LogDto> toLogDtos(List<Log> logs){
        return logs.stream().map(l -> toLogDto(l)).collect(Collectors.toList());
    }

}
