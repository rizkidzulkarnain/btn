package com.mitkoindo.smartcollection.network;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class RetrofitHelper {

    private RestServices services;

    public RetrofitHelper(Context context) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(RestConstants.BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()));

        services = builder.client(okHttpClientBuilder.build()).build().create(RestServices.class);
    }

    public RestServices getServices() {
        return services;
    }

}
