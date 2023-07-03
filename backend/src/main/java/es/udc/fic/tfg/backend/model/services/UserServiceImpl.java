/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.entities.User;
import es.udc.fic.tfg.backend.model.entities.UserDao;
import es.udc.fic.tfg.backend.model.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.backend.model.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PermissionChecker permissionChecker;

    @Override
    public void signUp(User user) throws DuplicateInstanceException {
        if(userDao.existsByUserName(user.getUserName())){
            throw new DuplicateInstanceException("project.entities.user", user.getUserName());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User login(String username, String password) throws IncorrectLoginException {

        Optional<User> user = userDao.findByUserName(username);

        if(!user.isPresent()){ //if we do not find the user in database
            throw new IncorrectLoginException(username, password);
        }

        if(!passwordEncoder.matches(password, user.get().getPassword())){ //password is wrong
            throw new IncorrectLoginException(username, password);
        }

        return user.get(); //user is logged

    }

    @Override
    @Transactional(readOnly = true)
    public User loginFromId(Long userId) throws InstanceNotFoundException {
        return permissionChecker.checkUser(userId);
    }

}
