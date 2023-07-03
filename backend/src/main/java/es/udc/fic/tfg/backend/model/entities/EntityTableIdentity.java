/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EntityTableIdentity implements Serializable {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="databaseId")
    private Database database;
    private String entityName;

    public EntityTableIdentity(){}

    public EntityTableIdentity(Database database, String entityName){
        this.database = database;
        this.entityName = entityName;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityTableIdentity)) return false;
        EntityTableIdentity that = (EntityTableIdentity) o;
        return getDatabase().equals(that.getDatabase()) && getEntityName().equals(that.getEntityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDatabase(), getEntityName());
    }
}
