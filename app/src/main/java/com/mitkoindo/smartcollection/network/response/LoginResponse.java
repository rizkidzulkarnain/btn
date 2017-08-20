package com.mitkoindo.smartcollection.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class LoginResponse {

    @SerializedName("AccessToken")
    @Expose
    private String accessToken;
}
