package com.mitkoindo.smartcollection.module.laporan.staffproductivity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityStaffProductivityDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturViewModel;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;
import com.mitkoindo.smartcollection.utils.Utils;


/**
 * Created by ericwijaya on 9/18/17.
 */

public class StaffProductivityDebiturActivity extends BaseActivity {

    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String EXTRA_ACTION_DATE = "extra_action_date";
    private static final String EXTRA_TIME_RANGE = "extra_time_range";
    private static final String EXTRA_USER_NAME = "extra_user_name";

    private ListDebiturViewModel mListDebiturViewModel;
    private ActivityStaffProductivityDebiturBinding mBinding;

    private FastItemAdapter mFastAdapter;

    private String mUserId;
    private String mActionDate;
    private String mTimeRange;
    private String mUserName;
    private String mDateFormatted;
    private String mTimeRangeFormatted;


    public static Intent instantiate(Context context, String userId, String actionDate, String timeRange, String userName) {
        Intent intent = new Intent(context, StaffProductivityDebiturActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_ACTION_DATE, actionDate);
        intent.putExtra(EXTRA_TIME_RANGE, timeRange);
        intent.putExtra(EXTRA_USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.StaffProductivityDebitur_PageTitle));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_staff_productivity_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mListDebiturViewModel = new ListDebiturViewModel(getAccessToken());
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setListDebiturVideModel(mListDebiturViewModel);

        mListDebiturViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutDebitur.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mListDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
//                displayMessage(R.string.GagalMendapatkanData);
                mFastAdapter.clear();
                mListDebiturViewModel.obsIsEmpty.set(true);
            }
        });
        mListDebiturViewModel.obsDebiturResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.clear();
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponse.get());
                mBinding.recyclerViewDebitur.getLayoutManager().scrollToPosition(0);
                if (mListDebiturViewModel.obsDebiturResponse.get().size() > 0) {
                    mListDebiturViewModel.obsIsEmpty.set(false);
                } else {
                    mListDebiturViewModel.obsIsEmpty.set(true);
                }
            }
        });

        setupHeader();
        setupRecyclerView();

        mListDebiturViewModel.getListDebiturStaffProductivity(mUserId, mActionDate, mTimeRange);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mUserId = getIntent().getExtras().getString(EXTRA_USER_ID);
            mActionDate = getIntent().getExtras().getString(EXTRA_ACTION_DATE);
            mTimeRange = getIntent().getExtras().getString(EXTRA_TIME_RANGE);
            mUserName = getIntent().getExtras().getString(EXTRA_USER_NAME);
            mDateFormatted = Utils.changeDateFormat(mActionDate, Constant.DATE_FORMAT_STAFF_PRODUCTIVITY, Constant.DATE_FORMAT_DISPLAY_DATE);
            mTimeRangeFormatted = getTimeRangeFormatted();
        }
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewDebitur.addItemDecoration(itemDecoration);
        mBinding.recyclerViewDebitur.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewDebitur.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {
                startActivity(DetailDebiturActivity.instantiate(StaffProductivityDebiturActivity.this, item.getNoRekening(), item.getCustomerReference(), ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE));

                return true;
            }
        });
        mBinding.swipeRefreshLayoutDebitur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutDebitur.setRefreshing(true);
                mListDebiturViewModel.getListDebiturStaffProductivity(mUserId, mActionDate, mTimeRange);
            }
        });
    }

    private String getTimeRangeFormatted() {
        switch (mTimeRange) {
            case "1": {
                return getString(R.string.StaffProductivity_Less_10);
            }
            case "2": {
                return getString(R.string.StaffProductivity_10_12);
            }
            case "3": {
                return getString(R.string.StaffProductivity_12_14);
            }
            case "4": {
                return getString(R.string.StaffProductivity_14_16);
            }
            case "5": {
                return getString(R.string.StaffProductivity_16_18);
            }
            case "6": {
                return getString(R.string.StaffProductivity_18_20);
            }
            case "7": {
                return getString(R.string.StaffProductivity_More_20);
            }
            default:
                return "";
        }
    }

    private void setupHeader() {
        mBinding.textViewNamaStaff.setText(mUserName);
        String dateTimeFormatted = mDateFormatted + " (" + mTimeRangeFormatted + ")";
        mBinding.textViewDateTime.setText(dateTimeFormatted);
    }
}
