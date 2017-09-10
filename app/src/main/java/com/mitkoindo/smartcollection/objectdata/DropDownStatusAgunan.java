package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class DropDownStatusAgunan extends RealmObject {

    @PrimaryKey
    @SerializedName("RowNumber")
    @Expose
    private int rowNumber;

    @SerializedName("COLSTA_CODE")
    @Expose
    private String colstaCode;

    @SerializedName("COL_TYPE")
    @Expose
    private String colType;

    @SerializedName("COLSTA_DESC")
    @Expose
    private String colstaDesc;

    @SerializedName("CORE_CODE")
    @Expose
    private String coreCode;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;


    public DropDownStatusAgunan() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getColstaCode() {
        return colstaCode;
    }

    public void setColstaCode(String colstaCode) {
        this.colstaCode = colstaCode;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getColstaDesc() {
        return colstaDesc;
    }

    public void setColstaDesc(String colstaDesc) {
        this.colstaDesc = colstaDesc;
    }

    public String getCoreCode() {
        return coreCode;
    }

    public void setCoreCode(String coreCode) {
        this.coreCode = coreCode;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
