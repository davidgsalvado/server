/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DatabaseDao extends PagingAndSortingRepository<Database, Long> {

    List<Database> findByUserIdOrderByDatabaseAsc(Long userId);

    boolean existsDatabaseByIdAndUser(Long databaseId, User user);

    int countByDatabaseAndPortAndHostAndUserDbAndPasswordAndType(String database, int port, String host, String userDb, String password,
                                                                    Type type);

    Database findByDatabaseAndPortAndHostAndUserDbAndPasswordAndTypeAndUserId(String database, int port, String host, String userDb,
                                                                            String password, Type type, Long userId);

}
