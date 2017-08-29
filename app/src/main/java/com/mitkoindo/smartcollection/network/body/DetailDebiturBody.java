package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DetailDebiturBody {

    @SerializedName("DatabaseID")
    @Expose
    private String databaseId;

    @SerializedName("SpName")
    @Expose
    private String spName;

    @SerializedName("SpParameter")
    @Expose
    private SpParameter spParameter;


    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public void setSpName(String spName) {
        this.spName = spName;
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

        @Override
        public String toString() {
            return "SpParameter{" +
                    "nomorRekening='" + nomorRekening + '\'' +
                    '}';
        }
    }
}
