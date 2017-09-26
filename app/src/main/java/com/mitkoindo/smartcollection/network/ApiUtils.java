package com.mitkoindo.smartcollection.network;

/**
 * Created by ericwijaya on 8/21/17.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static RestServices getRestServices(String accessToken) {
        return RetrofitClient.getClient(accessToken).create(RestServices.class);
    }

    public static MultipartServices getMultipartServices(String accessToken) {
        return MultipartClient.getClient(accessToken).create(MultipartServices.class);
    }

    public static void resetServices() {
        RetrofitClient.resetRetrofitClient();
        MultipartClient.resetRetrofitMultipartClient();
    }
}

