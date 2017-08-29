package com.mitkoindo.smartcollection.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class Sort {

    @SerializedName("Property")
    @Expose
    private String property;
    @SerializedName("Direction")
    @Expose
    private String direction;


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
