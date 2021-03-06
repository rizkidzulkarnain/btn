package com.mitkoindo.smartcollection.module.formvisit;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormVisitBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.dialog.DialogTwoLineSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.objectdata.AddressNew;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressDb;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;
import com.mitkoindo.smartcollection.utils.ToastUtils;
import com.mitkoindo.smartcollection.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class FormVisitActivity extends BaseActivity {

    private static final String EXTRA_NO_REKENING = "extra_no_rekening";

    private FormVisitViewModel mFormVisitViewModel;
    private ActivityFormVisitBinding mBinding;
    private Dialog mSpinnerDialog;
    private Dialog mListAddressDialog;

    private List<DropDownPurpose> mListDropDownPurpose;
    private List<DropDownAddress> mListDropDownAddress;
    private List<AddressNew> mListAddressNew;
    private List<DropDownRelationship> mListDropDownRelationship;
    private List<DropDownResult> mListDropDownResult;
    private List<DropDownReason> mListDropDownReason;
    private List<DropDownAction> mListDropDownAction;
    private List<DropDownStatusAgunan> mListDropDownStatusAgunan;
    private List<String> mListTujuanKunjungan = new ArrayList<String>();
    private List<String> mListAlamatYangDikunjungi = new ArrayList<String>();
    private ArrayList<DialogTwoLineSpinnerAdapter.TwoLineSpinner> mListAddress = new ArrayList<>();
    private List<String> mListHubunganDenganDebitur = new ArrayList<String>();
    private List<String> mListHasilKunjungan = new ArrayList<String>();
    private List<String> mListAlasanMenunggak = new ArrayList<String>();
    private List<String> mListTindakLanjut = new ArrayList<String>();
    private List<String> mListStatusAgunan = new ArrayList<String>();
    private List<String> mListKondisiAgunan = new ArrayList<String>();

    private String mNoRekening;


    public static Intent instantiate(Context context, String noRekening) {
        Intent intent = new Intent(context, FormVisitActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormVisit_PageTitle));
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
        return R.layout.activity_form_visit;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mFormVisitViewModel = new FormVisitViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormVisitViewModel(mFormVisitViewModel);

        mFormVisitViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mFormVisitViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
//                Failed to get address data from API, using data Address from bundle
                List<DropDownAddress> dropDownAddressList = new ArrayList<DropDownAddress>();
                List<DropDownAddressDb> dropDownAddressDbList = RealmHelper.getAddress(mNoRekening);
                for (DropDownAddressDb dropDownAddressDb : dropDownAddressDbList) {
                    dropDownAddressList.add(dropDownAddressDb.toDropDownAddress());
                }

                mFormVisitViewModel.mObsListDropDownAddress.set(dropDownAddressList);
            }
        });
        mFormVisitViewModel.errorSave.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMenyimpanFormVisit);
            }
        });
        mFormVisitViewModel.mObsListDropDownAddress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitViewModel.mObsListDropDownAddress.get() != null) {
                    mListDropDownAddress = mFormVisitViewModel.mObsListDropDownAddress.get();
                    mListAddress.clear();
                    for (DropDownAddress dropDownAddress : mListDropDownAddress) {
                        if (dropDownAddress.getAlamat() != null) {
                            mListAlamatYangDikunjungi.add(dropDownAddress.getAlamat());

                            DialogTwoLineSpinnerAdapter.TwoLineSpinner twoLineSpinner = new DialogTwoLineSpinnerAdapter.TwoLineSpinner();
                            twoLineSpinner.title = dropDownAddress.getAtDesc();
                            twoLineSpinner.description = dropDownAddress.getAlamat();
                            mListAddress.add(twoLineSpinner);
                        }
                    }
                }
            }
        });
        mFormVisitViewModel.mObsListAddressNew.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitViewModel.mObsListAddressNew.get() != null) {
                    mListAddressNew = mFormVisitViewModel.mObsListAddressNew.get();
                    mListAddress.clear();
                    for (AddressNew addressNew : mListAddressNew) {
                        if (addressNew.getAlamat() != null) {
                            mListAlamatYangDikunjungi.add(addressNew.getAlamat());

                            DialogTwoLineSpinnerAdapter.TwoLineSpinner twoLineSpinner = new DialogTwoLineSpinnerAdapter.TwoLineSpinner();
                            twoLineSpinner.title = addressNew.getTipeAlamat();
                            twoLineSpinner.description = addressNew.getAlamat();
                            mListAddress.add(twoLineSpinner);
                        }
                    }
                }
            }
        });
        mFormVisitViewModel.jumlahYangAkanDisetor.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    Double jumlahYangAkanDisetor = Double.parseDouble(mFormVisitViewModel.jumlahYangAkanDisetor.get());
                    mFormVisitViewModel.spParameterFormVisitDb.setPtpAmount(jumlahYangAkanDisetor);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                    mFormVisitViewModel.spParameterFormVisitDb.setPtpAmount(0);
                }
            }
        });
        mFormVisitViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitViewModel.obsIsSaveSuccess.get()) {
                    displayErrorDialog("", getString(R.string.FormVisit_SaveFormSuccess));
                    startActivity(HomeActivity.instantiateClearTask(FormVisitActivity.this));
                }
            }
        });
        mFormVisitViewModel.isFotoAgunan1Show.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!mFormVisitViewModel.isFotoAgunan1Show.get()) {
                    mBinding.imageViewFotoAgunan1.setImageResource(R.drawable.ic_home_variant_grey600_48dp);
                    mFormVisitViewModel.spParameterFormVisitDb.setPhotoAgunan1Path("");
                    mFormVisitViewModel.isFotoAgunan2Show.set(false);
                }
            }
        });
        mFormVisitViewModel.isFotoAgunan2Show.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!mFormVisitViewModel.isFotoAgunan2Show.get()) {
                    mBinding.imageViewFotoAgunan2.setImageResource(R.drawable.ic_home_variant_grey600_48dp);
                    mFormVisitViewModel.spParameterFormVisitDb.setPhotoAgunan2Path("");
                }
            }
        });
        mFormVisitViewModel.hasilKunjungan.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String hasilKunjungan = mFormVisitViewModel.hasilKunjungan.get();
                String hasilKunjunganId = "";
                for (DropDownResult dropDownResult : mListDropDownResult) {
                    if (!TextUtils.isEmpty(dropDownResult.getResultDesc()) && dropDownResult.getResultDesc().equals(hasilKunjungan)) {
                        hasilKunjunganId = dropDownResult.getResultId();
                        break;
                    }
                }

