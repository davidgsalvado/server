/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TypeDao extends PagingAndSortingRepository<Type, Long> {

    Type getTypeByTypeName(String typeName);

}
