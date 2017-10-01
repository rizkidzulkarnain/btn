package com.mitkoindo.smartcollection.module.laporan.reportdistribusi;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentReportDistribusiDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturViewModel;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;
import com.mitkoindo.smartcollection.utils.Utils;

import butterknife.OnClick;


/**
 * Created by ericwijaya on 9/16/17.
 */

public class ReportDistribusiDebiturFragment extends BaseFragment {

    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_USER_ID = "extra_user_id";

    private ListDebiturViewModel mListDebiturViewModel;
    private FragmentReportDistribusiDebiturBinding mBinding;

    private FastItemAdapter mFastAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;
    private EndlessRecyclerOnScrollListener mScrollListener;
    private String mType;
    private String mUserId;
    private int mPage = 1;


    public static Fragment getInstance(String type, String userId) {
        Fragment fragment = new ReportDistribusiDebiturFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TYPE, type);
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_distribusi_debitur, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mType = args.getString(EXTRA_TYPE);
        mUserId = args.getString(EXTRA_USER_ID);

        mListDebiturViewModel = new ListDebiturViewModel(getAccessToken());
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(view);
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
                if (mPage == 1) {
                    mListDebiturViewModel.obsIsEmpty.set(true);
                }
                mFooterAdapter.clear();
            }
        });
        mListDebiturViewModel.obsDebiturResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFooterAdapter.clear();
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
        mListDebiturViewModel.obsDebiturResponseLoadMore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFooterAdapter.clear();
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponseLoadMore.get());
            }
        });

        setupRecyclerView();

        mPage = 1;
        mListDebiturViewModel.getListDebiturReportDistribusi(mUserId, mPage);
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();
        mFooterAdapter = new FooterAdapter<>();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewDebitur.addItemDecoration(itemDecoration);
        mBinding.recyclerViewDebitur.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewDebitur.setAdapter(mFooterAdapter.wrap(mFastAdapter));

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {
                startActivity(DetailDebiturActivity.instantiate(getActivity(), item.getNoRekening(), item.getCustomerReference(), mType));

                return true;
            }
        });
        mScrollListener = new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(final int currentPage) {
                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(false));

                mPage = currentPage;
                mListDebiturViewModel.getListDebiturReportDistribusi(mUserId, mPage);
            }
        };
        mBinding.recyclerViewDebitur.addOnScrollListener(mScrollListener);

        mBinding.swipeRefreshLayoutDebitur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutDebitur.setRefreshing(true);
                mPage = 1;
                mScrollListener.resetPageCount(1);
            }
        });
    }

    @OnClick(R.id.image_view_search)
    public void onSearchClicked(View view) {
        mPage = 1;
        mScrollListener.resetPageCount(1);
        Utils.hideKeyboard(view);
    }
}