//                Jika hasil kunjungan = akan setor tanggal / akan datang ke btn tanggal / minta dihubungi tanggal, maka show field tanggal janji debitur
                if (hasilKunjunganId.equals(RestConstants.RESULT_ID_AKAN_SETOR_TANGGAL_VALUE)
                        || hasilKunjunganId.equals(RestConstants.RESULT_ID_AKAN_DATANG_KE_BTN_TANGGAL_VALUE)
                        || hasilKunjunganId.equals(RestConstants.RESULT_ID_MINTA_DIHUBUNGI_TANGGAL_VALUE)) {

                    mFormVisitViewModel.obsIsShowTanggalJanjiDebitur.set(true);

//                    Jika hasil kunjungan = akan setor tanggal, maka show field jumlah yang akan disetor
                    if (hasilKunjunganId.equals(RestConstants.RESULT_ID_AKAN_SETOR_TANGGAL_VALUE)) {
                        mFormVisitViewModel.obsIsShowJumlahYangAkanDisetor.set(true);
                        mFormVisitViewModel.tanggalJanjiDebitur.set(getString(R.string.FormVisit_TanggalJanjiDebiturInitial));
                    } else {
                        mFormVisitViewModel.obsIsShowJumlahYangAkanDisetor.set(false);
                        mFormVisitViewModel.tanggalJanjiDebitur.set(getString(R.string.FormVisit_TanggalRealisasiJanjiInitial));
                    }
                } else {
                    mFormVisitViewModel.obsIsShowTanggalJanjiDebitur.set(false);
                    mFormVisitViewModel.obsIsShowJumlahYangAkanDisetor.set(false);
                }

                mFormVisitViewModel.spParameterFormVisitDb.setResultDate(null);
                mFormVisitViewModel.spParameterFormVisitDb.setPtpAmount(0);
                mFormVisitViewModel.jumlahYangAkanDisetor.set("");
            }
        });

        mFormVisitViewModel.getListAddress(getAccessToken(), mNoRekening);
