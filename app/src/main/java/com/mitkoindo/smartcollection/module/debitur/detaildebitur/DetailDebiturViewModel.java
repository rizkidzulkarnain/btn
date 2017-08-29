package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.DetailDebiturBody;
import com.mitkoindo.smartcollection.network.body.ListPhoneNumberBody;
import com.mitkoindo.smartcollection.network.response.DetailDebiturResponse;
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

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    public static int GET_DETAIL_DEBITUR_ERROR = 0;
    public static int GET_PHONE_LIST_ERROR = 1;

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<DetailDebitur> obsDetailDebitur = new ObservableField<>();
    public ObservableField<List<PhoneNumber>> obsListPhoneNumber = new ObservableField<>();
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
                .subscribeWith(new DisposableObserver<DetailDebiturResponse>() {
                    @Override
                    public void onNext(DetailDebiturResponse detailDebiturResponse) {
                        obsDetailDebitur.set(detailDebiturResponse.getDetailDebitur());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorType = GET_DETAIL_DEBITUR_ERROR;
                        error.set(e);
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
