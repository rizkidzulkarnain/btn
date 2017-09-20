package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityGalleryBinding;

import retrofit2.HttpException;


/**
 * Created by ericwijaya on 9/19/17.
 */

public class GalleryActivity extends BaseActivity {

    public static final String EXTRA_NO_REKENING = "extra_no_rekening";

    private GalleryViewModel mGalleryViewModel;
    private ActivityGalleryBinding mBinding;

    private String mNoRekening = "";

    public static Intent instantiate(Context context, String noRekening) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.Gallery_PageTitle));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mGalleryViewModel = new GalleryViewModel(getAccessToken());
        addViewModel(mGalleryViewModel);
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setGalleryViewModel(mGalleryViewModel);

        mGalleryViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mGalleryViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mGalleryViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mGalleryViewModel.error.get() instanceof HttpException) {
                    if (((HttpException) mGalleryViewModel.error.get()).code() == 404) {
                        displayMessage(R.string.TidakAdaDataFoto);
                        mGalleryViewModel.obsIsEmpty.set(true);
                    }
                }
            }
        });
        mGalleryViewModel.obsListGalleryItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mGalleryViewModel.obsIsEmpty.set(false);
            }
        });

        mGalleryViewModel.getGallery(mNoRekening);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
        }
    }
}
