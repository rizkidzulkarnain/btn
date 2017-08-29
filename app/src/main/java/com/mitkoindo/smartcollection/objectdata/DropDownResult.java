package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DropDownResult extends RealmObject {

    @SerializedName("RowNumber")
    @Expose
    @PrimaryKey
    private Integer rowNumber;
    @SerializedName("RESULT_ID")
    @Expose
    private String resultId;
    @SerializedName("RESULT_DESC")
    @Expose
    private String resultDesc;
    @SerializedName("ACTIVE")
    @Expose
    private Boolean active;
    @SerializedName("RESULT_MAXDAY")
    @Expose
    private Integer resultMaxDay;


    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getResultMaxDay() {
        return resultMaxDay;
    }

    public void setResultMaxDay(Integer resultMaxDay) {
        this.resultMaxDay = resultMaxDay;
    }
}
