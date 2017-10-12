package com.mitkoindo.smartcollection.network;

import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
            okHttpClientBuilder.readTimeout(180, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(180, TimeUnit.SECONDS);
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
//            okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);

                SSLContext sslContext = SSLContext.getInstance("TLS");

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
                sslContext.init(null, trustAllCerts, new SecureRandom());

                okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory())
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
            } catch (Exception e) {}


            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(RestConstants.BASE_URL);
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()));

            sRetrofit = builder.client(okHttpClientBuilder.build()).build();
        }

        return sRetrofit;
    }

    public static void resetRetrofitClient() {
        sRetrofit = null;
    }
}
