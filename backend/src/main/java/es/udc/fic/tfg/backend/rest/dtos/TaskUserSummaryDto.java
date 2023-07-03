/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

public class TaskUserSummaryDto {

    private Long id;
    private String taskName;
    private String state;
    private Long creationDate;
    private Long endDate;

    public TaskUserSummaryDto(){}

    public TaskUserSummaryDto(Long id, String taskName, String state, Long creationDate, Long endDate) {
        this.id = id;
        this.taskName = taskName;
        this.state = state;
        this.creationDate = creationDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getState() {
        return state;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Long getEndDate() {
        return endDate;
    }
}
