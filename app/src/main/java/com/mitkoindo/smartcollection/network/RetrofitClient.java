package com.mitkoindo.smartcollection.network;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class RetrofitClient {
    private static Retrofit sRetrofit = null;

    public static Retrofit getClient(String accessToken) {
        if (sRetrofit == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
            okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
            okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request()
                            .newBuilder()
                            .header("Content-Type", "application/json")
                            .header("PhoneSpec", "");

                    if (!TextUtils.isEmpty(accessToken)) {
                        requestBuilder.header("Authorization", "Bearer " + accessToken);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(RestConstants.BASE_URL);
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()));

            sRetrofit = builder.client(okHttpClientBuilder.build()).build();
        }

        return sRetrofit;
    }
}
