/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.entities;

import javax.persistence.Entity;
import javax.persistence.EmbeddedId;

@Entity
public class EntityTable {

    @EmbeddedId
    private EntityTableIdentity entityTableIdentity;

    public EntityTable(){}

    public EntityTable(EntityTableIdentity entityTableIdentity){
        this.entityTableIdentity = entityTableIdentity;
    }

    public EntityTableIdentity getEntityTableIdentity() {
        return entityTableIdentity;
    }

    public void setEntityTableIdentity(EntityTableIdentity entityTableIdentity) {
        this.entityTableIdentity = entityTableIdentity;
    }
}
