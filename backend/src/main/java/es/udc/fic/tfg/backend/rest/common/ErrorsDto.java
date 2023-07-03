/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.common;

import java.util.List;

public class ErrorsDto {

    private String globalError;
    private List<FieldErrorDto> fieldErrors;

    public ErrorsDto(String globalError) {
        this.globalError = globalError;
    }

    public ErrorsDto(List<FieldErrorDto> fieldErrors) {

        this.fieldErrors = fieldErrors;

    }

    public String getGlobalError() {
        return globalError;
    }

    public void setGlobalError(String globalError) {
        this.globalError = globalError;
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
