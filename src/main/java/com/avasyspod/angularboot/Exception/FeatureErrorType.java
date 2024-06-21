package com.avasyspod.angularboot.Exception;

import com.avasyspod.angularboot.model.Features;

public class FeatureErrorType extends Features
{
    private String errorMessage;

    public FeatureErrorType(final String errorMessage){

        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {

        return errorMessage;
    }
}
