package com.mitkoindo.smartcollection.module.laporan.agenttracking;

import android.app.DatePickerDialog;
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
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentListAgentTrackingBinding;
import com.mitkoindo.smartcollection.objectdata.AgentTracking;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/18/17.
 */

public class ListAgentTrackingFragment extends BaseFragment {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private AgentTrackingViewModel mAgentTrackingViewModel;
    private FragmentListAgentTrackingBinding mBinding;

    private FastItemAdapter mFastAdapter;
    private String mUserId;
    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_FORMAT_STAFF_PRODUCTIVITY, id);
    private SimpleDateFormat dateFormatterLayout = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);


    public static Fragment getInstance(String userId) {
        Fragment fragment = new ListAgentTrackingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_agent_tracking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mUserId = args.getString(EXTRA_USER_ID);

        mAgentTrackingViewModel = new AgentTrackingViewModel(getAccessToken());
        addViewModel(mAgentTrackingViewModel);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setAgentTrackingViewModel(mAgentTrackingViewModel);

        mAgentTrackingViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mAgentTrackingViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutAgentTacking.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mAgentTrackingViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMendapatkanData);
            }
        });
        mAgentTrackingViewModel.obsAgentTrackingResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.clear();
                mFastAdapter.add(mAgentTrackingViewModel.obsAgentTrackingResponse.get());
                mBinding.recyclerViewAgentTracking.getLayoutManager().scrollToPosition(0);
                if (mAgentTrackingViewModel.obsAgentTrackingResponse.get().size() > 0) {
                    mAgentTrackingViewModel.obsIsEmpty.set(false);
                } else {
                    mAgentTrackingViewModel.obsIsEmpty.set(true);
                }
            }
        });
        mAgentTrackingViewModel.obsTanggal.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mAgentTrackingViewModel.getListAgentTracking(mUserId, mAgentTrackingViewModel.obsTanggal.get());
            }
        });

        setupRecyclerView();

        setDateNow();
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewAgentTracking.addItemDecoration(itemDecoration);
        mBinding.recyclerViewAgentTracking.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewAgentTracking.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        /*mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {

                return true;
            }
        });*/

        mBinding.swipeRefreshLayoutAgentTacking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutAgentTacking.setRefreshing(true);
                mAgentTrackingViewModel.getListAgentTracking(mUserId, mAgentTrackingViewModel.obsTanggal.get());
            }
        });
    }

    @OnClick(R.id.card_view_tanggal)
    public void onTanggalClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String dateFormatted = dateFormatter.format(c.getTime());
            mAgentTrackingViewModel.obsTanggal.set(dateFormatted);

            String dateFormattedLayout = dateFormatterLayout.format(c.getTime());
            mAgentTrackingViewModel.obsTanggalLayout.set(dateFormattedLayout);
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }

    private void setDateNow() {
        Calendar c = Calendar.getInstance();

        String dateFormatted = dateFormatter.format(c.getTime());
        mAgentTrackingViewModel.obsTanggal.set(dateFormatted);

        String dateFormattedLayout = dateFormatterLayout.format(c.getTime());
        mAgentTrackingViewModel.obsTanggalLayout.set(dateFormattedLayout);
    }
}
