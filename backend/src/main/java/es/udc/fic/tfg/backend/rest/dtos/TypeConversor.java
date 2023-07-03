/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.dtos;

import es.udc.fic.tfg.backend.model.entities.Type;

import java.util.List;
import java.util.stream.Collectors;

public class TypeConversor {

    private TypeConversor(){}

    private final static TypeDto toTypeDto(Type type){
        return new TypeDto(type.getId(), type.getTypeName());
    }

    public final static List<TypeDto> toTypeDtos(List<Type> typeList){
        return typeList.stream().map(t -> toTypeDto(t)).collect(Collectors.toList());
    }

}
