/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.model.exceptions;

@SuppressWarnings("serial")
public class InstanceException extends Exception{

    private String name;
    private Object key;

    protected InstanceException(String message) {
        super(message);
    }

    public InstanceException(String name, Object key) {

        this.name = name;
        this.key = key;

    }

    public String getName() {
        return name;
    }

    public Object getKey() {
        return key;
    }

}
