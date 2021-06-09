package com.nirwal.gailconnect.modal;

public class User {
    private String userid;
    private String password;
    private String Device_Token;
    private String DEVICE_MODEL;
    private DeviceProperties Device_properties; //": JSON.stringify(deviceprop),

    public User() {
    }

    public User(String userid, String password, String device_Token, String DEVICE_MODEL, DeviceProperties device_properties) {
        this.userid = userid;
        this.password = password;
        this.Device_Token = device_Token;
        this.DEVICE_MODEL = DEVICE_MODEL;
        this.Device_properties = device_properties;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_Token() {
        return Device_Token;
    }

    public void setDevice_Token(String device_Token) {
        Device_Token = device_Token;
    }

    public String getDEVICE_MODEL() {
        return DEVICE_MODEL;
    }

    public void setDEVICE_MODEL(String DEVICE_MODEL) {
        this.DEVICE_MODEL = DEVICE_MODEL;
    }

    public DeviceProperties getDevice_properties() {
        return Device_properties;
    }

    public void setDevice_properties(DeviceProperties device_properties) {
        Device_properties = device_properties;
    }
}
