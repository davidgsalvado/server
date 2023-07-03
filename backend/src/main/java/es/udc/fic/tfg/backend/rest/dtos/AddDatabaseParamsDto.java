/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import javax.validation.constraints.*;

public class AddDatabaseParamsDto {

    private String database;
    private Long typeId;
    private String host;
    private int port;
    private String userDb;
    private String password;

    public AddDatabaseParamsDto(){}

    public AddDatabaseParamsDto(String database, Long typeId, String host, int port, String userDb, String password){
        this.database = database;
        this.typeId = typeId;
        this.host = host;
        this.port = port;
        this.userDb = userDb;
        this.password = password;
    }

    @NotEmpty
    public String getDatabase() {
        return database;
    }

    @NotNull
    @Positive
    public Long getTypeId(){
        return typeId;
    }

    @NotEmpty
    public String getHost(){
        return host;
    }

    @NotNull
    @Min(value = 0)
    @Max(value = 65535)
    public int getPort() {
        return port;
    }

    @NotEmpty
    public String getUserDb(){
        return userDb;
    }

    @NotEmpty
    public String getPassword(){
        return password;
    }

}
