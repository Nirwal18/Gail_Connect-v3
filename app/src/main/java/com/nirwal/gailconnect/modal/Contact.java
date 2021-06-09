package com.nirwal.gailconnect.modal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Emp_no;
    private String Emp_Name;
    private String Designation;
    private String Department;
    private String Location;
    private String LGailTel;
    private String TelNo;
    private String MobileNo;
    private String MobileNo1;
    private String FaxNo;
    private String OfficeTel;
    private String OfficeExt;
    private String HBJExt;
    private String LTel;
    private String Emails;
    private String Grade;
    private String DateOfBirth;
    private String IMAGE;

    public Contact() {
    }

    public Contact( String emp_Name) {

        Emp_Name = emp_Name;
    }
    public Contact(String emp_no, String emp_Name, String designation, String department,
                   String location, String LGailTel, String telNo, String mobileNo,
                   String mobileNo1, String faxNo, String officeTel, String officeExt,
                   String HBJExt, String LTel, String emails, String grade, String dateOfBirth, String IMAGE) {
        Emp_no = emp_no;
        Emp_Name = emp_Name;
        Designation = designation;
        Department = department;
        Location = location;
        this.LGailTel = LGailTel;
        TelNo = telNo;
        MobileNo = mobileNo;
        MobileNo1 = mobileNo1;
        FaxNo = faxNo;
        OfficeTel = officeTel;
        OfficeExt = officeExt;
        this.HBJExt = HBJExt;
        this.LTel = LTel;
        Emails = emails;
        Grade = grade;
        DateOfBirth = dateOfBirth;
        this.IMAGE = IMAGE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmp_no() {
        return Emp_no;
    }

    public void setEmp_no(String emp_no) {
        Emp_no = emp_no;
    }

    public String getEmp_Name() {
        return Emp_Name;
    }

    public void setEmp_Name(String emp_Name) {
        Emp_Name = emp_Name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLGailTel() {
        return LGailTel;
    }

    public void setLGailTel(String LGailTel) {
        this.LGailTel = LGailTel;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getMobileNo1() {
        return MobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        MobileNo1 = mobileNo1;
    }

    public String getFaxNo() {
        return FaxNo;
    }

    public void setFaxNo(String faxNo) {
        FaxNo = faxNo;
    }

    public String getOfficeTel() {
        return OfficeTel;
    }

    public void setOfficeTel(String officeTel) {
        OfficeTel = officeTel;
    }

    public String getOfficeExt() {
        return OfficeExt;
    }

    public void setOfficeExt(String officeExt) {
        OfficeExt = officeExt;
    }

    public String getHBJExt() {
        return HBJExt;
    }

    public void setHBJExt(String HBJExt) {
        this.HBJExt = HBJExt;
    }

    public String getLTel() {
        return LTel;
    }

    public void setLTel(String LTel) {
        this.LTel = LTel;
    }

    public String getEmails() {
        return Emails;
    }

    public void setEmails(String emails) {
        Emails = emails;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.Emp_no);
        dest.writeString(this.Emp_Name);
        dest.writeString(this.Designation);
        dest.writeString(this.Department);
        dest.writeString(this.Location);
        dest.writeString(this.LGailTel);
        dest.writeString(this.TelNo);
        dest.writeString(this.MobileNo);
        dest.writeString(this.MobileNo1);
        dest.writeString(this.FaxNo);
        dest.writeString(this.OfficeTel);
        dest.writeString(this.OfficeExt);
        dest.writeString(this.HBJExt);
        dest.writeString(this.LTel);
        dest.writeString(this.Emails);
        dest.writeString(this.Grade);
        dest.writeString(this.DateOfBirth);
        dest.writeString(this.IMAGE);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.Emp_no = source.readString();
        this.Emp_Name = source.readString();
        this.Designation = source.readString();
        this.Department = source.readString();
        this.Location = source.readString();
        this.LGailTel = source.readString();
        this.TelNo = source.readString();
        this.MobileNo = source.readString();
        this.MobileNo1 = source.readString();
        this.FaxNo = source.readString();
        this.OfficeTel = source.readString();
        this.OfficeExt = source.readString();
        this.HBJExt = source.readString();
        this.LTel = source.readString();
        this.Emails = source.readString();
        this.Grade = source.readString();
        this.DateOfBirth = source.readString();
        this.IMAGE = source.readString();
    }

    protected Contact(Parcel in) {
        this.id = in.readInt();
        this.Emp_no = in.readString();
        this.Emp_Name = in.readString();
        this.Designation = in.readString();
        this.Department = in.readString();
        this.Location = in.readString();
        this.LGailTel = in.readString();
        this.TelNo = in.readString();
        this.MobileNo = in.readString();
        this.MobileNo1 = in.readString();
        this.FaxNo = in.readString();
        this.OfficeTel = in.readString();
        this.OfficeExt = in.readString();
        this.HBJExt = in.readString();
        this.LTel = in.readString();
        this.Emails = in.readString();
        this.Grade = in.readString();
        this.DateOfBirth = in.readString();
        this.IMAGE = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
