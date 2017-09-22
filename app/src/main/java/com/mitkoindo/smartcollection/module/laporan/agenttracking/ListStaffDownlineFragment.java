package com.mitkoindo.smartcollection.module.laporan.agenttracking;

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
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentStaffDownlineBinding;
import com.mitkoindo.smartcollection.objectdata.StaffItem;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;
import com.mitkoindo.smartcollection.utils.Utils;

import butterknife.OnClick;


/**
 * Created by ericwijaya on 9/19/17.
 */

public class ListStaffDownlineFragment extends BaseFragment {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private StaffDownlineViewModel mStaffDownlineViewModel;
    private FragmentStaffDownlineBinding mBinding;
    private FastItemAdapter mFastAdapter;

    private String mUserId;


    public static ListStaffDownlineFragment getInstance(String userId) {
        ListStaffDownlineFragment fragment = new ListStaffDownlineFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_downline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mUserId = args.getString(EXTRA_USER_ID);

        mStaffDownlineViewModel = new StaffDownlineViewModel(getAccessToken());
        addViewModel(mStaffDownlineViewModel);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setStaffDownlineViewModel(mStaffDownlineViewModel);

        mStaffDownlineViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mStaffDownlineViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutStaffDownline.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mStaffDownlineViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getUserVisibleHint()) {
//                    displayMessage(R.string.GagalMendapatkanData);
                }
                mFastAdapter.clear();
                mStaffDownlineViewModel.obsIsEmpty.set(true);
            }
        });
        mStaffDownlineViewModel.obsStaffItemResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mStaffDownlineViewModel.obsStaffItemResponse.get() != null) {
                    mFastAdapter.clear();
                    mFastAdapter.add(mStaffDownlineViewModel.obsStaffItemResponse.get());
                    if (mStaffDownlineViewModel.obsStaffItemResponse.get().size() > 0) {
                        mStaffDownlineViewModel.obsIsEmpty.set(false);
                    } else {
                        mStaffDownlineViewModel.obsIsEmpty.set(true);
                    }
                }
            }
        });

        setupRecyclerView();

        mStaffDownlineViewModel.getStaff(mUserId);
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter<>();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewStaffDownline.addItemDecoration(itemDecoration);
        mBinding.recyclerViewStaffDownline.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewStaffDownline.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<StaffItem>() {
            @Override
            public boolean onClick(View v, IAdapter<StaffItem> adapter, StaffItem item, int position) {

                startActivity(AgentTrackingActivity.instantiate(getContext(), item.getUserId()));
                return true;
            }
        });

        mBinding.swipeRefreshLayoutStaffDownline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutStaffDownline.setRefreshing(true);
                mStaffDownlineViewModel.getStaff(mUserId);
            }
        });
    }

    @OnClick(R.id.image_view_search)
    public void onSearchClicked(View view) {
        mStaffDownlineViewModel.getStaff(mUserId);
        Utils.hideKeyboard(view);
    }
}
