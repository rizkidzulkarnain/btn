package com.mitkoindo.smartcollection.module.formvisit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mitkoindo.smartcollection.FetchAddressIntentService;
import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormVisitKonfirmasiBinding;
import com.mitkoindo.smartcollection.dialog.DialogFactory;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.FileUtils;
import com.mitkoindo.smartcollection.utils.ToastUtils;
import com.mitkoindo.smartcollection.utils.Utils;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.OnClick;

import static com.mitkoindo.smartcollection.FetchAddressIntentService.Constants.SUCCESS_RESULT;

/**
 * Created by ericwijaya on 8/26/17.
 */

public class FormVisitKonfirmasiActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String EXTRA_DATA_FORM_VISIT = "extra_data_form_visit";
    private static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_ADDRESS = "extra_address";
    private static final int REQ_CODE_LOCATION_PERMISSION = 1;

    private FormVisitKonfirmasiViewModel mFormVisitKonfirmasiViewModel;
    private ActivityFormVisitKonfirmasiBinding mBinding;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private FormVisitKonfirmasiActivity.AddressResultReceiver mResultReceiver;

    private SpParameterFormVisitDb mSpParameterFormVisitDb;
    private String mNoRekening;
    private String mAddress;
    private String mAddressOutput;
    private boolean mAddressRequested;
    private boolean mIsSignaturePadSigned = false;
    private Location mLastKnownLocation;


    public static Intent instantiate(Context context, SpParameterFormVisitDb spParameter, String noRekening, String alamatYangDikunjungi) {
        Intent intent = new Intent(context, FormVisitKonfirmasiActivity.class);
        intent.putExtra(EXTRA_DATA_FORM_VISIT, Parcels.wrap(spParameter));
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_ADDRESS, alamatYangDikunjungi);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormKonfirmasi_PageTitle));
        initSignaturePad();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_visit_konfirmasi;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();
        initGoogleService();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new FormVisitKonfirmasiActivity.AddressResultReceiver(new Handler());
        mAddressOutput = "";
        mAddressRequested = false;

        mFormVisitKonfirmasiViewModel = new FormVisitKonfirmasiViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormVisitKonfirmasiViewModel(mFormVisitKonfirmasiViewModel);
        mFormVisitKonfirmasiViewModel.spParameterFormVisitDb = mSpParameterFormVisitDb;

        mFormVisitKonfirmasiViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitKonfirmasiViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mFormVisitKonfirmasiViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                DialogFactory.createDialog(FormVisitKonfirmasiActivity.this,
                        getString(R.string.FormKonfirmasi_PageTitle),
                        getString(R.string.GagalMenyimpanFormVisit),
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(HomeActivity.instantiateClearTask(FormVisitKonfirmasiActivity.this));
                            }
                        }).show();
            }
        });
        mFormVisitKonfirmasiViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitKonfirmasiViewModel.obsIsSaveSuccess.get()) {
//                    Delete file photo
                    if (!TextUtils.isEmpty(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoDebiturPath())) {
                        FileUtils.deleteFile(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoDebiturPath());
                    }
                    if (!TextUtils.isEmpty(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoAgunan1Path())) {
                        FileUtils.deleteFile(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoAgunan1Path());
                    }
                    if (!TextUtils.isEmpty(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoAgunan2Path())) {
                        FileUtils.deleteFile(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoAgunan2Path());
                    }
                    if (!TextUtils.isEmpty(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoSignaturePath())) {
                        FileUtils.deleteFile(mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getPhotoSignaturePath());
                    }

                    displayMessage(R.string.FormVisit_SaveFormSuccess);
                    startActivity(HomeActivity.instantiateClearTask(FormVisitKonfirmasiActivity.this));
                }
            }
        });

        setFormVisitData();
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
            Parcelable parcelable = getIntent().getParcelableExtra(EXTRA_DATA_FORM_VISIT);
            mSpParameterFormVisitDb = Parcels.unwrap(parcelable);

            mAddress = getIntent().getExtras().getString(EXTRA_ADDRESS);
        }
    }

    private void setFormVisitData() {
//        Set Tujuan
        List<DropDownPurpose> listDropDownPurpose = RealmHelper.getListDropDownPurpose();
        for (DropDownPurpose dropDownPurpose : listDropDownPurpose) {
            if (dropDownPurpose.getPId() != null && dropDownPurpose.getPId().equals(mSpParameterFormVisitDb.getTujuan())) {
                if (dropDownPurpose.getPDesc() != null) {
                    mFormVisitKonfirmasiViewModel.tujuanKunjungan.set(dropDownPurpose.getPDesc());
                    break;
                }
            }
        }

//        Set Address
        mFormVisitKonfirmasiViewModel.alamatYangDikunjungi.set(mAddress);

//        Set nama orang yang dikunjungi
        mFormVisitKonfirmasiViewModel.namaOrangYangDikunjungi.set(mSpParameterFormVisitDb.getPersonVisit());

//        Set Hubungan
        List<DropDownRelationship> listDropDownRelationship = RealmHelper.getListDropDownRelationship();
        for (DropDownRelationship dropDownRelationship : listDropDownRelationship) {
            if (dropDownRelationship.getRelId() != null && dropDownRelationship.getRelId().equals(mSpParameterFormVisitDb.getPersonVisitRel())) {
                if (dropDownRelationship.getRelDesc() != null) {
                    mFormVisitKonfirmasiViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                    break;
                }
            }
        }

//        Set Hasil Kunjungan
        List<DropDownResult> listDropDownResult = RealmHelper.getListDropDownResult();
        for (DropDownResult dropDownResult : listDropDownResult) {
            if (dropDownResult.getResultId() != null && dropDownResult.getResultId().equals(mSpParameterFormVisitDb.getResult())) {
                if (dropDownResult.getResultDesc() != null) {
                    mFormVisitKonfirmasiViewModel.hasilKunjungan.set(dropDownResult.getResultDesc());
                    break;
                }
            }
        }

//        Set tanggal janji debitur
        if (mSpParameterFormVisitDb.getResultDate() != null) {
            mFormVisitKonfirmasiViewModel.tanggalJanjiDebitur.set(Utils.changeDateFormat(
                    mSpParameterFormVisitDb.getResultDate(),
                    Constant.DATE_FORMAT_SEND_DATE,
                    Constant.DATE_FORMAT_DISPLAY_DATE));

            mFormVisitKonfirmasiViewModel.obsIsShowTanggalJanjiDebitur.set(true);
        } else {
            mFormVisitKonfirmasiViewModel.obsIsShowTanggalJanjiDebitur.set(false);
        }

//        Set jumlah yang akan disetor
        if (mSpParameterFormVisitDb.getPtpAmount() == 0) {
            mFormVisitKonfirmasiViewModel.obsIsShowJumlahYangAkanDisetor.set(false);
        } else {
            mFormVisitKonfirmasiViewModel.jumlahYangAkanDisetor.set(Utils.convertDoubleToString(mSpParameterFormVisitDb.getPtpAmount(), ".0"));
            mFormVisitKonfirmasiViewModel.obsIsShowJumlahYangAkanDisetor.set(true);
        }

//        Set Status Agunan
        List<DropDownStatusAgunan> listDropDownStatusAgunan = RealmHelper.getListDropDownStatusAgunan();
        for (DropDownStatusAgunan dropDownStatusAgunan : listDropDownStatusAgunan) {
            if (dropDownStatusAgunan.getColstaCode() != null && dropDownStatusAgunan.getColstaCode().equals(mSpParameterFormVisitDb.getCollStatDesc())) {
                if (dropDownStatusAgunan.getColstaDesc() != null) {
                    mFormVisitKonfirmasiViewModel.statusAgunan.set(dropDownStatusAgunan.getColstaDesc());
                    break;
                }
            }
        }

//        Set Kondisi Agunan
        mFormVisitKonfirmasiViewModel.kondisiAgunan.set(mSpParameterFormVisitDb.getCollCondDesc());

//        Set Alasan Menunggak
        List<DropDownReason> listDropDownReason = RealmHelper.getListDropDownReason();
        for (DropDownReason dropDownReason : listDropDownReason) {
            if (dropDownReason.getReasonId() != null && dropDownReason.getReasonId().equals(mSpParameterFormVisitDb.getReasonNonPayment())) {
                if (dropDownReason.getReasonDesc() != null) {
                    mFormVisitKonfirmasiViewModel.alasanMenunggak.set(dropDownReason.getReasonDesc());
                    break;
                }
            }
        }

//        Set Tindak Lanjut
        List<DropDownAction> listDropDownAction = RealmHelper.getListDropDownAction();
        for (DropDownAction dropDownAction : listDropDownAction) {
            if (dropDownAction.getActionId() != null && dropDownAction.getActionId().equals(mSpParameterFormVisitDb.getNextAction())) {
                if (dropDownAction.getActionDesc() != null) {
                    mFormVisitKonfirmasiViewModel.tindakLanjut.set(dropDownAction.getActionDesc());
                    break;
                }
            }
        }

//        Set Tanggal Tindak Lanjut
        mFormVisitKonfirmasiViewModel.tanggalTindakLanjut.set(Utils.changeDateFormat(
                mSpParameterFormVisitDb.getNextActionDate(),
                Constant.DATE_FORMAT_SEND_DATE,
                Constant.DATE_FORMAT_DISPLAY_DATE));

//        Set catatan
        mFormVisitKonfirmasiViewModel.catatan.set(mSpParameterFormVisitDb.getNotes());

//        Set Foto Debitur
        int widthHeight = Utils.convertDensityPixel(90, getResources());
        if (!TextUtils.isEmpty(mSpParameterFormVisitDb.getPhotoDebiturPath())) {
            Bitmap resizeBmp = Utils.decodeSampledBitmapFromFile(mSpParameterFormVisitDb.getPhotoDebiturPath(), widthHeight, widthHeight);
            int rotate = Utils.getOrientationFromExif(mSpParameterFormVisitDb.getPhotoDebiturPath());
            if (rotate > 0) {
                int w = resizeBmp.getWidth();
                int h = resizeBmp.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);
                resizeBmp = Bitmap.createBitmap(resizeBmp, 0, 0, w, h, mtx, false);
                resizeBmp = resizeBmp.copy(Bitmap.Config.ARGB_8888, true);
            }
            mBinding.cardViewFotoDebitur.setVisibility(View.VISIBLE);
            mBinding.imageViewFotoDebitur.setImageBitmap(resizeBmp);
        } else {
            mBinding.cardViewFotoDebitur.setVisibility(View.GONE);
        }

