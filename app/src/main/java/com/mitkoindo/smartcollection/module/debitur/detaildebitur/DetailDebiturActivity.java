package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikepenz.fastadapter_extensions.items.TwoLineItem;
import com.mitkoindo.smartcollection.FetchAddressIntentService;
import com.mitkoindo.smartcollection.HistoriTindakanActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityDetailDebiturBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.dialog.DialogTwoLineSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.tambahalamat.TambahAlamatActivity;
import com.mitkoindo.smartcollection.module.debitur.tambahalamatdebitur.TambahAlamatDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.tambahtelepon.TambahTeleponActivity;
import com.mitkoindo.smartcollection.module.formcall.FormCallActivity;
import com.mitkoindo.smartcollection.module.formvisit.FormVisitActivity;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DetailDebiturDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_CUSTOMER_REFERENCE = "extra_customer_reference";
    private static final String EXTRA_TYPE = "extra_type";
    private static final int REQ_CODE_LOCATION_PERMISSION = 1;

    private DetailDebiturViewModel mDetailDebiturViewModel;
    private ActivityDetailDebiturBinding mBinding;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;

    private PopupMenu mPopUpMenu;
    private Dialog mListPhoneNumberDialog;
    private Dialog mListAddressDialog;
    private ArrayList<DialogTwoLineSpinnerAdapter.TwoLineSpinner> mListNomorTelepon = new ArrayList<>();
    private String mNoRekening = "";
    private String mCustomerReference = "";
    private Location mLastKnownLocation;
    private String mAddressOutput;
    private boolean mAddressRequested;

    private String mType = ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE;


    public static Intent instantiate(Context context, String noRekening, String customerReference, String type) {
        Intent intent = new Intent(context, DetailDebiturActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_CUSTOMER_REFERENCE, customerReference);
        intent.putExtra(EXTRA_TYPE, type);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.DetailDebitur_PageTitle));
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
        return R.layout.activity_detail_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();
        initGoogleService();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressOutput = "";
        mAddressRequested = false;

        mDetailDebiturViewModel = new DetailDebiturViewModel(getAccessToken());
        addViewModel(mDetailDebiturViewModel);
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setDetailDebiturViewModel(mDetailDebiturViewModel);

        mDetailDebiturViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mDetailDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.mErrorType == DetailDebiturViewModel.GET_PHONE_LIST_ERROR) {
//                    displayMessage(R.string.GagalMendapatListNomorTelepon);

                    List<PhoneNumber> phoneNumberList = new ArrayList<PhoneNumber>();
                    List<PhoneNumberDb> phoneNumberDbList = RealmHelper.getPhoneNumber(mNoRekening);
                    for (PhoneNumberDb phoneNumberDb : phoneNumberDbList) {
                        phoneNumberList.add(phoneNumberDb.toPhoneNumber());
                    }

                    mDetailDebiturViewModel.obsListPhoneNumber.set(phoneNumberList);
                } else if (mDetailDebiturViewModel.mErrorType == DetailDebiturViewModel.CHECK_IN_ERROR) {
                    displayMessage(R.string.GagalCheckIn);
                } else {
//                    displayMessage(R.string.GagalMendapatkanData);

                    DetailDebiturDb detailDebiturDb = RealmHelper.getDetailDebitur(mNoRekening);
                    mDetailDebiturViewModel.obsDetailDebitur.set(detailDebiturDb.toDetailDebitur());
                }
            }
        });
        mDetailDebiturViewModel.obsDetailDebitur.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.obsDetailDebitur.get() != null) {
                    displayResult(mDetailDebiturViewModel.obsDetailDebitur.get());
                }
            }
        });
        mDetailDebiturViewModel.obsListPhoneNumber.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.obsListPhoneNumber.get() != null) {
                    mListNomorTelepon.clear();
                    for (PhoneNumber phoneNumber : mDetailDebiturViewModel.obsListPhoneNumber.get()) {
                        DialogTwoLineSpinnerAdapter.TwoLineSpinner twoLineSpinner = new DialogTwoLineSpinnerAdapter.TwoLineSpinner();
                        twoLineSpinner.title = phoneNumber.getJenisKontak();
                        twoLineSpinner.description = phoneNumber.getNomorKontak();
                        mListNomorTelepon.add(twoLineSpinner);
                    }
                    showPhoneNumberDialogSimpleSpinner(mListNomorTelepon, getString(R.string.DetailDebitur_PilihNomorTelepon), LIST_PHONE);
                }
            }
        });
        mDetailDebiturViewModel.obsCheckInSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.DetailDebitur_SuksesCheckIn);
            }
        });

        mDetailDebiturViewModel.getDetailDebitur(mNoRekening);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
            mCustomerReference = getIntent().getExtras().getString(EXTRA_CUSTOMER_REFERENCE);
            mType = getIntent().getExtras().getString(EXTRA_TYPE);
        }
    }

    private void displayResult(DetailDebitur detailDebitur) {
        mBinding.setDetailDebitur(detailDebitur);
    }

    @Optional
    @OnClick(R.id.image_btn_toolbar_more)
    public void onShortcutMenuClick(View view) {
        showShortcutMenu(view);
    }

    private void showShortcutMenu(View anchorView) {
        mPopUpMenu = new PopupMenu(this, anchorView);
        if (mType.equals(ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE)) {
            mPopUpMenu.getMenuInflater().inflate(R.menu.popup_menu, mPopUpMenu.getMenu());
        } else if (mType.equals(ListDebiturActivity.EXTRA_TYPE_TAMBAH_KONTAK_VALUE)) {
            mPopUpMenu.getMenuInflater().inflate(R.menu.popup_menu_tambah_kontak, mPopUpMenu.getMenu());
        } else if (mType.equals(ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE)) {
            mPopUpMenu.getMenuInflater().inflate(R.menu.popup_menu_account_assignment, mPopUpMenu.getMenu());
        }
        mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_menu_call: {
                        mDetailDebiturViewModel.getPhoneList(mNoRekening);
                        break;
                    }
                    case R.id.popup_menu_check_in: {
                        requestAccessLocationPermission();
                        break;
                    }
                    case R.id.popup_menu_isi_form_visit: {
                        startActivity(FormVisitActivity.instantiate(DetailDebiturActivity.this, mNoRekening));
                        break;
                    }
                    case R.id.popup_menu_lihat_history: {
                        Intent intent = new Intent(DetailDebiturActivity.this, HistoriTindakanActivity.class);
                        intent.putExtra(EXTRA_NO_REKENING, mNoRekening);
                        startActivity(intent);
                        break;
                    }
                    case R.id.popup_menu_tambah_telepon: {
                        startActivity(TambahTeleponActivity.instantiate(DetailDebiturActivity.this, mNoRekening, mCustomerReference));
                        break;
                    }
                    case R.id.popup_menu_view_telepon: {
                        startActivity(ViewTeleponActivity.instantiate(DetailDebiturActivity.this, mNoRekening, mCustomerReference));
                        break;
                    }
                    case R.id.popup_menu_tambah_alamat: {
                        startActivity(TambahAlamatActivity.instantiate(DetailDebiturActivity.this, mNoRekening, mCustomerReference));
                        break;
                    }
                    case R.id.popup_menu_gallery: {
                        openGallery();
                        break;
                    }
                }
                return true;
            }
        });

        mPopUpMenu.show();
    }

    private void openGallery() {

    }

    private static final int LIST_PHONE = 123;
    private void showPhoneNumberDialogSimpleSpinner(List<DialogTwoLineSpinnerAdapter.TwoLineSpinner> itemList, String dialogTitle, int viewId) {
        if (mListPhoneNumberDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mListPhoneNumberDialog = dialogBuilder.create();
        }
        if (itemList.size() > 0) {
            mListPhoneNumberDialog.show();
            RecyclerView recyclerView = (RecyclerView) mListPhoneNumberDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogTwoLineSpinnerAdapter adapter = new DialogTwoLineSpinnerAdapter(itemList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mListPhoneNumberDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(this, getString(R.string.TidakAdaDataPilihan));
        }
    }

    private static final int LIST_ADDRESS = 124;
    private void showAddressDialogSimpleSpinner(List<String> nameList, String dialogTitle, int viewId) {
        if (mListAddressDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mListAddressDialog = dialogBuilder.create();
        }
        if (nameList.size() > 0) {
            mListAddressDialog.show();
            RecyclerView recyclerView = (RecyclerView) mListAddressDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogSimpleSpinnerAdapter adapter = new DialogSimpleSpinnerAdapter(nameList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mListAddressDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(this, getString(R.string.TidakAdaDataPilihan));
        }
    }

    @Subscribe
    public void onDialogSimpleSpinnerSelected(EventDialogSimpleSpinnerSelected event) {
        if (event.getViewId() ==  LIST_PHONE) {
            if (mListPhoneNumberDialog != null && mListPhoneNumberDialog.isShowing()) {
                mListPhoneNumberDialog.dismiss();
                String phoneNumber = event.getName();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                startActivities(new Intent[] {
                        FormCallActivity.instantiate(DetailDebiturActivity.this, mNoRekening, phoneNumber),
                        intent});
            }
        } else if (event.getViewId() ==  LIST_ADDRESS) {
//            Uri gmmIntentUri = Uri.parse("geo:-6.1671626,106.8175127");
            if (mListAddressDialog != null && mListAddressDialog.isShowing()) {
                mListAddressDialog.dismiss();
                String address = event.getName();

                Uri gmmIntentUri;
                if (address.equals(getString(R.string.DetailDebitur_AlamatRumah))) {
                    gmmIntentUri = Uri.parse("geo:0,0?q=" + mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatRumah());
                } else if (address.equals(getString(R.string.DetailDebitur_AlamatKantor))) {
                    gmmIntentUri = Uri.parse("geo:0,0?q=" + mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatKantor());
                } else if (address.equals(getString(R.string.DetailDebitur_AlamatAgunan))) {
                    gmmIntentUri = Uri.parse("geo:0,0?q=" + mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatAgunan());
                } else {
                    gmmIntentUri = Uri.parse("geo:0,0?q=" + mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatSaatIni());
                }
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        }
    }

    @OnClick(R.id.fab_map)
    public void onFabMapClick(View view) {
        if (mDetailDebiturViewModel.obsDetailDebitur.get() != null) {
            List<String> addressList = new ArrayList<>();
            if (!TextUtils.isEmpty(mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatRumah())) {
                addressList.add(getString(R.string.DetailDebitur_AlamatRumah));
            }
            if (!TextUtils.isEmpty(mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatKantor())) {
                addressList.add(getString(R.string.DetailDebitur_AlamatKantor));
            }
            if (!TextUtils.isEmpty(mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatAgunan())) {
                addressList.add(getString(R.string.DetailDebitur_AlamatAgunan));
            }
            if (!TextUtils.isEmpty(mDetailDebiturViewModel.obsDetailDebitur.get().getAlamatSaatIni())) {
                addressList.add(getString(R.string.DetailDebitur_AlamatSaatIni));
            }

            showAddressDialogSimpleSpinner(addressList, getString(R.string.DetailDebitur_PilihAlamat), LIST_ADDRESS);
        }
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

//                                Get Address string
                                startIntentService(location);
                                mAddressRequested = true;
                            } else {
                                mDetailDebiturViewModel.obsIsLoading.set(false);
                            }
                        }
                    });
        } catch (SecurityException e) {
            mDetailDebiturViewModel.obsIsLoading.set(false);
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
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

            mDetailDebiturViewModel.checkIn(getUserId(), mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), mAddressOutput);

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }

}
