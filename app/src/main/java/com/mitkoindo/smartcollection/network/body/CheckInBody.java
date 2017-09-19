package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 9/2/17.
 */

public class CheckInBody {

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

        @SerializedName("userID")
        @Expose
        private String userID;

        @SerializedName("latitude")
        @Expose
        private Double latitude;

        @SerializedName("longitude")
        @Expose
        private Double longitude;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("isCheckin")
        @Expose
        private String isCheckin;


        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIsCheckin() {
            return isCheckin;
        }

        public void setIsCheckin(String isCheckin) {
            this.isCheckin = isCheckin;
        }
    }
}
