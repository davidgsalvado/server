/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

public class TaskDto {

    private Long id;
    private String taskName;
    private String taskString;
    private String state;
    private String error;
    private Long creationDate;
    private Long endDate;
    private Long databaseId;

    public TaskDto(){}

    public TaskDto(Long id, String taskName, String taskString, String state, String error, Long creationDate, Long endDate, Long databaseId){
        this.id = id;
        this.taskName = taskName;
        this.taskString = taskString;
        this.state = state;
        this.error = error;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.databaseId = databaseId;
    }

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskString() {
        return taskString;
    }

    public String getState() {
        return state;
    }

    public String getError() {
        return error;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public Long getDatabaseId(){
        return databaseId;
    }
}
