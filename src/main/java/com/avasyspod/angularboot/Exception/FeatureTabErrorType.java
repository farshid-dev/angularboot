package com.avasyspod.angularboot.Exception;

import java.util.HashMap;
import java.util.Map;

public class FeatureTabErrorType
{
    private String errorMessage;
    public FeatureTabErrorType(final String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public Map<String, String> toMap()
    {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", errorMessage);
        return errorMap;
    }
}
