package com.avasyspod.angularboot.model;

public class FinalFeatures {

    private String Rolename;
    private String featurename;
    private String Readoption;
    private String ReadwriteOption;


    public FinalFeatures() {
    }

    public FinalFeatures(String rolename, String featurename, boolean readoption, boolean readwriteOption) {
        Rolename = rolename;
        this.featurename = featurename;
    }

    public String getRolename() {
        return Rolename;
    }

    public void setRolename(String rolename) {
        Rolename = rolename;
    }

    public String getFeaturename() {
        return featurename;
    }

    public void setFeaturename(String featurename) {
        this.featurename = featurename;
    }

    public String getReadoption() {
        return Readoption;
    }

    public void setReadoption(String readoption) {
        Readoption = readoption;
    }

    public String getReadwriteOption() {
        return ReadwriteOption;
    }

    public void setReadwriteOption(String readwriteOption) {
        ReadwriteOption = readwriteOption;
    }

}
