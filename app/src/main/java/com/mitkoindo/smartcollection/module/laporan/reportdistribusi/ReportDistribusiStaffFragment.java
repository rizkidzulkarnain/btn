package com.mitkoindo.smartcollection.module.laporan.reportdistribusi;


import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentReportDistribusiStaffBinding;
import com.mitkoindo.smartcollection.objectdata.ReportDistribusiStaff;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;


public class ReportDistribusiStaffFragment extends BaseFragment {

    private ReportDistribusiStaffViewModel mReportDistribusiViewModel;
    private FragmentReportDistribusiStaffBinding mBinding;
    private FastItemAdapter<IItem> mFastAdapter;


    public static ReportDistribusiStaffFragment getInstance() {
        ReportDistribusiStaffFragment fragment = new ReportDistribusiStaffFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_distribusi_staff, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportDistribusiViewModel = new ReportDistribusiStaffViewModel(getAccessToken());
        addViewModel(mReportDistribusiViewModel);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setReportDistribusiStaffViewModel(mReportDistribusiViewModel);

        mReportDistribusiViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mReportDistribusiViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutDebitur.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mReportDistribusiViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getUserVisibleHint()) {
//                    displayMessage(R.string.GagalMendapatkanData);
                }
                mFastAdapter.clear();
                mReportDistribusiViewModel.obsIsEmpty.set(true);
            }
        });
        mReportDistribusiViewModel.obsReportDistribusiSummaryResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                if (mReportDistribusiViewModel.obsReportDistribusiSummaryResponse.get() != null &&
                        mReportDistribusiViewModel.obsReportDistribusiSummaryResponse.get().size() > 0) {
                    mFastAdapter.clear();
                    mFastAdapter.add(mReportDistribusiViewModel.obsReportDistribusiSummaryResponse.get().get(0));
                }

                mBinding.recyclerViewStaff.getLayoutManager().scrollToPosition(0);
            }
        });
        mReportDistribusiViewModel.obsReportDistribusiStaffResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mReportDistribusiViewModel.obsReportDistribusiStaffResponse.get() != null) {
                    for (ReportDistribusiStaff reportDistribusiStaff : mReportDistribusiViewModel.obsReportDistribusiStaffResponse.get()) {
                        mFastAdapter.add(reportDistribusiStaff);
                    }
                    if (mReportDistribusiViewModel.obsReportDistribusiStaffResponse.get().size() > 0) {
                        mReportDistribusiViewModel.obsIsEmpty.set(false);
                    } else {
                        mReportDistribusiViewModel.obsIsEmpty.set(true);
                    }
                }
            }
        });

        setupRecyclerView();

        mReportDistribusiViewModel.getListReportDistribusiStaff(getUserId());
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter<>();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewStaff.addItemDecoration(itemDecoration);
        mBinding.recyclerViewStaff.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewStaff.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter<IItem> adapter, IItem item, int position) {
                if (item instanceof ReportDistribusiStaff) {
                    ReportDistribusiStaff reportDistribusiStaff = (ReportDistribusiStaff) item;
                    startActivity(ReportDistribusiDebiturActivity.instantiate(getContext(), reportDistribusiStaff.getUserID()));
                }
                return true;
            }
        });

        mBinding.swipeRefreshLayoutDebitur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutDebitur.setRefreshing(true);
                mReportDistribusiViewModel.getListReportDistribusiStaff(getUserId());
            }
        });
    }
}
