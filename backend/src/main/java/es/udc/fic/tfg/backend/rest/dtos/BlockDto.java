/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import java.util.List;

public class BlockDto<T> {

    private List<T> items;
    private boolean existMoreItems;

    public BlockDto() {}

    public BlockDto(List<T> items, boolean existMoreItems) {

        this.items = items;
        this.existMoreItems = existMoreItems;

    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public boolean getExistMoreItems() {
        return existMoreItems;
    }

    public void setExistMoreItems(boolean existMoreItems) {
        this.existMoreItems = existMoreItems;
    }

}
