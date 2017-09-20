package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 9/19/17.
 */

public class GalleryBody {

    @SerializedName("DatabaseID")
    @Expose
    private String databaseId;

    @SerializedName("SpName")
    @Expose
    private String spName;

    @SerializedName("SpParameter")
    @Expose
    private SpParameter spParameter;


    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public SpParameter getSpParameter() {
        return spParameter;
    }

    public void setSpParameter(SpParameter spParameter) {
        this.spParameter = spParameter;
    }


    public static class SpParameter {

        @SerializedName("accNo")
        @Expose
        private String accNo;

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }
    }
}
