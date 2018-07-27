package my.edu.tarc.foodorderingapp;

import java.io.Serializable;

/**
 * Created by SX on 2018-07-28.
 */

public class Staff implements Serializable {

    public String staffPic;
    public  String staffName;
    public String dob,position,ic,gender,contact,email,status, id;

    public Staff(String id, String staffPic, String staffName, String dob, String position, String ic, String gender, String contact, String email, String status) {
        this.id = id;
        this.staffPic = staffPic;
        this.staffName = staffName;
        this.dob = dob;
        this.position = position;
        this.ic = ic;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffPic() {
        return staffPic;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getDob() {
        return dob;
    }

    public String getPosition() {
        return position;
    }

    public String getIc() {
        return ic;
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setStaffPic(String staffPic) {
        this.staffPic = staffPic;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
