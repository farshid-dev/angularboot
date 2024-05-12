package com.avasyspod.angularboot.Exception;

import com.avasyspod.angularboot.model.User;

public class CustomErrorType extends User {

	private String errorMessage;
	 
    public CustomErrorType(final String errorMessage){
        this.errorMessage = errorMessage;
    }
 
    public String getErrorMessage() {
        return errorMessage;
    }
}
