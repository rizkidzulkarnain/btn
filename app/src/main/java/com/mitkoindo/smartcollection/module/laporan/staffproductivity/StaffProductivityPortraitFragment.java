package com.mitkoindo.smartcollection.module.laporan.staffproductivity;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentStaffProductivityPortraitBinding;
import com.mitkoindo.smartcollection.objectdata.StaffProductivity;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/17/17.
 */

public class StaffProductivityPortraitFragment extends BaseFragment {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private StaffProductivityViewModel mStaffProductivityViewModel;
    private FragmentStaffProductivityPortraitBinding mBinding;

    private String mUserId;
    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_FORMAT_STAFF_PRODUCTIVITY, id);
    private SimpleDateFormat dateFormatterLayout = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);
    private StaffProductivity mEmptyStaffProductivity = new StaffProductivity();


    public static StaffProductivityPortraitFragment getInstance(String userId) {
        StaffProductivityPortraitFragment fragment = new StaffProductivityPortraitFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_productivity_portrait, container, false);
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

        setEmptyStaffProductivity();

        mStaffProductivityViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mStaffProductivityViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mStaffProductivityViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mStaffProductivityViewModel.errorType == StaffProductivityViewModel.NOT_FOUND_ERROR_TYPE) {
                    displayMessage(R.string.GagalDataTanggalIni);
                } else {
                    displayMessage(R.string.GagalMendapatkanData);
                }

                mStaffProductivityViewModel.obsStaffProductivity.set(mEmptyStaffProductivity);
            }
        });
        mStaffProductivityViewModel.obsListStaffProductivity.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

            }
        });
        mStaffProductivityViewModel.obsTanggal.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mStaffProductivityViewModel.getStaffProductivity(mUserId, mStaffProductivityViewModel.obsTanggal.get());
            }
        });

        setDateNow();
    }

    @OnClick({R.id.background_1, R.id.background_2, R.id.background_3, R.id.background_4, R.id.background_5, R.id.background_6, R.id.background_7})
    public void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.background_1: {
                int timeRange1 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange1());
                if (timeRange1 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "1"));
                }
                break;
            }
            case R.id.background_2: {
                int timeRange2 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange2());
                if (timeRange2 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "2"));
                }
                break;
            }
            case R.id.background_3: {
                int timeRange3 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange3());
                if (timeRange3 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "3"));
                }
                break;
            }
            case R.id.background_4: {
                int timeRange4 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange4());
                if (timeRange4 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "4"));
                }
                break;
            }
            case R.id.background_5: {
                int timeRange5 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange5());
                if (timeRange5 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "5"));
                }
                break;
            }
            case R.id.background_6: {
                int timeRange6 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange6());
                if (timeRange6 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "6"));
                }
                break;
            }
            case R.id.background_7: {
                int timeRange7 = Utils.stringToInt(mStaffProductivityViewModel.obsStaffProductivity.get().getTimeRange7());
                if (timeRange7 > 0) {
                    startActivity(StaffProductivityDebiturActivity.instantiate(getContext(), mUserId, mStaffProductivityViewModel.obsTanggal.get(), "7"));
                }
                break;
            }
        }
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

    private void setEmptyStaffProductivity() {
        mEmptyStaffProductivity.setTimeRange1("0");
        mEmptyStaffProductivity.setTimeRange2("0");
        mEmptyStaffProductivity.setTimeRange3("0");
        mEmptyStaffProductivity.setTimeRange4("0");
        mEmptyStaffProductivity.setTimeRange5("0");
        mEmptyStaffProductivity.setTimeRange6("0");
        mEmptyStaffProductivity.setTimeRange7("0");
        mEmptyStaffProductivity.setTotal("0");
    }
}
