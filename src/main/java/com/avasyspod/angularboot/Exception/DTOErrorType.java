package com.avasyspod.angularboot.Exception;

import com.avasyspod.angularboot.security.UserDTO;

public class DTOErrorType extends UserDTO {

    private String errorMessage;

    public DTOErrorType(final String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
