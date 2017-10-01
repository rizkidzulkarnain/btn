package com.mitkoindo.smartcollection.module.formcall;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mitkoindo.smartcollection.FetchAddressIntentService;
import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormCallBinding;
import com.mitkoindo.smartcollection.dialog.DialogFactory;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

import static com.mitkoindo.smartcollection.FetchAddressIntentService.Constants.SUCCESS_RESULT;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCallActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_NO_TELPON = "extra_no_telpon";
    private static final int REQ_CODE_LOCATION_PERMISSION = 1;

    private FormCallViewModel mFormCallViewModel;
    private ActivityFormCallBinding mBinding;

    private Dialog mSpinnerDialog;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;

    private List<DropDownPurpose> mListDropDownPurpose;
    private List<DropDownRelationship> mListDropDownRelationship;
    private List<DropDownResult> mListDropDownResult;
    private List<DropDownReason> mListDropDownReason;
    private List<DropDownAction> mListDropDownAction;
    private List<String> mListTujuanCall = new ArrayList<String>();
    private List<String> mListHubunganDenganDebitur = new ArrayList<String>();
    private List<String> mListHasilCall = new ArrayList<String>();
    private List<String> mListAlasanMenunggak = new ArrayList<String>();
    private List<String> mListTindakLanjut = new ArrayList<String>();

    private String mNoRekening;
    private String mNoTelepon;
    private String mAddressOutput;
    private boolean mAddressRequested;
    private Location mLastKnownLocation;


    public static Intent instantiate(Context context, String noRekening, String noTelepon) {
        Intent intent = new Intent(context, FormCallActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_NO_TELPON, noTelepon);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormCall_PageTitle));
        initArrayDropDown();
        initNoSelectValue();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_call;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();
        initGoogleService();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressOutput = "";
        mAddressRequested = false;

        mFormCallViewModel = new FormCallViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormCallViewModel(mFormCallViewModel);

        mFormCallViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormCallViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mFormCallViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DialogFactory.createDialog(FormCallActivity.this,
                        getString(R.string.FormCall_PageTitle),
                        getString(R.string.GagalMenyimpanFormCall),
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(HomeActivity.instantiateClearTask(FormCallActivity.this));
                            }
                        }).show();
            }
        });
        mFormCallViewModel.jumlahYangAkanDisetor.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    Double jumlahYangAkanDisetor = Double.parseDouble(mFormCallViewModel.jumlahYangAkanDisetor.get());
                    mFormCallViewModel.spParameterFormCall.setPtpAmount(jumlahYangAkanDisetor);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                    mFormCallViewModel.spParameterFormCall.setPtpAmount(0);
                }
            }
        });
        mFormCallViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormCallViewModel.obsIsSaveSuccess.get()) {
                    displayMessage(R.string.FormCall_SaveFormSuccess);
                    startActivity(HomeActivity.instantiateClearTask(FormCallActivity.this));
                }
            }
        });
        mFormCallViewModel.hasilPanggilan.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String hasilPanggilan = mFormCallViewModel.hasilPanggilan.get();
                String hasilPanggilanId = "";
                for (DropDownResult dropDownResult : mListDropDownResult) {
                    if (!TextUtils.isEmpty(dropDownResult.getResultDesc()) && dropDownResult.getResultDesc().equals(hasilPanggilan)) {
                        hasilPanggilanId = dropDownResult.getResultId();
                        break;
                    }
                }

