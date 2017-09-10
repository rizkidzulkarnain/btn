package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DropDownRelationship extends RealmObject {

    @SerializedName("RowNumber")
    @Expose
    @PrimaryKey
    private int rowNumber;

    @SerializedName("REL_ID")
    @Expose
    private String relId;

    @SerializedName("REL_DESC")
    @Expose
    private String relDesc;

    @SerializedName("CD_SIBS")
    @Expose
    private String cdsibs;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;

    @SerializedName("URUTAN")
    @Expose
    private int urutan;


    public DropDownRelationship() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getRelDesc() {
        return relDesc;
    }

    public void setRelDesc(String relDesc) {
        this.relDesc = relDesc;
    }

    public String getCdsibs() {
        return cdsibs;
    }

    public void setCdsibs(String cdsibs) {
        this.cdsibs = cdsibs;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getUrutan() {
        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

}
