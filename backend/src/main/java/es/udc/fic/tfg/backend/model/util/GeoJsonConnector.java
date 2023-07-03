/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.util;

import es.udc.fic.tfg.backend.model.entities.Database;
import es.udc.fic.tfg.backend.model.entities.EntityTable;
import es.udc.fic.tfg.osmparser.backend.model.util.ParserBdConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeoJsonConnector {

    public GeoJsonConnector(){}

    public EntityGeoJson connectAndGetGeoJson(EntityTable entityTable, Database database) throws SQLException {
        ParserBdConnector parserBdConnector = new ParserBdConnector(database.getType().getTypeName());

        Connection connection = parserBdConnector.getConnection(database.getDatabase(), database.getHost(), database.getPort(),
                database.getUserDb(), database.getPassword());

        String queryString = parserBdConnector.getQueryStringGeoJson(entityTable.getEntityTableIdentity().getEntityName(),
                database.getUserDb(), database.getPassword());
        System.out.println(queryString);

        EntityGeoJson entityGeoJson;
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String json = resultSet.getString(1);
            entityGeoJson = new EntityGeoJson(entityTable.getEntityTableIdentity().getEntityName(), json);
        }

        return entityGeoJson;
    }

}
