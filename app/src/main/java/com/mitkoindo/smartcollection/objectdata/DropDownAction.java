package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DropDownAction extends RealmObject {

    @PrimaryKey
    @SerializedName("RowNumber")
    @Expose
    private int rowNumber;

    @SerializedName("ACTION_ID")
    @Expose
    private String actionId;

    @SerializedName("ACTION_DESC")
    @Expose
    private String actionDesc;

    @SerializedName("ACTIVE")
    @Expose
    private String active;


    public DropDownAction() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
