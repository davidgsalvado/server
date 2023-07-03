/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

    public interface AllValidations {}

    private Long id;
    private String userName;
    private String password;
    private String email;
    private String role;

    public UserDto(){}

    public UserDto(Long id, String userName, String email){
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = "USER";
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    @NotNull(groups={AllValidations.class})
    @Size(min=1, max=60, groups={AllValidations.class})
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.trim();
    }

    @NotNull(groups={AllValidations.class})
    @Size(min=1, max=60, groups={AllValidations.class})
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(groups={AllValidations.class})
    @Size(min=1, max=60, groups={AllValidations.class})
    @Email(groups={AllValidations.class})
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
