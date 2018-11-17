package com.jinyun.antivirusfour.telephoneList.entity;

/**
 * 联系人信息实体类
 */

public class ContactInfo {

    private String id;
    private String name;
    private String number;
    private int versionCode;
    private String versionName;
    private String description;
    private String downloadUrl;


    public ContactInfo() {
        this("id","name","number",0,"0");
    }

    public ContactInfo(String id, String name, String number, int versionCode, String versionName) {
        this(id,name,number,versionCode,versionName,"");
    }

    public ContactInfo(String id, String name, String number, int versionCode, String versionName, String description) {
        this.id=id;
        this.name = name;
        this.number = number;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.description = description;
        this.downloadUrl = "";
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public int getVersionCode() {
        return versionCode;
    }
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String toJson() {
        return "{" +
                "\"id\":\"" + id + '"' +
                "\"name\":\"" + name + '"' +
                ", \"number\":\"" + number + '"' +
                "\"versionCode\":\"" + versionCode + '"' +
                ", \"versionName\":\"" + versionName + '"' +
                ", \"description\":\"" + description + '"' +
                ", \"downloadUrl\":\"" + downloadUrl + '"' +
                '}';
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", description='" + description + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

}
