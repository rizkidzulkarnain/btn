package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class ListPhoneNumberBody {

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
        private String nomorRekening;

        public String getNomorRekening() {
            return nomorRekening;
        }

        public void setNomorRekening(String nomorRekening) {
            this.nomorRekening = nomorRekening;
        }
    }
}
