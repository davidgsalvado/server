/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.services;

import es.udc.fic.tfg.backend.model.exceptions.InstanceNotFoundException;
import es.udc.fic.tfg.backend.model.exceptions.NotFoundEntityUsedByDatabaseException;
import es.udc.fic.tfg.backend.model.util.EntityGeoJson;

import java.sql.SQLException;

public interface GeoJsonService {

    EntityGeoJson getGeoJson(Long databaseId, String tableName) throws InstanceNotFoundException, SQLException, NotFoundEntityUsedByDatabaseException;

}
