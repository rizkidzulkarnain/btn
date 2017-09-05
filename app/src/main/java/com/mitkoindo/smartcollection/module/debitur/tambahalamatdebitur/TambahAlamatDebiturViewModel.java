package com.mitkoindo.smartcollection.module.debitur.tambahalamatdebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.CheckInBody;
import com.mitkoindo.smartcollection.network.response.CheckInResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ericwijaya on 8/31/17.
 */

public class TambahAlamatDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();

    public ObservableField<String> teleponBaru = new ObservableField<>();
    public ObservableField<String> alamatBaru = new ObservableField<>();


    public TambahAlamatDebiturViewModel() {
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

//    public void tambahTelepon(String userId, double latitude, double longitude, String address) {
//        CheckInBody checkInBody = new CheckInBody();
//        checkInBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
//        checkInBody.setSpName(RestConstants.CHECK_IN_SP_NAME);
//
//        CheckInBody.SpParameter spParameter = new CheckInBody.SpParameter();
//        spParameter.setUserID(userId);
//        spParameter.setLatitude(latitude);
//        spParameter.setLongitude(longitude);
//        spParameter.setAddress(address);
//
//        checkInBody.setSpParameter(spParameter);
//
//        Disposable disposable = ApiUtils.getRestServices(mAccessToken).checkIn(checkInBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        obsIsLoading.set(true);
//                    }
//                })
//                .doOnTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        obsIsLoading.set(false);
//                    }
//                })
//                .subscribeWith(new DisposableObserver<List<CheckInResponse>>() {
//                    @Override
//                    public void onNext(List<CheckInResponse> listCheckInResponse) {
//                        obsCheckInSuccess.set(true);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mErrorType = CHECK_IN_ERROR;
//                        error.set(e);
//                        Log.e("DetailDebiturViewModel", "checkIn" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//        composites.add(disposable);
//    }
}
