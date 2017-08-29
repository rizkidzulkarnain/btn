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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormVisitKonfirmasiBinding;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.ToastUtils;
import com.mitkoindo.smartcollection.utils.Utils;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/26/17.
 */

public class FormVisitKonfirmasiActivity extends BaseActivity {

    private static final String EXTRA_DATA_FORM_VISIT = "extra_data_form_visit";
    private static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_ADDRESS = "extra_address";

    private FormVisitKonfirmasiViewModel mFormVisitKonfirmasiViewModel;
    private ActivityFormVisitKonfirmasiBinding mBinding;

    private FormVisitBody.SpParameter mSpParameter;
    private String mNoRekening;
    private String mAddress;


    public static Intent instantiate(Context context, FormVisitBody.SpParameter spParameter, String noRekening, String alamatYangDikunjungi) {
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
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_visit_konfirmasi;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mFormVisitKonfirmasiViewModel = new FormVisitKonfirmasiViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormVisitKonfirmasiViewModel(mFormVisitKonfirmasiViewModel);
        mFormVisitKonfirmasiViewModel.spParameter = mSpParameter;

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
                displayMessage(R.string.GagalMenyimpanFormVisit);
            }
        });
        mFormVisitKonfirmasiViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormVisitKonfirmasiViewModel.obsIsSaveSuccess.get()) {
                    displayErrorDialog("", getString(R.string.FormVisit_SaveFormSuccess));
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
            mSpParameter = Parcels.unwrap(parcelable);

            mAddress = getIntent().getExtras().getString(EXTRA_ADDRESS);
        }
    }

    private void setFormVisitData() {
//        Set Tujuan
        List<DropDownPurpose> listDropDownPurpose = RealmHelper.getListDropDownPurpose();
        for (DropDownPurpose dropDownPurpose : listDropDownPurpose) {
            if (dropDownPurpose.getPId() != null && dropDownPurpose.getPId().equals(mSpParameter.getTujuan())) {
                if (dropDownPurpose.getPDesc() != null) {
                    mFormVisitKonfirmasiViewModel.tujuanKunjungan.set(dropDownPurpose.getPDesc());
                    break;
                }
            }
        }

//        Set Address
        mFormVisitKonfirmasiViewModel.alamatYangDikunjungi.set(mAddress);

//        Set nama orang yang dikunjungi
        mFormVisitKonfirmasiViewModel.namaOrangYangDikunjungi.set(mSpParameter.getPersonVisit());

//        Set Hubungan
        List<DropDownRelationship> listDropDownRelationship = RealmHelper.getListDropDownRelationship();
        for (DropDownRelationship dropDownRelationship : listDropDownRelationship) {
            if (dropDownRelationship.getRelId() != null && dropDownRelationship.getRelId().equals(mSpParameter.getPersonVisitRel())) {
                if (dropDownRelationship.getRelDesc() != null) {
                    mFormVisitKonfirmasiViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                    break;
                }
            }
        }

//        Set Hasil Kunjungan
        List<DropDownResult> listDropDownResult = RealmHelper.getListDropDownResult();
        for (DropDownResult dropDownResult : listDropDownResult) {
            if (dropDownResult.getResultId() != null && dropDownResult.getResultId().equals(mSpParameter.getResult())) {
                if (dropDownResult.getResultDesc() != null) {
                    mFormVisitKonfirmasiViewModel.hasilKunjungan.set(dropDownResult.getResultDesc());
                    break;
                }
            }
        }

//        Set tanggal janji debitur
        mFormVisitKonfirmasiViewModel.tanggalJanjiDebitur.set(Utils.changeDateFormat(
                    mSpParameter.getResultDate(),
                    Constant.DATE_FORMAT_SEND_DATE,
                    Constant.DATE_FORMAT_DISPLAY_DATE));

//        Set jumlah yang akan disetor
        mFormVisitKonfirmasiViewModel.jumlahYangAkanDisetor.set(Utils.convertDoubleToString(mSpParameter.getPtpAmount(), ".0"));

//        Set Status Agunan
        List<DropDownStatusAgunan> listDropDownStatusAgunan = RealmHelper.getListDropDownStatusAgunan();
        for (DropDownStatusAgunan dropDownStatusAgunan : listDropDownStatusAgunan) {
            if (dropDownStatusAgunan.getColstaCode() != null && dropDownStatusAgunan.getColstaCode().equals(mSpParameter.getCollStatDesc())) {
                if (dropDownStatusAgunan.getColstaDesc() != null) {
                    mFormVisitKonfirmasiViewModel.statusAgunan.set(dropDownStatusAgunan.getColstaDesc());
                    break;
                }
            }
        }

//        Set Kondisi Agunan
        mFormVisitKonfirmasiViewModel.kondisiAgunan.set(mSpParameter.getCollCondDesc());

//        Set Alasan Menunggak
        List<DropDownReason> listDropDownReason = RealmHelper.getListDropDownReason();
        for (DropDownReason dropDownReason : listDropDownReason) {
            if (dropDownReason.getReasonId() != null && dropDownReason.getReasonId().equals(mSpParameter.getReasonNonPayment())) {
                if (dropDownReason.getReasonDesc() != null) {
                    mFormVisitKonfirmasiViewModel.alasanMenunggak.set(dropDownReason.getReasonDesc());
                    break;
                }
            }
        }

//        Set Tindak Lanjut
        List<DropDownAction> listDropDownAction = RealmHelper.getListDropDownAction();
        for (DropDownAction dropDownAction : listDropDownAction) {
            if (dropDownAction.getActionId() != null && dropDownAction.getActionId().equals(mSpParameter.getNextAction())) {
                if (dropDownAction.getActionDesc() != null) {
                    mFormVisitKonfirmasiViewModel.tindakLanjut.set(dropDownAction.getActionDesc());
                    break;
                }
            }
        }

//        Set Tanggal Tindak Lanjut
        mFormVisitKonfirmasiViewModel.tanggalTindakLanjut.set(Utils.changeDateFormat(
                mSpParameter.getNextActionDate(),
                Constant.DATE_FORMAT_SEND_DATE,
                Constant.DATE_FORMAT_DISPLAY_DATE));

//        Set catatan
        mFormVisitKonfirmasiViewModel.catatan.set(mSpParameter.getNotes());

//        Set Foto Debitur
        int widthHeight = Utils.convertDensityPixel(90, getResources());
        if (!TextUtils.isEmpty(mSpParameter.getPhotoDebiturPath())) {
            Bitmap resizeBmp = Utils.decodeSampledBitmapFromFile(mSpParameter.getPhotoDebiturPath(), widthHeight, widthHeight);
            int rotate = Utils.getOrientationFromExif(mSpParameter.getPhotoDebiturPath());
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
        if (!TextUtils.isEmpty(mSpParameter.getPhotoAgunan1Path())) {
            Bitmap resizeBmpAgunan1 = Utils.decodeSampledBitmapFromFile(mSpParameter.getPhotoAgunan1Path(), widthHeight, widthHeight);
            int rotate1 = Utils.getOrientationFromExif(mSpParameter.getPhotoAgunan1Path());
            if (rotate1 > 0) {
                int w = resizeBmpAgunan1.getWidth();
                int h = resizeBmpAgunan1.getHeight();

                Matrix mtx = new Matrix();
                mtx.preRotate(rotate1);
                resizeBmpAgunan1 = Bitmap.createBitmap(resizeBmpAgunan1, 0, 0, w, h, mtx, false);
                resizeBmpAgunan1 = resizeBmpAgunan1.copy(Bitmap.Config.ARGB_8888, true);
            }
            mBinding.cardViewFotoAgunan1.setVisibility(View.VISIBLE);
            mBinding.imageViewFotoAgunan1.setImageBitmap(resizeBmpAgunan1);
        } else {
            mBinding.cardViewFotoAgunan1.setVisibility(View.GONE);
        }

//        Set Foto Agunan 2
        if (!TextUtils.isEmpty(mSpParameter.getPhotoAgunan2Path())) {
            Bitmap resizeBmpAgunan2 = Utils.decodeSampledBitmapFromFile(mSpParameter.getPhotoAgunan2Path(), widthHeight, widthHeight);
            int rotate2 = Utils.getOrientationFromExif(mSpParameter.getPhotoAgunan2Path());
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
            mFormVisitKonfirmasiViewModel.spParameter.setSignaturePath(file.getAbsolutePath());
            result = true;

            mFormVisitKonfirmasiViewModel.saveFormVisit(getAccessToken());
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
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_SIGNATURE,
                getString(R.string.FormVisit_external_storage_permission_description));
    }
}
