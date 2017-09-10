package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/8/17.
 */

public class DropDownAddressType extends RealmObject {

    @PrimaryKey
    @SerializedName("RowNumber")
    @Expose
    private int rowNumber;

    @SerializedName("AT_CODE")
    @Expose
    private String atCode;

    @SerializedName("AT_DESC")
    @Expose
    private String atDesc;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;


    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getAtCode() {
        return atCode;
    }

    public void setAtCode(String atCode) {
        this.atCode = atCode;
    }

    public String getAtDesc() {
        return atDesc;
    }

    public void setAtDesc(String atDesc) {
        this.atDesc = atDesc;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
