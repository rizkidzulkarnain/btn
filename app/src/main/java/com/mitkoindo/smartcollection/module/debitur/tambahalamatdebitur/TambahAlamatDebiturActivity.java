package com.mitkoindo.smartcollection.module.debitur.tambahalamatdebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityTambahAlamatDebiturBinding;


import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/31/17.
 */

public class TambahAlamatDebiturActivity extends BaseActivity {

    private static final String EXTRA_NAMA_DEBITUR = "extra_nama_debitur";
    private static final String EXTRA_NO_REKENING = "extra_no_rekening";

    private TambahAlamatDebiturViewModel mTambahAlamatDebiturViewModel;
    private ActivityTambahAlamatDebiturBinding mBinding;

    private String mNamaDebitur;
    private String mNoRekening;


    public static Intent instantiate(Context context, String namaDebitur, String noRekening) {
        Intent intent = new Intent(context, TambahAlamatDebiturActivity.class);
        intent.putExtra(EXTRA_NAMA_DEBITUR, namaDebitur);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tambah_alamat_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mTambahAlamatDebiturViewModel = new TambahAlamatDebiturViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setTambahAlamatDebiturViewModel(mTambahAlamatDebiturViewModel);
//        mBinding.setNamaDebitur(mNamaDebitur);
//        mBinding.setNomorRekening(mNoRekening);

        mTambahAlamatDebiturViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mTambahAlamatDebiturViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mTambahAlamatDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMenambahkanAlamatDanTelepon);
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNamaDebitur = getIntent().getExtras().getString(EXTRA_NAMA_DEBITUR);
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
        }
    }

    @OnClick(R.id.back_button)
    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}
