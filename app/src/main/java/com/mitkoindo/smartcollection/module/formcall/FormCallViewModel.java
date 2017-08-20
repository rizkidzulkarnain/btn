package com.mitkoindo.smartcollection.module.formcall;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.objectdata.FormCall;
import com.mitkoindo.smartcollection.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCallViewModel extends BaseObservable implements ILifecycleViewModel {

    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);

    private CompositeDisposable composites = new CompositeDisposable();
    public ObservableField<String> tujuan = new ObservableField<>();
    public ObservableField<String> tanggalHasilPanggilan = new ObservableField<>();
    public ObservableField<String> tanggalTindakLanjut = new ObservableField<>();

    private FormCall formCall = new FormCall();

    public FormCallViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void setTujuan(String tujuan) {
        this.tujuan.set(tujuan);
    }

    public void setTanggalHasilPanggilan(Date tanggalHasilPanggilan) {
        formCall.setTanggalHasilPanggilan(tanggalHasilPanggilan);
        this.tanggalHasilPanggilan.set(dateFormatter.format(formCall.getTanggalHasilPanggilan()));
    }

    public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
        formCall.setTanggalTindakLanjut(tanggalTindakLanjut);
        this.tanggalTindakLanjut.set(dateFormatter.format(formCall.getTanggalTindakLanjut()));
    }
}
