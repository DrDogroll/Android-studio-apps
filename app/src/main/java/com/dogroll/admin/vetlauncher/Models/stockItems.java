package com.dogroll.admin.vetlauncher.Models;

public class stockItems {
    public int id;
    private String vpmsCode, product, grouping, barcode, onHand;

    public stockItems(int id, String vpmsCode, String product, String grouping, String barcode, String onHand){
        this.id = id;
        this.vpmsCode = vpmsCode;
        this.product = product;
        this.grouping = grouping;
        this.barcode = barcode;
        this.onHand = onHand;
    }

    public stockItems() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVpmsCode() {
        return vpmsCode;
    }

    public void setVpmsCode(String vpmsCode) {
        this.vpmsCode = vpmsCode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOnHand() {
        return onHand;
    }

    public void setOnHand(String onHand) {
        this.onHand = onHand;
    }


}
