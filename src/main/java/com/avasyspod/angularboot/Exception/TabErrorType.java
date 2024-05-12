package com.avasyspod.angularboot.Exception;

import com.avasyspod.angularboot.model.Tabs;

public class TabErrorType extends Tabs {

    private String errorMessage;

    public TabErrorType(final String errorMessage){

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {

        return errorMessage;
    }
}
