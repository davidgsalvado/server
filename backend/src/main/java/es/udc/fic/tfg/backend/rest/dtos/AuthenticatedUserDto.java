/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatedUserDto {

    private String serviceToken;
    private UserDto userDto;

    public AuthenticatedUserDto(){}

    public AuthenticatedUserDto(String serviceToken, UserDto userDto){
        this.serviceToken = serviceToken;
        this.userDto = userDto;
    }

    public String getServiceToken(){
        return serviceToken;
    }

    public void setServiceToken(String serviceToken){
        this.serviceToken = serviceToken;
    }

    @JsonProperty("user")
    public UserDto getUserDto(){
        return userDto;
    }

    public void setUserDto(UserDto userDto){
        this.userDto = userDto;
    }

}
