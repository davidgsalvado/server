/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

public class LogDto {

    private Long id;
    private String log;
    private String type;

    public LogDto(){}

    public LogDto(Long id, String log, String type){
        this.id = id;
        this.log = log;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getLog() {
        return log;
    }

    public String getType(){
        return type;
    }
}
