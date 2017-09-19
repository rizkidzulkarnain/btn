package com.mitkoindo.smartcollection.module.laporan.reportdistribusi;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityReportDistribusiDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;

/**
 * Created by ericwijaya on 9/16/17.
 */

public class ReportDistribusiDebiturActivity extends BaseActivity {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private ActivityReportDistribusiDebiturBinding mBinding;

    private String mUserId;

    public static Intent instantiate(Context context, String userId) {
        Intent intent = new Intent(context, ReportDistribusiDebiturActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.ReportDistribusiDebitur_PageTitle));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_report_distribusi_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mBinding = DataBindingUtil.bind(contentView);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_layout_content);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.frame_layout_content, ReportDistribusiDebiturFragment.getInstance(ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE, mUserId))
                    .commit();
        }
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mUserId = getIntent().getExtras().getString(EXTRA_USER_ID);
        }
    }
}
