package com.example.workflowguidance.api.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyModuleApi {

    @SerializedName("RC")
    @Expose
    private String responCode;

    @SerializedName("RD")
    @Expose
    private String responDesc;

    @SerializedName("TotalUser")
    @Expose
    private Integer totalUser;

    @SerializedName("CName")
    @Expose
    private String companyName;

    @SerializedName("CAddress")
    @Expose
    private String companyAddress;

    @SerializedName("CDirector")
    @Expose
    private String companyDirector;

    @SerializedName("CType")
    @Expose
    private String businessType;

    @SerializedName("CPhone")
    @Expose
    private String phoneNumber;

    @SerializedName("CWeb")
    @Expose
    private String website;

    @SerializedName("Cin")
    @Expose
    private String incorporatedDate;

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

    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyDirector() {
        return companyDirector;
    }

    public void setCompanyDirector(String companyDirector) {
        this.companyDirector = companyDirector;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIncorporatedDate() {
        return incorporatedDate;
    }

    public void setIncorporatedDate(String incorporatedDate) {
        this.incorporatedDate = incorporatedDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
