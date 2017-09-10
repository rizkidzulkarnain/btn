package com.mitkoindo.smartcollection.network;

import com.mitkoindo.smartcollection.network.body.CheckInBody;
import com.mitkoindo.smartcollection.network.body.DetailDebiturBody;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.body.FormVisitDropDownBody;
import com.mitkoindo.smartcollection.network.body.ListDebiturBody;
import com.mitkoindo.smartcollection.network.body.ListPhoneNumberBody;
import com.mitkoindo.smartcollection.network.body.LoginBody;
import com.mitkoindo.smartcollection.network.body.TambahAlamatBody;
import com.mitkoindo.smartcollection.network.body.TambahTeleponBody;
import com.mitkoindo.smartcollection.network.response.CheckInResponse;
import com.mitkoindo.smartcollection.network.response.FormCallResponse;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.LoginResponse;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.network.response.TambahAlamatResponse;
import com.mitkoindo.smartcollection.network.response.TambahTeleponResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressType;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.DropDownTeleponType;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by ericwijaya on 8/20/17.
 */

public interface RestServices {

    @POST(RestConstants.ENDPOINT_LOGIN)
    Observable<LoginResponse> login(@Body LoginBody loginBody);

    @POST(RestConstants.ENDPOINT_LIST_DEBITUR)
    Observable<List<DebiturItem>> getListDebitur(@Body ListDebiturBody listDebiturBody);

    @POST(RestConstants.ENDPOINT_DETAIL_DEBITUR)
    Observable<List<DetailDebitur>> getDetailDebitur(@Body DetailDebiturBody detailDebiturBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownPurpose>> getDropDownPurpose(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownRelationship>> getDropDownRelationship(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownResult>> getDropDownResult(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownReason>> getDropDownReason(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownAction>> getDropDownAction(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownStatusAgunan>> getDropDownStatusAgunan(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownAddress>> getDropDownAddress(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownTeleponType>> getDropDownTeleponType(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.ENDPOINT_VISIT_DROPDOWN)
    Observable<List<DropDownAddressType>> getDropDownAddressType(@Body FormVisitDropDownBody formVisitDropDownBody);

    @POST(RestConstants.FORM_VISIT)
    Observable<List<FormVisitResponse>> saveFormVisit(@Body FormVisitBody formVisitBody);

    @POST(RestConstants.FORM_CALL)
    Observable<List<FormCallResponse>> saveFormCall(@Body FormCallBody formCallBody);

    @POST(RestConstants.ENDPOINT_LIST_PHONE_NUMBER)
    Observable<List<PhoneNumber>> getListPhoneNumber(@Body ListPhoneNumberBody listPhoneNumberBody);

    @POST(RestConstants.DATA_SP)
    Observable<List<CheckInResponse>> checkIn(@Body CheckInBody checkInBody);

    @POST(RestConstants.DATA_SP)
    Observable<List<TambahTeleponResponse>> tambahTelepon(@Body TambahTeleponBody tambahTeleponBody);

    @POST(RestConstants.DATA_SP)
    Observable<List<TambahAlamatResponse>> tambahAlamat(@Body TambahAlamatBody tambahAlamatBody);

    @GET(RestConstants.BUNDLE)
    Observable<OfflineBundleResponse> getBundle(@Path("dbName") String dbName, @Path("limit") String limit);


    @Headers({"Content-Type: multipart/form-data"})
    @Multipart
    @POST(RestConstants.ENDPOINT_UPLOAD_FILE)
    Observable<MultipartResponse> uploadFile(@Part MultipartBody.Part file);
//    @Part("description") RequestBody description,

}