//        Set Foto Agunan 1
        if (!TextUtils.isEmpty(mSpParameterFormVisitDb.getPhotoAgunan1Path())) {
            Bitmap resizeBmpAgunan1 = Utils.decodeSampledBitmapFromFile(mSpParameterFormVisitDb.getPhotoAgunan1Path(), widthHeight, widthHeight);
            int rotate1 = Utils.getOrientationFromExif(mSpParameterFormVisitDb.getPhotoAgunan1Path());
            if (rotate1 > 0) {
                int w = resizeBmpAgunan1.getWidth();
                int h = resizeBmpAgunan1.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate1);
                resizeBmpAgunan1 = Bitmap.createBitmap(resizeBmpAgunan1, 0, 0, w, h, mtx, false);
                resizeBmpAgunan1 = resizeBmpAgunan1.copy(Bitmap.Config.ARGB_8888, true);
            }
//            mBinding.cardViewFotoAgunan1.setVisibility(View.VISIBLE);
            mBinding.imageViewFotoAgunan1.setImageBitmap(resizeBmpAgunan1);
            mFormVisitKonfirmasiViewModel.isFotoAgunan1Show.set(true);
        } else {
//            mBinding.cardViewFotoAgunan1.setVisibility(View.GONE);
            mFormVisitKonfirmasiViewModel.isFotoAgunan1Show.set(false);
        }

