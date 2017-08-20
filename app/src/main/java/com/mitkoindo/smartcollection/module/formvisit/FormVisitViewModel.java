package com.mitkoindo.smartcollection.module.formvisit;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.objectdata.FormVisit;
import com.mitkoindo.smartcollection.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class FormVisitViewModel extends BaseObservable implements ILifecycleViewModel {

    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);

    private CompositeDisposable composites = new CompositeDisposable();
    public ObservableField<String> tujuanKunjungan = new ObservableField<>();
    public ObservableField<String> tanggalJanjiDebitur = new ObservableField<>();
    public ObservableField<String> tanggalTindakLanjut = new ObservableField<>();
    public ObservableField<Boolean> isFotoAgunan2Show = new ObservableField<>();

    private FormVisit formVisit = new FormVisit();

    public FormVisitViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void setTujuanKunjungan(String tujuanKunjungan) {
        this.tujuanKunjungan.set(tujuanKunjungan);
    }

    public void setTanggalJanjiDebitur(Date tanggalJanjiDebitur) {
        formVisit.setTanggalJanjiDebitur(tanggalJanjiDebitur);
        this.tanggalJanjiDebitur.set(dateFormatter.format(formVisit.getTanggalJanjiDebitur()));
    }

    public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
        formVisit.setTanggalTindakLanjut(tanggalTindakLanjut);
        this.tanggalTindakLanjut.set(dateFormatter.format(formVisit.getTanggalTindakLanjut()));
    }


}
