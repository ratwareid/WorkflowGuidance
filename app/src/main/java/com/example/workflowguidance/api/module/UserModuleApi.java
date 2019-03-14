package com.example.workflowguidance.api.module;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModuleApi {

    @SerializedName("RC")
    @Expose
    private String responCode;

    @SerializedName("RD")
    @Expose
    private String responDesc;

    @SerializedName("userid")
    @Expose
    private String userID;

    @SerializedName("name")
    @Expose
    private String userName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("companyID")
    @Expose
    private String companyID;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getResponCode() {
        return responCode;
    }

    public void setResponCode(String responCode) {
        this.responCode = responCode;
    }

    public String getResponDesc() {
        return responDesc;
    }

    public void setResponDesc(String responDesc) {
        this.responDesc = responDesc;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
