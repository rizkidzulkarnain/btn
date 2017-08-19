package com.mitkoindo.smartcollection.module.visitform;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormVisitBinding;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class FormVisitActivity extends BaseActivity {

    private FormVisitViewModel mFormVisitViewModel;
    private ActivityFormVisitBinding mBinding;

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, FormVisitActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormVisit_PageTitle));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_visit;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mFormVisitViewModel = new FormVisitViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormVisitViewModel(mFormVisitViewModel);
    }
}
