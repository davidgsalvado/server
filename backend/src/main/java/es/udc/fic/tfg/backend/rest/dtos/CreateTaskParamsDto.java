/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class CreateTaskParamsDto {

    private String taskName;
    private String taskString;
    private Long databaseId;

    public CreateTaskParamsDto(){}

    public CreateTaskParamsDto(String taskName, String taskString){
        this.taskName = taskName;
        this.taskString = taskString;
    }

    @NotEmpty
    public String getTaskName(){
        return taskName;
    }

    @NotEmpty
    public String getTaskString(){
        return taskString;
    }

    @Positive
    public Long getDatabaseId(){
        return databaseId;
    }

    public void setDatabaseId(Long databaseId){
        this.databaseId = databaseId;
    }

}
