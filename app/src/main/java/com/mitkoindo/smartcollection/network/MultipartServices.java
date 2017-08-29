package com.mitkoindo.smartcollection.network;

import com.mitkoindo.smartcollection.network.response.MultipartResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by ericwijaya on 8/26/17.
 */

public interface MultipartServices {

    @Multipart
    @POST(RestConstants.ENDPOINT_UPLOAD_FILE)
    Observable<MultipartResponse> uploadFile(@Part MultipartBody.Part file);
//    @Part("description") RequestBody description,
}
