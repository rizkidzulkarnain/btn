package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DropDownReason extends RealmObject {

    @PrimaryKey
    @SerializedName("RowNumber")
    @Expose
    private int rowNumber;

    @SerializedName("REASON_ID")
    @Expose
    private String reasonId;

    @SerializedName("REASON_DESC")
    @Expose
    private String reasonDesc;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;


    public DropDownReason() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
