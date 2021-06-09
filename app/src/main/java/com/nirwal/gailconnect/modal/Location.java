package com.nirwal.gailconnect.modal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Location {



    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int uniq_id;

    public String ID;

    public String LOCATION;
    public String MODIFIED_BY;
    public String MODIFIED_ON; // eg = "11\/19\/2018 2:27:08 PM",
    public String SUBLOCATION;
    public String TEL1;
    public String TEL2;
    public String TEL3;
    public String TEL4;

    public String CREATED_BY;
    public String CREATED_ON;
    public String HBJNO;

    public Location() {
    }

    @Ignore
    public Location(String CREATED_BY,
                    String CREATED_ON,
                    String HBJNO, String ID,
                    String LOCATION,
                    String MODIFIED_BY,
                    String MODIFIED_ON,
                    String SUBLOCATION,
                    String TEL1,
                    String TEL2,
                    String TEL3,
                    String TEL4) {
        this.CREATED_BY = CREATED_BY;
        this.CREATED_ON = CREATED_ON;
        this.HBJNO = HBJNO;
        this.ID = ID;
        this.LOCATION = LOCATION;
        this.MODIFIED_BY = MODIFIED_BY;
        this.MODIFIED_ON = MODIFIED_ON;
        this.SUBLOCATION = SUBLOCATION;
        this.TEL1 = TEL1;
        this.TEL2 = TEL2;
        this.TEL3 = TEL3;
        this.TEL4 = TEL4;
    }



    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public String getHBJNO() {
        return HBJNO;
    }

    public void setHBJNO(String HBJNO) {
        this.HBJNO = HBJNO;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getMODIFIED_BY() {
        return MODIFIED_BY;
    }

    public void setMODIFIED_BY(String MODIFIED_BY) {
        this.MODIFIED_BY = MODIFIED_BY;
    }

    public String getMODIFIED_ON() {
        return MODIFIED_ON;
    }

    public void setMODIFIED_ON(String MODIFIED_ON) {
        this.MODIFIED_ON = MODIFIED_ON;
    }

    public String getSUBLOCATION() {
        return SUBLOCATION;
    }

    public void setSUBLOCATION(String SUBLOCATION) {
        this.SUBLOCATION = SUBLOCATION;
    }

    public String getTEL1() {
        return TEL1;
    }

    public void setTEL1(String TEL1) {
        this.TEL1 = TEL1;
    }

    public String getTEL2() {
        return TEL2;
    }

    public void setTEL2(String TEL2) {
        this.TEL2 = TEL2;
    }

    public String getTEL3() {
        return TEL3;
    }

    public void setTEL3(String TEL3) {
        this.TEL3 = TEL3;
    }

    public String getTEL4() {
        return TEL4;
    }

    public void setTEL4(String TEL4) {
        this.TEL4 = TEL4;
    }
}
