package com.dogroll.admin.vetlauncher.Models;

public class bcsRecord {
    public int id;
    public String date, farm, herd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getHerd() {
        return herd;
    }

    public void setHerd(String herd) {
        this.herd = herd;
    }
    public bcsRecord(){}
}
