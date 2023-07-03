/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

public class DatabaseUserSummaryDto {

    private Long id;
    private String database;
    private Long typeId;
    private String host;
    private int port;
    private String userDb;
    private String password;

    public DatabaseUserSummaryDto(){}

    public DatabaseUserSummaryDto(Long id, String database, Long typeId, String host, int port, String userDb, String password){
        this.id = id;
        this.database = database;
        this.typeId = typeId;
        this.host = host;
        this.port = port;
        this.userDb = userDb;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUserDb() {
        return userDb;
    }

    public String getPassword(){
        return password;
    }

    public Long getTypeId(){
        return typeId;
    }
}