//        Set Foto Agunan 2
        if (!TextUtils.isEmpty(mSpParameterFormVisitDb.getPhotoAgunan2Path())) {
            Bitmap resizeBmpAgunan2 = Utils.decodeSampledBitmapFromFile(mSpParameterFormVisitDb.getPhotoAgunan2Path(), widthHeight, widthHeight);
            int rotate2 = Utils.getOrientationFromExif(mSpParameterFormVisitDb.getPhotoAgunan2Path());
            if (rotate2 > 0) {
                int w = resizeBmpAgunan2.getWidth();
                int h = resizeBmpAgunan2.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate2);
                resizeBmpAgunan2 = Bitmap.createBitmap(resizeBmpAgunan2, 0, 0, w, h, mtx, false);
                resizeBmpAgunan2 = resizeBmpAgunan2.copy(Bitmap.Config.ARGB_8888, true);
            }
            mBinding.imageViewFotoAgunan2.setImageBitmap(resizeBmpAgunan2);
            mFormVisitKonfirmasiViewModel.isFotoAgunan2Show.set(true);
        } else {
            mFormVisitKonfirmasiViewModel.isFotoAgunan2Show.set(false);
        }

//        Set Signature visibility
        String relationShipId = mSpParameterFormVisitDb.getPersonVisitRel();
        if ( relationShipId.equals(RestConstants.RELATIONSHIP_ID_YANG_BERSANGKUTAN_VALUE)
                || relationShipId.equals(RestConstants.RELATIONSHIP_ID_SUAMI_VALUE)
                || relationShipId.equals(RestConstants.RELATIONSHIP_ID_ISTRI_VALUE) ) {

            mFormVisitKonfirmasiViewModel.obsIsSignatureShow.set(true);
        } else {
            mFormVisitKonfirmasiViewModel.obsIsSignatureShow.set(false);
        }
    }

    private void initSignaturePad() {
        mBinding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                mIsSignaturePadSigned = true;
            }

            @Override
            public void onSigned() {
                mIsSignaturePadSigned = true;
            }

            @Override
            public void onClear() {
                mIsSignaturePadSigned = false;
            }
        });
    }

    final private int REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_SIGNATURE = 124;
    private void getPermission(final String aPermission, final int aRequestCode, String aDescription) {
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this, aPermission);
        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                ToastUtils.toastShort(this, aDescription);
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{aPermission},
                    aRequestCode);
            return;
        }
        saveSignatureToFile(mBinding.signaturePad.getSignatureBitmap());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_SIGNATURE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveSignatureToFile(mBinding.signaturePad.getSignatureBitmap());
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        ToastUtils.toastShort(this, getString(R.string.FormVisit_external_storage_permission_description));
                    } else {
                        new AlertDialog.Builder(this)
                                .setMessage(getString(R.string.FormVisit_setting_external_storage_permission))
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", FormVisitKonfirmasiActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.toastShort(FormVisitKonfirmasiActivity.this, getString(R.string.FormVisit_denied_external_storage_permission));
                                    }
                                })
                                .create()
                                .show();
                    }
                }
                break;
            }
            case REQ_CODE_LOCATION_PERMISSION: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestAccessLocationPermission();
                } else {
//                showLocationAndPopup(CENTRAL_JAKATAR_LAT, CENTRAL_JAKATAR_LONG, false);
//                if not get location permission
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean saveSignatureToFile(Bitmap signature) {
        boolean result = false;
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name));
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dir, "signature_" + mNoRekening +".jpg");
            saveBitmapToJPG(signature, file);
            mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setSignaturePath(file.getAbsolutePath());
            result = true;

            requestAccessLocationPermission();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        if (mFormVisitKonfirmasiViewModel.obsIsSignatureShow.get()) {
            if (mIsSignaturePadSigned) {
                getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_SIGNATURE,
                        getString(R.string.FormVisit_external_storage_permission_description));
            } else {
                displayMessage(getString(R.string.FormKonfirmasi_SignatureHint));
            }
        } else {
            requestAccessLocationPermission();
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

    private void getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;

                                mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setGeoLatitude(location.getLatitude());
                                mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setGeoLongitude(location.getLongitude());

                                if (mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.getResultDate() == null) {
                                    mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setResultDate("");
                                    mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setPtpAmount(0);
                                }

//                                Get Address string
                                startIntentService(location);
                                mAddressRequested = true;
                            } else {
                                mFormVisitKonfirmasiViewModel.obsIsLoading.set(false);
                            }
                        }
                    });
        } catch (SecurityException e) {
            mFormVisitKonfirmasiViewModel.obsIsLoading.set(false);
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

            mFormVisitKonfirmasiViewModel.spParameterFormVisitDb.setGeoAddress(mAddressOutput);

            mFormVisitKonfirmasiViewModel.saveFormVisit(getAccessToken());

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }
}
