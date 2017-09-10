package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by ericwijaya on 9/3/17.
 */

public class DropDownTeleponType extends RealmObject {

    @SerializedName("RowNumber")
    @Expose
    private int rowNumber;

    @SerializedName("CT_CODE")
    @Expose
    private String ctCode;

    @SerializedName("CT_DESC")
    @Expose
    private String ctDesc;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;


    public DropDownTeleponType() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getCtCode() {
        return ctCode;
    }

    public void setCtCode(String ctCode) {
        this.ctCode = ctCode;
    }

    public String getCtDesc() {
        return ctDesc;
    }

    public void setCtDesc(String ctDesc) {
        this.ctDesc = ctDesc;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
