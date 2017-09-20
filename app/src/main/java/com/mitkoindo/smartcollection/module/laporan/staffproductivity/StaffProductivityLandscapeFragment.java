package com.mitkoindo.smartcollection.module.laporan.staffproductivity;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentStaffProductivityLandscapeBinding;
import com.mitkoindo.smartcollection.objectdata.StaffProductivity;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;
import com.mitkoindo.smartcollection.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/18/17.
 */

public class StaffProductivityLandscapeFragment extends BaseFragment {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private StaffProductivityViewModel mStaffProductivityViewModel;
    private FragmentStaffProductivityLandscapeBinding mBinding;

    private FastItemAdapter mFastAdapter;
    private String mUserId;
    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_FORMAT_STAFF_PRODUCTIVITY, id);
    private SimpleDateFormat dateFormatterLayout = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);


    public static Fragment getInstance(String userId) {
        Fragment fragment = new StaffProductivityLandscapeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_productivity_landscape, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mUserId = args.getString(EXTRA_USER_ID);

        mStaffProductivityViewModel = new StaffProductivityViewModel(getAccessToken());
        addViewModel(mStaffProductivityViewModel);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setStaffProductivityViewModel(mStaffProductivityViewModel);

        mStaffProductivityViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mStaffProductivityViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutStaffProductivity.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mStaffProductivityViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMendapatkanData);
                mFastAdapter.clear();
            }
        });
        mStaffProductivityViewModel.obsListStaffProductivity.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.clear();
                mFastAdapter.add(mStaffProductivityViewModel.obsListStaffProductivity.get());
                mBinding.recyclerViewStaffProductivity.getLayoutManager().scrollToPosition(0);
                if (mStaffProductivityViewModel.obsListStaffProductivity.get().size() > 0) {
                    mStaffProductivityViewModel.obsIsEmpty.set(false);
                } else {
                    mStaffProductivityViewModel.obsIsEmpty.set(true);
                }
            }
        });
        mStaffProductivityViewModel.obsTanggal.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mStaffProductivityViewModel.getStaffProductivity(mUserId, mStaffProductivityViewModel.obsTanggal.get());
            }
        });

        setupRecyclerView();

        setDateNow();
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_smaller);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewStaffProductivity.addItemDecoration(itemDecoration);
        mBinding.recyclerViewStaffProductivity.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewStaffProductivity.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<StaffProductivity>() {
            @Override
            public boolean onClick(View v, IAdapter<StaffProductivity> adapter, StaffProductivity item, int position) {

                return true;
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background1;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange1 = Utils.stringToInt(item.getTimeRange1());
                if (timeRange1 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "1"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background2;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange2 = Utils.stringToInt(item.getTimeRange2());
                if (timeRange2 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "2"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background3;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange3 = Utils.stringToInt(item.getTimeRange3());
                if (timeRange3 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "3"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background4;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange4 = Utils.stringToInt(item.getTimeRange4());
                if (timeRange4 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "4"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background5;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange5 = Utils.stringToInt(item.getTimeRange5());
                if (timeRange5 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "5"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background6;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange6 = Utils.stringToInt(item.getTimeRange6());
                if (timeRange6 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "6"));
                }
            }
        });
        mFastAdapter.withEventHook(new ClickEventHook<StaffProductivity>() {
            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof StaffProductivity.ViewHolder) {
                    return ((StaffProductivity.ViewHolder) viewHolder).binding.background7;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<StaffProductivity> fastAdapter, StaffProductivity item) {
                int timeRange7 = Utils.stringToInt(item.getTimeRange7());
                if (timeRange7 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), item.getUserID(), mStaffProductivityViewModel.obsTanggal.get(), "7"));
                }
            }
        });

        mBinding.swipeRefreshLayoutStaffProductivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutStaffProductivity.setRefreshing(true);
                mStaffProductivityViewModel.getStaffProductivity(mUserId, mStaffProductivityViewModel.obsTanggal.get());
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
            mStaffProductivityViewModel.obsTanggal.set(dateFormatted);

            String dateFormattedLayout = dateFormatterLayout.format(c.getTime());
            mStaffProductivityViewModel.obsTanggalLayout.set(dateFormattedLayout);
        }, currentYear, currentMonth, currentDay);

//        Get Date first day of the month
        Calendar calendarMin = Calendar.getInstance();
        calendarMin.set(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendarMin.getTime().getTime());

//        Get Date last day of the month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        datePickerDialog.show();
    }

    private void setDateNow() {
        Calendar c = Calendar.getInstance();

        String dateFormatted = dateFormatter.format(c.getTime());
        mStaffProductivityViewModel.obsTanggal.set(dateFormatted);

        String dateFormattedLayout = dateFormatterLayout.format(c.getTime());
        mStaffProductivityViewModel.obsTanggalLayout.set(dateFormattedLayout);
    }
}
