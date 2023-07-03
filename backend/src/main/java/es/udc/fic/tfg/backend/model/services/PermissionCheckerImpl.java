/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.DatabaseDao;
import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.entities.UserDao;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker{

    @Autowired
    private UserDao userDao;

    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public void checkUserExists(Long userId) throws InstanceNotFoundException {

        if(!userDao.existsById(userId)){
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

    }

    @Override
    public User checkUser(Long userId) throws InstanceNotFoundException {

        Optional<User> user = userDao.findById(userId);

        if(!user.isPresent()){
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        return user.get();
    }

    @Override
    public boolean checkDatabaseBelongsToUser(Long userId, Long databaseId) throws InstanceNotFoundException{

        User user = checkUser(userId);

        return databaseDao.existsDatabaseByIdAndUser(databaseId, user);
    }

}
