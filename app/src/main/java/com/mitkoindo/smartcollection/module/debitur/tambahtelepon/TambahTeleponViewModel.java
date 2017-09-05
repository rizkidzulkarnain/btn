package com.mitkoindo.smartcollection.module.debitur.tambahtelepon;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.TambahTeleponBody;
import com.mitkoindo.smartcollection.network.response.TambahTeleponResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ericwijaya on 9/3/17.
 */

public class TambahTeleponViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);

    public ObservableField<String> nama = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebiturId = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebitur = new ObservableField<>();
    public ObservableField<String> typeTeleponId = new ObservableField<>();
    public ObservableField<String> typeTelepon = new ObservableField<>();
    public ObservableField<String> telepon = new ObservableField<>();


    public TambahTeleponViewModel() {
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void tambahTelepon(String accessToken, String userId, String noRekening, String customerReference) {
        TambahTeleponBody tambahTeleponBody = new TambahTeleponBody();
        tambahTeleponBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        tambahTeleponBody.setSpName(RestConstants.TAMBAH_TELEPON_SP_NAME);

        TambahTeleponBody.SpParameter spParameter = new TambahTeleponBody.SpParameter();
        spParameter.setUserID(userId);
        spParameter.setCuRef(customerReference);
        spParameter.setAccNo(noRekening);
        spParameter.setContactType(typeTeleponId.get());
        spParameter.setContactNo(telepon.get());
        spParameter.setRelationship(hubunganDenganDebiturId.get());
        spParameter.setContactName(nama.get());

        tambahTeleponBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(accessToken).tambahTelepon(tambahTeleponBody)
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
                .subscribeWith(new DisposableObserver<List<TambahTeleponResponse>>() {
                    @Override
                    public void onNext(List<TambahTeleponResponse> listTambahTeleponResponse) {
                        obsIsSaveSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Log.e("TambahTeleponViewModel", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }
}
