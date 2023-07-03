/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import es.udc.fic.tfg.backend.model.entities.Task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class TaskConversor {

    private TaskConversor(){}

    private final static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.SECONDS).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    private static TaskUserSummaryDto toTaskUserSummaryDto(Task task){
        long endDate = task.getEndDate() != null ? toMillis(task.getEndDate()) : 0;
        return new TaskUserSummaryDto(task.getId(), task.getTaskName(), task.getState(), toMillis(task.getCreationDate()),
                endDate);
    }

    public static List<TaskUserSummaryDto> toTaskUserSummaryDtos(List<Task> taskList){
        return taskList.stream().map(t -> toTaskUserSummaryDto(t)).collect(Collectors.toList());
    }

    public static TaskDto toTaskDto(Task task){
        long endDate = task.getEndDate() != null ? toMillis(task.getEndDate()) : 0;
        Long databaseId = task.getDatabase() != null ? task.getDatabase().getId() : null;
        return new TaskDto(task.getId(), task.getTaskName(), task.getTaskString(), task.getState(), task.getError(),
                toMillis(task.getCreationDate()), endDate, databaseId);
    }

}
