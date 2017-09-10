package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.CheckInBody;
import com.mitkoindo.smartcollection.network.body.DetailDebiturBody;
import com.mitkoindo.smartcollection.network.body.ListPhoneNumberBody;
import com.mitkoindo.smartcollection.network.response.CheckInResponse;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    public static int GET_DETAIL_DEBITUR_ERROR = 0;
    public static int GET_PHONE_LIST_ERROR = 1;
    public static int CHECK_IN_ERROR = 2;

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<DetailDebitur> obsDetailDebitur = new ObservableField<>();
    public ObservableField<List<PhoneNumber>> obsListPhoneNumber = new ObservableField<>();
    public ObservableBoolean obsCheckInSuccess = new ObservableBoolean();
    public ObservableBoolean obsIsPhoneEmpty = new ObservableBoolean(false);
    public int mErrorType;

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public DetailDebiturViewModel(String accessToken) {
        mAccessToken = accessToken;
    }

    public void getDetailDebitur(String nomorRekening) {
        DetailDebiturBody detailDebiturBody = new DetailDebiturBody();
        detailDebiturBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        detailDebiturBody.setSpName(RestConstants.DETAIL_DEBITUR_SP_NAME);

        DetailDebiturBody.SpParameter spParameter = new DetailDebiturBody.SpParameter();
        spParameter.setNomorRekening(nomorRekening);

        detailDebiturBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getDetailDebitur(detailDebiturBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        obsIsLoading.set(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<DetailDebitur>>() {
                    @Override
                    public void onNext(List<DetailDebitur> listDetailDebitur) {
                        if (listDetailDebitur.size() > 0) {
                            obsDetailDebitur.set(listDetailDebitur.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorType = GET_DETAIL_DEBITUR_ERROR;
                        error.set(e);
                        Timber.e("getDetailDebitur " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void getPhoneList(String nomorRekening) {
        ListPhoneNumberBody listPhoneNumberBody = new ListPhoneNumberBody();
        listPhoneNumberBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        listPhoneNumberBody.setSpName(RestConstants.LIST_PHONE_NUMBER_SP_NAME);

        ListPhoneNumberBody.SpParameter spParameter = new ListPhoneNumberBody.SpParameter();
        spParameter.setNomorRekening(nomorRekening);

        listPhoneNumberBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListPhoneNumber(listPhoneNumberBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        obsIsLoading.set(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<PhoneNumber>>() {
                    @Override
                    public void onNext(List<PhoneNumber> listPhoneNumber) {
                        obsListPhoneNumber.set(listPhoneNumber);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorType = GET_PHONE_LIST_ERROR;
                        error.set(e);
                        Timber.e("getPhoneList " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void checkIn(String userId, double latitude, double longitude, String address) {
        CheckInBody checkInBody = new CheckInBody();
        checkInBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        checkInBody.setSpName(RestConstants.CHECK_IN_SP_NAME);

        CheckInBody.SpParameter spParameter = new CheckInBody.SpParameter();
        spParameter.setUserID(userId);
        spParameter.setLatitude(latitude);
        spParameter.setLongitude(longitude);
        spParameter.setAddress(address);

        checkInBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).checkIn(checkInBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        obsIsLoading.set(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<CheckInResponse>>() {
                    @Override
                    public void onNext(List<CheckInResponse> listCheckInResponse) {
                        obsCheckInSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorType = CHECK_IN_ERROR;
                        error.set(e);
                        Timber.e("checkIn " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
