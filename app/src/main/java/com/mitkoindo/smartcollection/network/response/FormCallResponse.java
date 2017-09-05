package com.mitkoindo.smartcollection.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 9/1/17.
 */

public class FormCallResponse {

    @SerializedName("ResponseCode")
    @Expose
    private int responseCode;
    @SerializedName("Message")
    @Expose
    private String message;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