//        mFormVisitViewModel.getListAddressNew(getAccessToken(), mNoRekening);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
        }
    }

    private void initArrayDropDown() {
        mListDropDownPurpose = RealmHelper.getListDropDownPurpose();
        for (DropDownPurpose dropDownPurpose : mListDropDownPurpose) {
            if (dropDownPurpose.getPDesc() != null) {
                mListTujuanKunjungan.add(dropDownPurpose.getPDesc());
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
                mListHasilKunjungan.add(dropDownResult.getResultDesc());
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

        mListDropDownStatusAgunan = RealmHelper.getListDropDownStatusAgunan();
        for (DropDownStatusAgunan dropDownStatusAgunan : mListDropDownStatusAgunan) {
            if (dropDownStatusAgunan.getColstaDesc() != null) {
                mListStatusAgunan.add(dropDownStatusAgunan.getColstaDesc());
            }
        }

        mListKondisiAgunan.add(getString(R.string.FormVisit_Terawat));
        mListKondisiAgunan.add(getString(R.string.FormVisit_TidakTerawat));
    }

    private void initNoSelectValue() {
        mFormVisitViewModel.tujuanKunjungan.set(getString(R.string.FormVisit_TujuanKunjunganInitial));
        mFormVisitViewModel.alamatYangDikunjungi.set(getString(R.string.FormVisit_AlamatYangDikunjungiInitial));
        mFormVisitViewModel.hubunganDenganDebitur.set(getString(R.string.FormVisit_HubunganDenganDebiturInitial));
        mFormVisitViewModel.hasilKunjungan.set(getString(R.string.FormVisit_HasilKunjunganInitial));
        mFormVisitViewModel.tanggalJanjiDebitur.set(getString(R.string.FormVisit_TanggalJanjiDebiturInitial));
        mFormVisitViewModel.statusAgunan.set(getString(R.string.FormVisit_StatusAgunanInitial));
        mFormVisitViewModel.kondisiAgunan.set(getString(R.string.FormVisit_KondisiAgunanInitial));
        mFormVisitViewModel.alasanMenunggak.set(getString(R.string.FormVisit_AlasanMenunggakInitial));
        mFormVisitViewModel.tindakLanjut.set(getString(R.string.FormVisit_TindakLanjutInitial));
        mFormVisitViewModel.tanggalTindakLanjut.set(getString(R.string.FormVisit_TanggalTindakLanjutInitial));
        mFormVisitViewModel.isFotoAgunan1Show.set(false);
        mFormVisitViewModel.isFotoAgunan2Show.set(false);
    }


    final private int REQ_CODE_CAMERA_PERMISSION = 55;
    private void requestCameraPermission() {
        if (!isCameraPermissionGranted()) {
            ToastUtils.toastShort(this, getString(R.string.FormVisit_camera_permission_description));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_CAMERA_PERMISSION);
        } else {
            getPicture(mViewId);
        }
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    final private int REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE = 123;
    private void getPermission(final String aPermission, final int aRequestCode, String aDescription) {
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this, aPermission);
        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                ToastUtils.toastShort(this, aDescription);
            }
            ActivityCompat.requestPermissions(this, new String[]{aPermission}, aRequestCode);
            return;
        }

        requestCameraPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
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
                                        Uri uri = Uri.fromParts("package", FormVisitActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.toastShort(FormVisitActivity.this, getString(R.string.FormVisit_denied_external_storage_permission));
                                    }
                                })
                                .create()
                                .show();
                    }
                }
                break;
            }
            case REQ_CODE_CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        ToastUtils.toastShort(this, getString(R.string.FormVisit_camera_permission_description));
                    } else {
                        new AlertDialog.Builder(this)
                                .setMessage(getString(R.string.FormVisit_setting_camera_permission))
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", FormVisitActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.toastShort(FormVisitActivity.this, getString(R.string.FormVisit_denied_camera_permission));
                                    }
                                })
                                .create()
                                .show();
                    }
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final int ACTION_TAKE_PICTURE_DEBITUR = 1;
    private static final int ACTION_TAKE_PICTURE_AGUNAN_1 = 2;
    private static final int ACTION_TAKE_PICTURE_AGUNAN_2 = 3;
    private String mFotoDebiturFilePath;
    private String mFotoAgunan1FilePath;
    private String mFotoAgunan2FilePath;
    private int mViewId;

    private void getPicture(int aViewId) {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name));
        if (!dir.exists()) {
            dir.mkdir();
        }

        switch (aViewId) {
            case R.id.button_foto_debitur: {
                File file = new File(dir, getString(R.string.app_name) + getString(R.string.FormVisit_NamaFileFotoDebitur));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                mFotoDebiturFilePath = file.getAbsolutePath();
                startActivityForResult(intent, ACTION_TAKE_PICTURE_DEBITUR);
                break;
            }
            case R.id.button_foto_agunan_1: {
                File file = new File(dir, getString(R.string.app_name) + getString(R.string.FormVisit_NamaFileFotoAgunan1));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                mFotoAgunan1FilePath = file.getAbsolutePath();
                startActivityForResult(intent, ACTION_TAKE_PICTURE_AGUNAN_1);
                break;
            }
            case R.id.button_foto_agunan_2: {
                File file = new File(dir, getString(R.string.app_name) + getString(R.string.FormVisit_NamaFileFotoAgunan2));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                mFotoAgunan2FilePath = file.getAbsolutePath();
                startActivityForResult(intent, ACTION_TAKE_PICTURE_AGUNAN_2);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_TAKE_PICTURE_DEBITUR:
                case ACTION_TAKE_PICTURE_AGUNAN_1:
                case ACTION_TAKE_PICTURE_AGUNAN_2: {
                    File dir = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name));
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    String imagePath;
                    File file;
                    if (requestCode == ACTION_TAKE_PICTURE_DEBITUR) {
                        imagePath = mFotoDebiturFilePath;
                        file = new File(dir, "foto_debitur_" + mNoRekening + ".jpg");
                    }
                    else if (requestCode == ACTION_TAKE_PICTURE_AGUNAN_1) {
                        imagePath = mFotoAgunan1FilePath;
                        file = new File(dir, "foto_agunan_1_" + mNoRekening + ".jpg");
                    }
                    else {
                        imagePath = mFotoAgunan2FilePath;
                        file = new File(dir, "foto_agunan_2_" + mNoRekening +".jpg");
                    }
                    if (file.exists()) {
                        file.delete();
                    }

                    int widthHeight = Utils.convertDensityPixel(90, getResources());
                    Bitmap resizeBmp = Utils.decodeSampledBitmapFromFile(imagePath, widthHeight, widthHeight);
                    int rotate = Utils.getOrientationFromExif(imagePath);
                    if (rotate > 0) {
                        int w = resizeBmp.getWidth();
                        int h = resizeBmp.getHeight();

                        Matrix mtx = new Matrix();
                        mtx.preRotate(rotate);
                        resizeBmp = Bitmap.createBitmap(resizeBmp, 0, 0, w, h, mtx, false);
                        resizeBmp = resizeBmp.copy(Bitmap.Config.ARGB_8888, true);
                    }

                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(file);
                        resizeBmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        out.flush();
                        out.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (requestCode == ACTION_TAKE_PICTURE_DEBITUR) {
                        mBinding.imageViewFotoDebitur.setImageBitmap(resizeBmp);
                        mFormVisitViewModel.spParameterFormVisitDb.setPhotoDebiturPath(file.getAbsolutePath());
                    } else if (requestCode == ACTION_TAKE_PICTURE_AGUNAN_1) {
                        mBinding.imageViewFotoAgunan1.setImageBitmap(resizeBmp);
                        mFormVisitViewModel.spParameterFormVisitDb.setPhotoAgunan1Path(file.getAbsolutePath());
                    } else {
                        mBinding.imageViewFotoAgunan2.setImageBitmap(resizeBmp);
                        mFormVisitViewModel.spParameterFormVisitDb.setPhotoAgunan2Path(file.getAbsolutePath());
                    }
                    break;
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    Show Dialog DropDown
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

    private void showAddressDialogSimpleSpinner(List<DialogTwoLineSpinnerAdapter.TwoLineSpinner> itemList, String dialogTitle, int viewId) {
        if (mListAddressDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mListAddressDialog = dialogBuilder.create();
        }
        if (itemList.size() > 0) {
            mListAddressDialog.show();
            RecyclerView recyclerView = (RecyclerView) mListAddressDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogTwoLineSpinnerAdapter adapter = new DialogTwoLineSpinnerAdapter(itemList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mListAddressDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(this, getString(R.string.TidakAdaDataAlamat));
        }
    }

//    Event ketika DropDown item dipilih
    @Subscribe
    public void onDialogSimpleSpinnerSelected(EventDialogSimpleSpinnerSelected event) {
        if (mSpinnerDialog != null && mSpinnerDialog.isShowing()) {
            mSpinnerDialog.dismiss();
        }
        if (mListAddressDialog != null && mListAddressDialog.isShowing()) {
            mListAddressDialog.dismiss();
        }

        switch (event.getViewId()) {
            case R.id.card_view_tujuan_kunjungan: {
                for (DropDownPurpose dropDownPurpose : mListDropDownPurpose) {
                    if (!TextUtils.isEmpty(dropDownPurpose.getPDesc()) && dropDownPurpose.getPDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setTujuan(dropDownPurpose.getPId());
                        mFormVisitViewModel.tujuanKunjungan.set(dropDownPurpose.getPDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_alamat_yang_dikunjungi: {
                for (DropDownAddress dropDownAddress : mListDropDownAddress) {
                    if (!TextUtils.isEmpty(dropDownAddress.getAlamat()) && event.getName().contains(dropDownAddress.getAlamat())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setCuAddr(dropDownAddress.getCaAddrType());
                        mFormVisitViewModel.alamatYangDikunjungi.set(event.getName());

//                        Tambah Kondisi Alamat, Munculkan tambah foto agunan jika Alamat Sekarang / Alamat Agunan / Alamat KTP, jika type alamat lain tidak perlu foto Agunan
                        if (dropDownAddress.getCaAddrType().equals(RestConstants.TYPE_ALAMAT_ID_ALAMAT_SEKARANG_VALUE)
                                || dropDownAddress.getCaAddrType().equals(RestConstants.TYPE_ALAMAT_ID_ALAMAT_AGUNAN_VALUE)
                                || dropDownAddress.getCaAddrType().equals(RestConstants.TYPE_ALAMAT_ID_ALAMAT_KTP_VALUE) ) {
                            mFormVisitViewModel.isFotoAgunan1Show.set(true);
                        } else {
                            mFormVisitViewModel.isFotoAgunan1Show.set(false);
                        }
                        break;
                    }
                }
//                for (AddressNew addressNew : mListAddressNew) {
//                    if (!TextUtils.isEmpty(addressNew.getAlamat()) && event.getName().contains(addressNew.getAlamat())) {
//                        mFormVisitViewModel.spParameterFormVisitDb.setCuAddr(addressNew.getKodeAlamat());
//                        mFormVisitViewModel.alamatYangDikunjungi.set(event.getName());
//                        break;
//                    }
//                }

                break;
            }
            case R.id.card_view_hubungan_dengan_debitur: {
                for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
                    if (!TextUtils.isEmpty(dropDownRelationship.getRelDesc()) && dropDownRelationship.getRelDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setPersonVisitRel(dropDownRelationship.getRelId());
                        mFormVisitViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_hasil_kunjungan: {
                for (DropDownResult dropDownResult : mListDropDownResult) {
                    if (!TextUtils.isEmpty(dropDownResult.getResultDesc()) && dropDownResult.getResultDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setResult(dropDownResult.getResultId());
                        mFormVisitViewModel.hasilKunjungan.set(dropDownResult.getResultDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_alasan_menunggak: {
                for (DropDownReason dropDownReason : mListDropDownReason) {
                    if (!TextUtils.isEmpty(dropDownReason.getReasonDesc()) && dropDownReason.getReasonDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setReasonNonPayment(dropDownReason.getReasonId());
                        mFormVisitViewModel.spParameterFormVisitDb.setReasonNonPaymentDesc(dropDownReason.getReasonDesc());
                        mFormVisitViewModel.alasanMenunggak.set(dropDownReason.getReasonDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_tindak_lanjut: {
                for (DropDownAction dropDownAction : mListDropDownAction) {
                    if (!TextUtils.isEmpty(dropDownAction.getActionDesc()) && dropDownAction.getActionDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setNextAction(dropDownAction.getActionId());
                        mFormVisitViewModel.tindakLanjut.set(dropDownAction.getActionDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_status_agunan: {
                for (DropDownStatusAgunan dropDownStatusAgunan : mListDropDownStatusAgunan) {
                    if (!TextUtils.isEmpty(dropDownStatusAgunan.getColstaDesc()) && dropDownStatusAgunan.getColstaDesc().equals(event.getName())) {
                        mFormVisitViewModel.spParameterFormVisitDb.setCollStatDesc(dropDownStatusAgunan.getColstaCode());
                        mFormVisitViewModel.statusAgunan.set(dropDownStatusAgunan.getColstaDesc());
                        break;
                    }
                }
                break;
            }
            case R.id.card_view_kondisi_agunan: {
                mFormVisitViewModel.spParameterFormVisitDb.setCollCondDesc(event.getName());
                mFormVisitViewModel.kondisiAgunan.set(event.getName());
                break;
            }
        }
    }

    @OnClick(R.id.card_view_tujuan_kunjungan)
    public void onTujuanKunjunganClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListTujuanKunjungan, getString(R.string.FormVisit_TujuanKunjunganInitial), R.id.card_view_tujuan_kunjungan);
    }

    @OnClick(R.id.card_view_alamat_yang_dikunjungi)
    public void onAlamatYangDikunjungiClicked(View view) {
//        showInstallmentDialogSimpleSpinner(mListAlamatYangDikunjungi, getString(R.string.FormVisit_AlamatYangDikunjungiInitial), R.id.card_view_alamat_yang_dikunjungi);
        showAddressDialogSimpleSpinner(mListAddress, getString(R.string.FormVisit_AlamatYangDikunjungiInitial), R.id.card_view_alamat_yang_dikunjungi);
    }

    @OnClick(R.id.card_view_hubungan_dengan_debitur)
    public void onHubunganDenganDebiturClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHubunganDenganDebitur, getString(R.string.FormVisit_HubunganDenganDebiturInitial), R.id.card_view_hubungan_dengan_debitur);
    }

    @OnClick(R.id.card_view_hasil_kunjungan)
    public void onHasilKunjunganClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHasilKunjungan, getString(R.string.FormVisit_HasilKunjunganInitial), R.id.card_view_hasil_kunjungan);
    }

    @OnClick(R.id.card_view_alasan_menunggak)
    public void onAlasanMenunggakClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListAlasanMenunggak, getString(R.string.FormVisit_AlasanMenunggakInitial), R.id.card_view_alasan_menunggak);
    }

    @OnClick(R.id.card_view_tindak_lanjut)
    public void onTindakLanjutClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListTindakLanjut, getString(R.string.FormVisit_TindakLanjutInitial), R.id.card_view_tindak_lanjut);
    }

    @OnClick(R.id.card_view_status_agunan)
    public void onStatusAgunanClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListStatusAgunan, getString(R.string.FormVisit_StatusAgunanInitial), R.id.card_view_status_agunan);
    }

    @OnClick(R.id.card_view_kondisi_agunan)
    public void onKondisiAgunanClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListKondisiAgunan, getString(R.string.FormVisit_KondisiAgunanInitial), R.id.card_view_kondisi_agunan);
    }

    @OnClick(R.id.card_view_tanggal_janji_debitur)
    public void onTanggalJanjiDebiturClicked(View view) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mFormVisitViewModel.setTanggalJanjiDebitur(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());

//        Get Date last day of the month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Calendar calendar15 = Calendar.getInstance();
        calendar15.add(Calendar.DATE, 15);

        long maxDate = calendar.getTime().getTime();
        int returnVal = Double.compare(calendar15.getTime().getTime(), calendar.getTime().getTime());
        if (returnVal < 0) {
            maxDate = calendar15.getTime().getTime();
        }

        datePickerDialog.getDatePicker().setMaxDate(maxDate);

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
            mFormVisitViewModel.setTanggalTindakLanjut(c.getTime());
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

    @OnClick(R.id.card_view_tambah_foto)
    public void onTambahFotoClicked(View view) {
        mFormVisitViewModel.isFotoAgunan2Show.set(!mFormVisitViewModel.isFotoAgunan2Show.get());
    }

    @OnClick(R.id.button_foto_debitur)
    public void onAmbilFotoDebiturClicked(View view) {
        mViewId = R.id.button_foto_debitur;
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE,
                getString(R.string.FormVisit_external_storage_permission_description));
    }

    @OnClick(R.id.button_foto_agunan_1)
    public void onAmbilFotoAgunan1Clicked(View view) {
        mViewId = R.id.button_foto_agunan_1;
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE,
                getString(R.string.FormVisit_external_storage_permission_description));
    }

    @OnClick(R.id.button_foto_agunan_2)
    public void onAmbilFotoAgunan2Clicked(View view) {
        mViewId = R.id.button_foto_agunan_2;
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE,
                getString(R.string.FormVisit_external_storage_permission_description));
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        if (isValid()) {
            startActivity(FormVisitKonfirmasiActivity.instantiate(FormVisitActivity.this,
                    mFormVisitViewModel.spParameterFormVisitDb,
                    mNoRekening,
                    mFormVisitViewModel.alamatYangDikunjungi.get()));
        }
    }

    private boolean isValid() {
        mFormVisitViewModel.spParameterFormVisitDb.setAccNo(mNoRekening);
        mFormVisitViewModel.spParameterFormVisitDb.setUserId(getUserId());
        SpParameterFormVisitDb spParameter = mFormVisitViewModel.spParameterFormVisitDb;
        if (TextUtils.isEmpty(spParameter.getTujuan())) {
            displayMessage(getString(R.string.FormVisit_TujuanKunjunganInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getCuAddr())) {
            displayMessage(getString(R.string.FormVisit_AlamatYangDikunjungiInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getPersonVisit())) {
            displayMessage(getString(R.string.FormVisit_OrangYangDiKunjungiHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getPersonVisitRel())) {
            displayMessage(getString(R.string.FormVisit_HubunganDenganDebiturInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getResult())) {
            displayMessage(getString(R.string.FormVisit_HasilKunjunganInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getResultDate()) && mFormVisitViewModel.obsIsShowTanggalJanjiDebitur.get()) {
            if (mFormVisitViewModel.obsIsShowJumlahYangAkanDisetor.get()) {
                displayMessage(getString(R.string.FormVisit_TanggalJanjiDebiturInitial));
            } else {
                displayMessage(getString(R.string.FormVisit_TanggalRealisasiJanjiInitial));
            }
            return false;
        } else if (spParameter.getPtpAmount() == 0 && mFormVisitViewModel.obsIsShowJumlahYangAkanDisetor.get()) {
            displayMessage(getString(R.string.FormVisit_JumlahYangAkanDisetorHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getCollStatDesc())) {
            displayMessage(getString(R.string.FormVisit_StatusAgunanInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getCollCondDesc())) {
            displayMessage(getString(R.string.FormVisit_KondisiAgunanInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getReasonNonPayment())) {
            displayMessage(getString(R.string.FormVisit_AlasanMenunggakInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getNextActionDate())) {
            displayMessage(getString(R.string.FormVisit_TanggalTindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getNextAction())) {
            displayMessage(getString(R.string.FormVisit_TindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getNotes())) {
            displayMessage(getString(R.string.FormVisit_CatatanHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getPhotoDebiturPath())) {
            displayMessage(getString(R.string.FormVisit_FotoDebiturHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getPhotoAgunan1Path()) && mFormVisitViewModel.isFotoAgunan1Show.get()) {
            displayMessage(getString(R.string.FormVisit_FotoAgunanHint));
            return false;
        }

        return true;
    }
}
