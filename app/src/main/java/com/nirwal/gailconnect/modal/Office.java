package com.nirwal.gailconnect.modal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "office_table")
public class Office implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Location;
    private String GailnetCode;
    private String Address;
    private String Fax;
    private String Latitude; //":"23.3370304",
    private String Longitude; //":"85.2660767",
    private String EPABX;

    public Office() {
    }

    public Office(String location,
                  String gailnetCode,
                  String address,
                  String fax,
                  String latitude,
                  String longitude,
                  String EPABX) {
        Location = location;
        GailnetCode = gailnetCode;
        Address = address;
        Fax = fax;
        Latitude = latitude;
        Longitude = longitude;
        this.EPABX = EPABX;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getGailnetCode() {
        return GailnetCode;
    }

    public void setGailnetCode(String gailnetCode) {
        GailnetCode = gailnetCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getEPABX() {
        return EPABX;
    }

    public void setEPABX(String EPABX) {
        this.EPABX = EPABX;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.Location);
        dest.writeString(this.GailnetCode);
        dest.writeString(this.Address);
        dest.writeString(this.Fax);
        dest.writeString(this.Latitude);
        dest.writeString(this.Longitude);
        dest.writeString(this.EPABX);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.Location = source.readString();
        this.GailnetCode = source.readString();
        this.Address = source.readString();
        this.Fax = source.readString();
        this.Latitude = source.readString();
        this.Longitude = source.readString();
        this.EPABX = source.readString();
    }

    protected Office(Parcel in) {
        this.id = in.readInt();
        this.Location = in.readString();
        this.GailnetCode = in.readString();
        this.Address = in.readString();
        this.Fax = in.readString();
        this.Latitude = in.readString();
        this.Longitude = in.readString();
        this.EPABX = in.readString();
    }

    public static final Parcelable.Creator<Office> CREATOR = new Parcelable.Creator<Office>() {
        @Override
        public Office createFromParcel(Parcel source) {
            return new Office(source);
        }

        @Override
        public Office[] newArray(int size) {
            return new Office[size];
        }
    };
}
