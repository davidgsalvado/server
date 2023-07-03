/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.backend.model.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;

public interface UserService {

    void signUp(User user) throws DuplicateInstanceException;

    User login(String username, String password) throws IncorrectLoginException;

    User loginFromId(Long userId) throws InstanceNotFoundException;

}
