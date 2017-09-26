package com.mitkoindo.smartcollection.module.laporan.agenttracking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentListAgentTrackingBinding;
import com.mitkoindo.smartcollection.module.laporan.LaporanVisitActivity;
import com.mitkoindo.smartcollection.objectdata.AgentTracking;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/18/17.
 */

public class ListAgentTrackingFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {

    private static final String EXTRA_USER_ID = "extra_user_id";

    private AgentTrackingViewModel mAgentTrackingViewModel;
    private FragmentListAgentTrackingBinding mBinding;

    private GoogleMap mGoogleMap = null;
    private Marker mCurrentMarker = null;
    private ArrayMap<Marker, Integer> mMarkerArrayMap= new ArrayMap<>();

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

    @Override
    public void onResume() {
        super.onResume();

        if (mBinding != null && mBinding.mapView != null) {
            mBinding.mapView.onResume();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBinding != null && mBinding.mapView != null) {
            mBinding.mapView.onDestroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mBinding != null && mBinding.mapView != null) {
            mBinding.mapView.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mBinding != null && mBinding.mapView != null) {
            mBinding.mapView.onStop();
        }
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
                mFastAdapter.clear();
                mAgentTrackingViewModel.obsIsEmpty.set(true);
                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                }
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

                dropPin();
            }
        });
        mAgentTrackingViewModel.obsTanggal.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mAgentTrackingViewModel.getListAgentTracking(mUserId, mAgentTrackingViewModel.obsTanggal.get());
            }
        });
        mAgentTrackingViewModel.obsIsMap.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!mAgentTrackingViewModel.obsIsMap.get()) {
                    mAgentTrackingViewModel.obsIsInfoWindowShow.set(false);
                    mAgentTrackingViewModel.obsIsLegendShow.set(false);
                }
            }
        });

        setupRecyclerView();
        setupMap();
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
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<AgentTracking>() {
            @Override
            public boolean onClick(View v, IAdapter<AgentTracking> adapter, AgentTracking item, int position) {
                if (!TextUtils.isEmpty(item.getIDVisit())) {
                    Intent intent = new Intent(getContext(), LaporanVisitActivity.class);
                    intent.putExtra("VisitID", item.getIDVisit());
                    startActivity(intent);
                }
                return true;
            }
        });

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
        mAgentTrackingViewModel.obsTanggal.set(dateFormatted);

        String dateFormattedLayout = dateFormatterLayout.format(c.getTime());
        mAgentTrackingViewModel.obsTanggalLayout.set(dateFormattedLayout);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(this);
    }

    private void setupMap() {
        mBinding.mapView.getMapAsync(this);
        mBinding.mapView.onCreate(getArguments());
    }

    private void dropPin() {
        if (mGoogleMap != null && mAgentTrackingViewModel.obsAgentTrackingResponse.get() != null) {
            mGoogleMap.clear();
            mAgentTrackingViewModel.obsIsInfoWindowShow.set(false);
            mCurrentMarker = null;

            int i = 0;
            for (AgentTracking agentTracking : mAgentTrackingViewModel.obsAgentTrackingResponse.get()) {
                LatLng latLng = new LatLng(agentTracking.getLatitude(), agentTracking.getLongitude());
                int trackingType = agentTracking.getTrackingType();
                float drawable;
                switch (trackingType) {
                    case AgentTracking.TYPE_VISIT: {
//                        drawable = R.drawable.ic_home_map;
                        drawable = BitmapDescriptorFactory.HUE_RED;
                        break;
                    }
                    case AgentTracking.TYPE_CALL: {
//                        drawable = R.drawable.ic_phone;
                        drawable = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                    }
                    case AgentTracking.TYPE_CHECK_IN: {
//                        drawable = R.drawable.ic_map_marker;
                        drawable = BitmapDescriptorFactory.HUE_BLUE;
                        break;
                    }
                    case AgentTracking.TYPE_TRACKING: {
//                        drawable = R.drawable.ic_dot;
                        drawable = BitmapDescriptorFactory.HUE_YELLOW;
                        break;
                    }
                    default: {
//                        drawable = R.drawable.ic_dot;
                        drawable = BitmapDescriptorFactory.HUE_YELLOW;
                    }
                }

                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(agentTracking.getTimeFormatted())
                        .snippet(agentTracking.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(drawable)));
//                      .icon(BitmapDescriptorFactory.fromResource(drawable)));

                mMarkerArrayMap.put(marker, i);

                if (i == 0) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mGoogleMap.animateCamera(cameraUpdate);
                }
                i++;
            }
        }
    }

    @OnClick(R.id.image_view_switch_layout)
    public void onSwitchViewClicked(View view) {
        mAgentTrackingViewModel.obsIsMap.set(!mAgentTrackingViewModel.obsIsMap.get());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mCurrentMarker = marker;
        Integer position = mMarkerArrayMap.get(marker);
        AgentTracking agentTracking = mAgentTrackingViewModel.obsAgentTrackingResponse.get().get(position);
        mAgentTrackingViewModel.obsInfoWindowTime.set(agentTracking.getTimeFormatted());
        mAgentTrackingViewModel.obsInfoWindowAddress.set(agentTracking.getAddress());
        mAgentTrackingViewModel.obsIsInfoWindowShow.set(true);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mAgentTrackingViewModel.obsIsInfoWindowShow.set(false);
        mAgentTrackingViewModel.obsIsLegendShow.set(false);
    }

    @OnClick(R.id.card_view_info_window)
    public void onInfoWindowClicked(View view) {
        Integer position = mMarkerArrayMap.get(mCurrentMarker);
        AgentTracking agentTracking = mAgentTrackingViewModel.obsAgentTrackingResponse.get().get(position);

        if (!TextUtils.isEmpty(agentTracking.getIDVisit())) {
            Intent intent = new Intent(getContext(), LaporanVisitActivity.class);
            intent.putExtra("VisitID", agentTracking.getIDVisit());
            startActivity(intent);
        }
    }

    @OnClick(R.id.card_view_legend_button)
    public void onLegendClicked(View view) {
        mAgentTrackingViewModel.obsIsLegendShow.set(!mAgentTrackingViewModel.obsIsLegendShow.get());
    }
}
