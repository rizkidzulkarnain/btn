package com.mitkoindo.smartcollection.module.formcall;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormCallBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCallActivity extends BaseActivity {

    private FormCallViewModel mFormCallViewModel;
    private ActivityFormCallBinding mBinding;

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, FormCallActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormCall_PageTitle));
        initSpinner();
        initNoSelectValue();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_call;
    }


    @Override
    protected void setupDataBinding(View contentView) {
        mFormCallViewModel = new FormCallViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormCallViewModel(mFormCallViewModel);
    }

    private void initSpinner() {
        List<String> listTujuanCall = new ArrayList<String>();
        listTujuanCall.add(getString(R.string.FormVisit_MengingatkanAngsuran));
        listTujuanCall.add(getString(R.string.FormVisit_PenagihanKredit));
        listTujuanCall.add(getString(R.string.FormVisit_Konfirmasi));
        listTujuanCall.add(getString(R.string.FormVisit_IdentifikasiBarangJaminan));
        listTujuanCall.add(getString(R.string.FormVisit_PemasanganPlakatPengawasanBank));
        listTujuanCall.add(getString(R.string.FormVisit_PemasanganPlakatDijual));
        listTujuanCall.add(getString(R.string.FormVisit_PemasanganPlakatDilelang));
        listTujuanCall.add(getString(R.string.FormVisit_LainLain));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listTujuanCall);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerTujuan.setAdapter(dataAdapter);


        List<String> listHubunganDenganDebitur = new ArrayList<String>();
        listHubunganDenganDebitur.add("Yang bersangkutan");
        listHubunganDenganDebitur.add("Suami");
        listHubunganDenganDebitur.add("Istri");
        listHubunganDenganDebitur.add("Orang tua");
        listHubunganDenganDebitur.add("Mertua");
        listHubunganDenganDebitur.add("Anak");
        listHubunganDenganDebitur.add("Kakak kandung");
        listHubunganDenganDebitur.add("Adik kandung");
        listHubunganDenganDebitur.add("Kakek / Nenek kandung");
        listHubunganDenganDebitur.add("Kakek / Nenek suami / istri");

        ArrayAdapter<String> dataAdapterHubunganDenganDebitur = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listHubunganDenganDebitur);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerHubunganDenganDebitur.setAdapter(dataAdapterHubunganDenganDebitur);


        List<String> listHasilPanggilan = new ArrayList<String>();
        listHasilPanggilan.add("Tanpa hasil");
        listHasilPanggilan.add("Telepon kembali");
        listHasilPanggilan.add("Janji bayar");
        listHasilPanggilan.add("Tidak ada jawaban");
        listHasilPanggilan.add("Titip pesan");
        listHasilPanggilan.add("Sudah bayar");
        listHasilPanggilan.add("Salah sambung");
        listHasilPanggilan.add("Tidak ada/salah on HP");
        listHasilPanggilan.add("Tidak mau bayar");
        listHasilPanggilan.add("Tidak jelas bagian / department");

        ArrayAdapter<String> dataAdapterHasilPanggilan = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listHasilPanggilan);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerHasilPanggilan.setAdapter(dataAdapterHasilPanggilan);


        List<String> listAlasanTidakBayar = new ArrayList<String>();
        listAlasanTidakBayar.add("Informasi kredit kurang");
        listAlasanTidakBayar.add("Sedang diluar luar kota / negeri");
        listAlasanTidakBayar.add("Kesalahan/keterlambatan transfer");
        listAlasanTidakBayar.add("Fasilitas pembayaran bermasalah");

        ArrayAdapter<String> dataAdapterAlasanTidakBayar = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listAlasanTidakBayar);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerAlasanTidakBayar.setAdapter(dataAdapterAlasanTidakBayar);


        List<String> listTindakLanjut = new ArrayList<String>();
        listTindakLanjut.add("Surat");
        listTindakLanjut.add("Litigasi");
        listTindakLanjut.add("Restrukturisasi Kredit");
        listTindakLanjut.add("Klaim Asuransi");

        ArrayAdapter<String> dataAdapterTindakLanjut = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listTindakLanjut);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerTindakLanjut.setAdapter(dataAdapterTindakLanjut);

    }

    private void initNoSelectValue() {
        mFormCallViewModel.tujuan.set(getString(R.string.FormCall_TujuanCallInitial));
        mFormCallViewModel.tanggalHasilPanggilan.set(getString(R.string.FormCall_TanggalHasilPanggilanInitial));
        mFormCallViewModel.tanggalTindakLanjut.set(getString(R.string.FormCall_TanggalTindakLanjutInitial));
    }

    @OnClick(R.id.card_view_tanggal_hasil_panggilan)
    public void onTanggalHasilPanggilanClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFormCallViewModel.setTanggalHasilPanggilan(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    @OnClick(R.id.card_view_tanggal_tindak_lanjut)
    public void onTanggalTindakLanjutClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFormCallViewModel.setTanggalTindakLanjut(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.FormCall_KonfirmasiSubmit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(HomeActivity.instantiateClearTask(FormCallActivity.this));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

}
