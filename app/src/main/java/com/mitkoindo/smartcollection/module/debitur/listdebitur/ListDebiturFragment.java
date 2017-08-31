package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.mitkoindo.smartcollection.databinding.FragmentListDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;


/**
 * Created by ericwijaya on 8/22/17.
 */

public class ListDebiturFragment extends BaseFragment {

    private static final String EXTRA_STATUS = "extra_status";

    private ListDebiturViewModel mListDebiturViewModel;
    private FragmentListDebiturBinding mBinding;
    private FastItemAdapter mFastAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;
    private String mStatus = RestConstants.LIST_DEBITUR_STATUS_PENDING_VALUE;


    public static ListDebiturFragment getInstance() {
        return new ListDebiturFragment();
    }

    public static Fragment getInstance(String status) {
        Fragment fragment = new ListDebiturFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_debitur, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mStatus = args.getString(EXTRA_STATUS);

        mListDebiturViewModel = new ListDebiturViewModel(getAccessToken());
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(view);

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
                displayMessage(R.string.GagalMendapatkanData);
            }
        });
        mListDebiturViewModel.obsDebiturResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.clear();
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponse.get());
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
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponseLoadMore.get());
            }
        });

        setupRecyclerView();

        mListDebiturViewModel.getListDebitur(getUserId(), mStatus, 1);
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
                startActivity(DetailDebiturActivity.instantiate(getActivity(), item.getNoRekening()));

                return true;
            }
        });
        mBinding.recyclerViewDebitur.addOnScrollListener(new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(final int currentPage) {
                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(false));

                mListDebiturViewModel.getListDebitur(getUserId(), mStatus, currentPage);
            }
        });

        mBinding.swipeRefreshLayoutDebitur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutDebitur.setRefreshing(true);
                mListDebiturViewModel.getListDebitur(getUserId(), mStatus, 1);
            }
        });
    }
}