//                Jika hasil kunjungan = akan setor tanggal / akan datang ke btn tanggal / minta dihubungi tanggal, maka show field tanggal janji debitur
                if (hasilPanggilanId.equals(RestConstants.RESULT_ID_AKAN_SETOR_TANGGAL_VALUE)
                        || hasilPanggilanId.equals(RestConstants.RESULT_ID_AKAN_DATANG_KE_BTN_TANGGAL_VALUE)
                        || hasilPanggilanId.equals(RestConstants.RESULT_ID_MINTA_DIHUBUNGI_TANGGAL_VALUE)) {

                    mFormCallViewModel.obsIsShowTanggalJanjiDebitur.set(true);

//                    Jika hasil kunjungan = akan setor tanggal, maka show field jumlah yang akan disetor
                    if (hasilPanggilanId.equals(RestConstants.RESULT_ID_AKAN_SETOR_TANGGAL_VALUE)) {
                        mFormCallViewModel.obsIsShowJumlahYangAkanDisetor.set(true);
                        mFormCallViewModel.tanggalHasilPanggilan.set(getString(R.string.FormCall_TanggalHasilPanggilanInitial));
                    } else {
                        mFormCallViewModel.obsIsShowJumlahYangAkanDisetor.set(false);
                        mFormCallViewModel.tanggalHasilPanggilan.set(getString(R.string.FormCall_TanggalRealisasiJanjiInitial));
                    }
                } else {
                    mFormCallViewModel.obsIsShowTanggalJanjiDebitur.set(false);
                    mFormCallViewModel.obsIsShowJumlahYangAkanDisetor.set(false);
                }

                mFormCallViewModel.spParameterFormCall.setResultDate(null);
                mFormCallViewModel.spParameterFormCall.setPtpAmount(0);
                mFormCallViewModel.jumlahYangAkanDisetor.set("");
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
            mNoTelepon = getIntent().getExtras().getString(EXTRA_NO_TELPON);
        }
    }

    private void initArrayDropDown() {
        mListDropDownPurpose = RealmHelper.getListDropDownPurpose();
        for (DropDownPurpose dropDownPurpose : mListDropDownPurpose) {
            if (dropDownPurpose.getPDesc() != null) {
                mListTujuanCall.add(dropDownPurpose.getPDesc());
            }
        }

        mListDropDownRelationship = RealmHelper.getListDropDownRelationship();
        for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
            if (dropDownRelationship.getRelDesc() != null) {
                mListHubunganDenganDebitur.add(dropDownRelationship.getRelDesc());
            }
        }

        mListDropDownResult = RealmHelper.getListDropDownResult();
        for (DropDownResult dropDownResult : mListDropDownResult) {
            if (dropDownResult.getResultDesc() != null) {
                mListHasilCall.add(dropDownResult.getResultDesc());
            }
        }

        mListDropDownReason = RealmHelper.getListDropDownReason();
        for (DropDownReason dropDownReason : mListDropDownReason) {
            if (dropDownReason.getReasonDesc() != null) {
                mListAlasanMenunggak.add(dropDownReason.getReasonDesc());
            }
        }

        mListDropDownAction = RealmHelper.getListDropDownAction();
        for (DropDownAction dropDownAction : mListDropDownAction) {
            if (dropDownAction.getActionDesc() != null) {
                mListTindakLanjut.add(dropDownAction.getActionDesc());
            }
        }
    }

    private void initNoSelectValue() {
        mFormCallViewModel.tujuan.set(getString(R.string.FormCall_TujuanCallInitial));
        mFormCallViewModel.hubunganDenganDebitur.set(getString(R.string.FormVisit_HubunganDenganDebiturInitial));
        mFormCallViewModel.hasilPanggilan.set(getString(R.string.FormCall_HasilPanggilanInitial));
        mFormCallViewModel.tanggalHasilPanggilan.set(getString(R.string.FormCall_TanggalHasilPanggilanInitial));
        mFormCallViewModel.alasanTidakBayar.set(getString(R.string.FormCall_AlasanTidakBayarInitial));
        mFormCallViewModel.tindakLanjut.set(getString(R.string.FormCall_TindakLanjutInitial));
        mFormCallViewModel.tanggalTindakLanjut.set(getString(R.string.FormCall_TanggalTindakLanjutInitial));
    }

    private void showInstallmentDialogSimpleSpinner(List<String> nameList, String dialogTitle, int viewId) {
        if (mSpinnerDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mSpinnerDialog = dialogBuilder.create();
        }
        if (nameList.size() > 0) {
            mSpinnerDialog.show();
            RecyclerView recyclerView = (RecyclerView) mSpinnerDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogSimpleSpinnerAdapter adapter = new DialogSimpleSpinnerAdapter(nameList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mSpinnerDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(this, getString(R.string.TidakAdaDataPilihan));
        }
    }

    @Subscribe
    public void onDialogSimpleSpinnerSelected(EventDialogSimpleSpinnerSelected event) {
        if (mSpinnerDialog != null && mSpinnerDialog.isShowing()) {
            mSpinnerDialog.dismiss();

            switch (event.getViewId()) {
                case R.id.card_view_tujuan: {
                    for (DropDownPurpose dropDownPurpose : mListDropDownPurpose) {
                        if (!TextUtils.isEmpty(dropDownPurpose.getPDesc()) && dropDownPurpose.getPDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameterFormCall.setTujuan(dropDownPurpose.getPId());
                            mFormCallViewModel.tujuan.set(dropDownPurpose.getPDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hubungan_dengan_debitur: {
                    for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
                        if (!TextUtils.isEmpty(dropDownRelationship.getRelDesc()) && dropDownRelationship.getRelDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameterFormCall.setRelationship(dropDownRelationship.getRelId());
                            mFormCallViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hasil_panggilan: {
                    for (DropDownResult dropDownResult : mListDropDownResult) {
                        if (!TextUtils.isEmpty(dropDownResult.getResultDesc()) && dropDownResult.getResultDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameterFormCall.setResult(dropDownResult.getResultId());
                            mFormCallViewModel.hasilPanggilan.set(dropDownResult.getResultDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_alasan_tidak_bayar: {
                    for (DropDownReason dropDownReason : mListDropDownReason) {
                        if (!TextUtils.isEmpty(dropDownReason.getReasonDesc()) && dropDownReason.getReasonDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameterFormCall.setReasonNoPayment(dropDownReason.getReasonId());
                            mFormCallViewModel.spParameterFormCall.setReasonNonPaymentDesc(dropDownReason.getReasonDesc());
                            mFormCallViewModel.alasanTidakBayar.set(dropDownReason.getReasonDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_tindak_lanjut: {
                    for (DropDownAction dropDownAction : mListDropDownAction) {
                        if (!TextUtils.isEmpty(dropDownAction.getActionDesc()) && dropDownAction.getActionDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameterFormCall.setNextAction(dropDownAction.getActionId());
                            mFormCallViewModel.tindakLanjut.set(dropDownAction.getActionDesc());
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @OnClick(R.id.card_view_tujuan)
    public void onTujuanCallClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListTujuanCall, getString(R.string.FormCall_TujuanCallInitial), R.id.card_view_tujuan);
    }

    @OnClick(R.id.card_view_hubungan_dengan_debitur)
    public void onHubunganDenganDebiturClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHubunganDenganDebitur, getString(R.string.FormCall_HubunganDenganDebiturInitial), R.id.card_view_hubungan_dengan_debitur);
    }

    @OnClick(R.id.card_view_hasil_panggilan)
    public void onHasilCallClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHasilCall, getString(R.string.FormCall_HasilPanggilanInitial), R.id.card_view_hasil_panggilan);
    }

    @OnClick(R.id.card_view_alasan_tidak_bayar)
    public void onAlasanTidakBayarClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListAlasanMenunggak, getString(R.string.FormCall_AlasanTidakBayarInitial), R.id.card_view_alasan_tidak_bayar);
    }

    @OnClick(R.id.card_view_tindak_lanjut)
    public void onTindakLanjutClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListTindakLanjut, getString(R.string.FormCall_TindakLanjutInitial), R.id.card_view_tindak_lanjut);
    }


    @OnClick(R.id.card_view_tanggal_hasil_panggilan)
    public void onTanggalHasilPanggilanClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFormCallViewModel.setTanggalHasilPanggilan(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());

//        Get Date last day of the month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        datePickerDialog.show();
    }

    @OnClick(R.id.card_view_tanggal_tindak_lanjut)
    public void onTanggalTindakLanjutClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFormCallViewModel.setTanggalTindakLanjut(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());

//        Get Date last day of the month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        datePickerDialog.show();
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        if (isValid()) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.FormCall_KonfirmasiSubmit))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            mFormCallViewModel.saveFormCall(getAccessToken());
                            requestAccessLocationPermission();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private boolean isValid() {
        mFormCallViewModel.spParameterFormCall.setAccountNo(mNoRekening);
        mFormCallViewModel.spParameterFormCall.setContactNo(mNoTelepon);
        mFormCallViewModel.spParameterFormCall.setUserId(getUserId());
        FormCallBody.SpParameterFormCall spParameterFormCall = mFormCallViewModel.spParameterFormCall;
        if (TextUtils.isEmpty(spParameterFormCall.getTujuan())) {
            displayMessage(getString(R.string.FormCall_TujuanCallInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getSpokeTo())) {
            displayMessage(getString(R.string.FormCall_BerbicaraDenganHint));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getRelationship())) {
            displayMessage(getString(R.string.FormCall_HubunganDenganDebiturInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getResult())) {
            displayMessage(getString(R.string.FormCall_HasilPanggilanInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getResultDate()) && mFormCallViewModel.obsIsShowTanggalJanjiDebitur.get()) {
            if (mFormCallViewModel.obsIsShowJumlahYangAkanDisetor.get()) {
                displayMessage(getString(R.string.FormCall_TanggalHasilPanggilanInitial));
            } else {
                displayMessage(getString(R.string.FormCall_TanggalRealisasiJanjiInitial));
            }
            return false;
        } else if (spParameterFormCall.getPtpAmount() == 0 && mFormCallViewModel.obsIsShowJumlahYangAkanDisetor.get()) {
            displayMessage(getString(R.string.FormCall_JumlahYangAkanDisetorHint));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getReasonNoPayment())) {
            displayMessage(getString(R.string.FormCall_AlasanTidakBayarInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getNextAction())) {
            displayMessage(getString(R.string.FormCall_TindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getDateAction())) {
            displayMessage(getString(R.string.FormCall_TanggalTindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameterFormCall.getNotes())) {
            displayMessage(getString(R.string.FormCall_CatatanHint));
            return false;
        }
        return true;
    }

    // Create a GoogleApiClient instance
    private void initGoogleService() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void requestAccessLocationPermission() {
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
        } else {
            getLastKnownLocation();
        }
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestAccessLocationPermission();
            } else {
//                showLocationAndPopup(CENTRAL_JAKATAR_LAT, CENTRAL_JAKATAR_LONG, false);
//                if not get location permission
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;

                                mFormCallViewModel.spParameterFormCall.setGeoLatitude(location.getLatitude());
                                mFormCallViewModel.spParameterFormCall.setGeoLongitude(location.getLongitude());

                                if (!mFormCallViewModel.obsIsShowTanggalJanjiDebitur.get()) {
                                    mFormCallViewModel.spParameterFormCall.setResultDate("");
                                    mFormCallViewModel.spParameterFormCall.setPtpAmount(0);
                                }

//                                Get Address string
                                startIntentService(location);
                                mAddressRequested = true;
                            } else {
                                mFormCallViewModel.obsIsLoading.set(false);
                            }
                        }
                    });
        } catch (SecurityException e) {
            mFormCallViewModel.obsIsLoading.set(false);
        }
    }

    private void startIntentService(Location location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in Activity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            if (resultCode == SUCCESS_RESULT) {
                mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            } else {
                mAddressOutput = String.format(getString(R.string.GagalMendapatkanGeoAddress), mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            }

            mFormCallViewModel.spParameterFormCall.setGeoAddress(mAddressOutput);

            mFormCallViewModel.saveFormCall(getAccessToken());

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }
}
