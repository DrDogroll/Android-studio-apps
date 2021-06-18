package com.dogroll.admin.vetlauncher.Models;

public class client {
    public int id;
    public String farmName, dairyNumber, contactName, number, address;

    public client (int id, String farmName, String dairyNumber, String contactName, String number, String address){
        this.id = id;
        this.farmName = farmName;
        this.dairyNumber = dairyNumber;
        this.contactName = contactName;
        this.number = number;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getDairyNumber() {
        return dairyNumber;
    }

    public void setDairyNumber(String dairyNumber) {
        this.dairyNumber = dairyNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public client(){}
}
