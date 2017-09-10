package com.mitkoindo.smartcollection.module.formcall;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.network.response.FormCallResponse;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormCallDb;
import com.mitkoindo.smartcollection.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCallViewModel extends BaseObservable implements ILifecycleViewModel {

    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatterDisplay = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);
    private SimpleDateFormat dateFormatterSend = new SimpleDateFormat(Constant.DATE_FORMAT_SEND_DATE, id);

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowTanggalJanjiDebitur = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowJumlahYangAkanDisetor = new ObservableBoolean(false);

    public ObservableField<String> tujuan = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebitur = new ObservableField<>();
    public ObservableField<String> hasilPanggilan = new ObservableField<>();
    public ObservableField<String> alasanTidakBayar = new ObservableField<>();
    public ObservableField<String> tindakLanjut = new ObservableField<>();
    public ObservableField<String> jumlahYangAkanDisetor = new ObservableField<>();
    public ObservableField<String> tanggalHasilPanggilan = new ObservableField<>();
    public ObservableField<String> tanggalTindakLanjut = new ObservableField<>();

    public FormCallBody.SpParameterFormCall spParameterFormCall = new FormCallBody.SpParameterFormCall();

    public FormCallViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void setTanggalHasilPanggilan(Date tanggalHasilPanggilan) {
        spParameterFormCall.setResultDate(dateFormatterSend.format(tanggalHasilPanggilan));
        this.tanggalHasilPanggilan.set(dateFormatterDisplay.format(tanggalHasilPanggilan));
    }

    public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
        spParameterFormCall.setDateAction(dateFormatterSend.format(tanggalTindakLanjut));
        this.tanggalTindakLanjut.set(dateFormatterDisplay.format(tanggalTindakLanjut));
    }

    public void saveFormCall(String accessToken) {
        Disposable disposable = ApiUtils.getRestServices(accessToken).saveFormCall(createFormCallBody())
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
                .subscribeWith(new DisposableObserver<List<FormCallResponse>>() {
                    @Override
                    public void onNext(List<FormCallResponse> listFormCall) {
                        obsIsSaveSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        SpParameterFormCallDb spParameterFormCallDb = new SpParameterFormCallDb(spParameterFormCall);
                        RealmHelper.storeFormCall(spParameterFormCallDb);
                        Log.e("FormCallViewModel", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private FormCallBody createFormCallBody() {
        FormCallBody formCallBody = new FormCallBody();
        formCallBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formCallBody.setSpName(RestConstants.FORM_CALL_SP_NAME);
        formCallBody.setSpParameterFormCall(spParameterFormCall);

        return formCallBody;
    }
}
