package com.nirwal.gailconnect.modal;

public class AuthResponse {

    private boolean Response;       //true/false,
    private String Cpf_Number; //": "00017592",
    private String User_Acess;//": "1",
    private String APK_Version_No;//": "1.6.12",
    private String Notification;//": "happy birth day",
    private String Business_Area;//": "6040",
    private String Email;//": "ak.nirwal@gail.co.in",
    private String Gstin;//": "24AAACG1209J3Z0",
    private String Ipa_version;//": "1.6.12",
    private String Ba_Name;//": "LPG - GANDHAR",
    private String Gstn_Location;//": "GJ"


    public AuthResponse() {
    }


    public AuthResponse(boolean response,
                        String cpf_Number,
                        String user_Acess,
                        String APK_Version_No,
                        String notification,
                        String business_Area,
                        String email,
                        String gstin,
                        String ipa_version,
                        String ba_Name,
                        String gstn_Location) {
        this.Response = response;
        this.Cpf_Number = cpf_Number;
        this.User_Acess = user_Acess;
        this.APK_Version_No = APK_Version_No;
        this.Notification = notification;
        this.Business_Area = business_Area;
        this.Email = email;
        this.Gstin = gstin;
        this.Ipa_version = ipa_version;
        this.Ba_Name = ba_Name;
        this.Gstn_Location = gstn_Location;
    }

    public boolean isResponse() {
        return Response;
    }

    public void setResponse(boolean response) {
        Response = response;
    }

    public String getCpf_Number() {
        return Cpf_Number;
    }

    public void setCpf_Number(String cpf_Number) {
        Cpf_Number = cpf_Number;
    }

    public String getUser_Acess() {
        return User_Acess;
    }

    public void setUser_Acess(String user_Acess) {
        User_Acess = user_Acess;
    }

    public String getAPK_Version_No() {
        return APK_Version_No;
    }

    public void setAPK_Version_No(String APK_Version_No) {
        this.APK_Version_No = APK_Version_No;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getBusiness_Area() {
        return Business_Area;
    }

    public void setBusiness_Area(String business_Area) {
        Business_Area = business_Area;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGstin() {
        return Gstin;
    }

    public void setGstin(String gstin) {
        Gstin = gstin;
    }

    public String getIpa_version() {
        return Ipa_version;
    }

    public void setIpa_version(String ipa_version) {
        Ipa_version = ipa_version;
    }

    public String getBa_Name() {
        return Ba_Name;
    }

    public void setBa_Name(String ba_Name) {
        Ba_Name = ba_Name;
    }

    public String getGstn_Location() {
        return Gstn_Location;
    }

    public void setGstn_Location(String gstn_Location) {
        Gstn_Location = gstn_Location;
    }
}
