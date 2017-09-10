package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DropDownPurpose extends RealmObject {

    @SerializedName("RowNumber")
    @Expose
    @PrimaryKey
    private int rowNumber;

    @SerializedName("P_ID")
    @Expose
    private String pId;

    @SerializedName("P_DESC")
    @Expose
    private String pDesc;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;


    public DropDownPurpose() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPDesc() {
        return pDesc;
    }

    public void setPDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
