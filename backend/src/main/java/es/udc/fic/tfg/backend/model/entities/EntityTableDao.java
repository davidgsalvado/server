/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EntityTableDao extends PagingAndSortingRepository<EntityTable, EntityTableIdentity> {

    List<EntityTable> findByEntityTableIdentity_Database_Id(Long databaseId);

    void deleteByEntityTableIdentity_Database_Id(Long databaseId);

}
