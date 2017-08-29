package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityListDebiturBinding;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class ListDebiturActivity extends BaseActivity {

    private ActivityListDebiturBinding mBinding;
    private ListDebiturViewPagerAdapter mViewPagerAdapter;
    private ViewPager.SimpleOnPageChangeListener mPageChangeListener;


    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, ListDebiturActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.ListDebitur_PageTitle));
        setupViewPager();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mBinding = DataBindingUtil.bind(contentView);
    }

    private void setupViewPager() {
        mViewPagerAdapter = new ListDebiturViewPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setOffscreenPageLimit(4);
        mBinding.viewPager.setAdapter(mViewPagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        if (mPageChangeListener != null) {
            mBinding.viewPager.removeOnPageChangeListener(mPageChangeListener);
        }
        mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        };
        mBinding.viewPager.addOnPageChangeListener(mPageChangeListener);
    }
}
