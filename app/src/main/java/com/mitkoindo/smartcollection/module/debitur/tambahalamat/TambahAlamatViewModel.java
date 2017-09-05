package com.mitkoindo.smartcollection.module.debitur.tambahalamat;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.TambahAlamatBody;
import com.mitkoindo.smartcollection.network.body.TambahTeleponBody;
import com.mitkoindo.smartcollection.network.response.TambahAlamatResponse;
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
 * Created by ericwijaya on 9/4/17.
 */

public class TambahAlamatViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);

    public ObservableField<String> nama = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebiturId = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebitur = new ObservableField<>();
    public ObservableField<String> alamat1 = new ObservableField<>();
    public ObservableField<String> alamat2 = new ObservableField<>();
    public ObservableField<String> alamat3 = new ObservableField<>();


    public TambahAlamatViewModel() {
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void tambahAlamat(String accessToken, String userId, String noRekening, String customerReference) {
        TambahAlamatBody tambahAlamatBody = new TambahAlamatBody();
        tambahAlamatBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        tambahAlamatBody.setSpName(RestConstants.TAMBAH_ALAMAT_SP_NAME);

        TambahAlamatBody.SpParameter spParameter = new TambahAlamatBody.SpParameter();
        spParameter.setUserID(userId);
        spParameter.setCuRef(customerReference);
        spParameter.setContactName(nama.get());
        spParameter.setRelationship(hubunganDenganDebiturId.get());
        spParameter.setAddressType("02");
        spParameter.setAddress1(alamat1.get());
        spParameter.setAddress2(alamat2.get());
        spParameter.setAddress3(alamat3.get());

        tambahAlamatBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(accessToken).tambahAlamat(tambahAlamatBody)
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
                .subscribeWith(new DisposableObserver<List<TambahAlamatResponse>>() {
                    @Override
                    public void onNext(List<TambahAlamatResponse> listTambahAlamatResponse) {
                        obsIsSaveSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Log.e("TambahAlamatViewModel", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }
}
