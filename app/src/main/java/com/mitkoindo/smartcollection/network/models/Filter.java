package com.mitkoindo.smartcollection.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/24/17.
 */


public class Filter {

    @SerializedName("Property")
    @Expose
    private String property;
    @SerializedName("Operator")
    @Expose
    private String operator;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

