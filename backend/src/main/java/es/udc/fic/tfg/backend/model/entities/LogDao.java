/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LogDao extends PagingAndSortingRepository<Log, Long> {

    List<Log> findByTaskIdOrderById(Long taskId);

    List<Log> findByTaskIdAndIdIsGreaterThanOrderById(Long taskId, Long id);

    void deleteByTaskId(Long taskId);

}
