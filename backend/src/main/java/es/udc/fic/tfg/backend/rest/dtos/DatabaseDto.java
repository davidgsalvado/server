/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DatabaseDto {

    private Long id;
    private String databaseName;
    private Long typeId;
    private String host;
    private int port;
    private String userDb;
    private String password;

    public DatabaseDto(){}

    public DatabaseDto(Long id, String databaseName, Long typeId, String host, int port, String userDb, String password){
        this.id = id;
        this.databaseName = databaseName;
        this.typeId = typeId;
        this.host = host;
        this.port = port;
        this.userDb = userDb;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @NotNull
    @Positive
    public Long getTypeId(){
        return typeId;
    }

    public void setType(Long typeId){
        this.typeId = typeId;
    }

    @NotNull
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @NotNull
    @Min(value = 0)
    @Max(value = 65535)
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @NotNull
    public String getUserDb() {
        return userDb;
    }

    public void setUserDb(String userDb) {
        this.userDb = userDb;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
