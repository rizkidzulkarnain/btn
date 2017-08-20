package com.mitkoindo.smartcollection.network;

import com.mitkoindo.smartcollection.network.body.LoginBody;
import com.mitkoindo.smartcollection.network.response.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ericwijaya on 8/20/17.
 */

public interface RestServices {

    @POST(RestConstants.LOGIN)
    Observable<LoginResponse> login(@Body LoginBody loginBody);

}
