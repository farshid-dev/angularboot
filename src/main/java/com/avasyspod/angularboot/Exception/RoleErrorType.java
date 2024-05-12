package com.avasyspod.angularboot.Exception;

import com.avasyspod.angularboot.model.Role;

/**
 * Created by farshidkhalaj on 10/1/20.
 */
public class RoleErrorType extends Role {

    private String errorMessage;

    public RoleErrorType(final String errorMessage){

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {

        return errorMessage;
    }
}
