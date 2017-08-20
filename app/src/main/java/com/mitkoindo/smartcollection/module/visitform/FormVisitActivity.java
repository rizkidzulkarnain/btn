package com.mitkoindo.smartcollection.module.visitform;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
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
import android.view.View;
import android.widget.ArrayAdapter;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormVisitBinding;
import com.mitkoindo.smartcollection.utils.ToastUtils;
import com.mitkoindo.smartcollection.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class FormVisitActivity extends BaseActivity {

    private FormVisitViewModel mFormVisitViewModel;
    private ActivityFormVisitBinding mBinding;

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, FormVisitActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.FormVisit_PageTitle));
        initSpinner();
        initNoSelectValue();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_form_visit;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mFormVisitViewModel = new FormVisitViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setFormVisitViewModel(mFormVisitViewModel);
    }

    private void initSpinner() {
        List<String> listTujuanKunjungan = new ArrayList<String>();
        listTujuanKunjungan.add(getString(R.string.FormVisit_MengingatkanAngsuran));
        listTujuanKunjungan.add(getString(R.string.FormVisit_PenagihanKredit));
        listTujuanKunjungan.add(getString(R.string.FormVisit_Konfirmasi));
        listTujuanKunjungan.add(getString(R.string.FormVisit_IdentifikasiBarangJaminan));
        listTujuanKunjungan.add(getString(R.string.FormVisit_PemasanganPlakatPengawasanBank));
        listTujuanKunjungan.add(getString(R.string.FormVisit_PemasanganPlakatDijual));
        listTujuanKunjungan.add(getString(R.string.FormVisit_PemasanganPlakatDilelang));
        listTujuanKunjungan.add(getString(R.string.FormVisit_LainLain));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listTujuanKunjungan);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerTujuanKunjungan.setAdapter(dataAdapter);

        List<String> listAlamatYangDikunjungi = new ArrayList<String>();
        listAlamatYangDikunjungi.add("Jl. Papua 33, Jurangmangu Timur");
        listAlamatYangDikunjungi.add("Jl. Ceger 192 RT 005 / RW 012, Pondok Aren, Banten");
        listAlamatYangDikunjungi.add("Jl. Camar 21, Bintaro");

        ArrayAdapter<String> dataAdapterAlamatYangDikunjungi = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listAlamatYangDikunjungi);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerAlamatYangDikunjungi.setAdapter(dataAdapterAlamatYangDikunjungi);


        List<String> listHubunganDenganDebitur = new ArrayList<String>();
        listHubunganDenganDebitur.add("Yang bersangkutan");
        listHubunganDenganDebitur.add("Suami");
        listHubunganDenganDebitur.add("Istri");
        listHubunganDenganDebitur.add("Orang tua");
        listHubunganDenganDebitur.add("Mertua");
        listHubunganDenganDebitur.add("Anak");
        listHubunganDenganDebitur.add("Kakak kandung");
        listHubunganDenganDebitur.add("Adik kandung");
        listHubunganDenganDebitur.add("Kakek / Nenek kandung");
        listHubunganDenganDebitur.add("Kakek / Nenek suami / istri");

        ArrayAdapter<String> dataAdapterHubunganDenganDebitur = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listHubunganDenganDebitur);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerHubunganDenganDebitur.setAdapter(dataAdapterHubunganDenganDebitur);


        List<String> listHasilKunjungan = new ArrayList<String>();
        listHasilKunjungan.add(getString(R.string.FormVisit_AkanSetorTanggal));
        listHasilKunjungan.add(getString(R.string.FormVisit_AkanDatangKeBtnTanggal));
        listHasilKunjungan.add(getString(R.string.FormVisit_MintaDihubungiTanggal));
        listHasilKunjungan.add(getString(R.string.FormVisit_AkanMenjualBarangJaminan));
        listHasilKunjungan.add(getString(R.string.FormVisit_AkanMenjualAsetLainnya));
        listHasilKunjungan.add(getString(R.string.FormVisit_Lainnya));

        ArrayAdapter<String> dataAdapterHasilKunjungan = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listHasilKunjungan);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerHasilKunjungan.setAdapter(dataAdapterHasilKunjungan);


        List<String> listStatusAgunan = new ArrayList<String>();
        listStatusAgunan.add("Ditempati");
        listStatusAgunan.add("Tidak ditempati");
        listStatusAgunan.add("Disewakan");

        ArrayAdapter<String> dataAdapterStatusAgunan = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listStatusAgunan);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerStatusAgunan.setAdapter(dataAdapterStatusAgunan);

        List<String> listKondisiAgunan = new ArrayList<String>();
        listKondisiAgunan.add("Terawat");
        listKondisiAgunan.add("Terbengkalai");

        ArrayAdapter<String> dataAdapterKondisiAgunan = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listKondisiAgunan);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerKondisiAgunan.setAdapter(dataAdapterKondisiAgunan);


        List<String> listAlasanMenunggak = new ArrayList<String>();
        listAlasanMenunggak.add("Informasi kredit kurang");
        listAlasanMenunggak.add("Sedang diluar luar kota / negeri");
        listAlasanMenunggak.add("Kesalahan/keterlambatan transfer");
        listAlasanMenunggak.add("Fasilitas pembayaran bermasalah");

        ArrayAdapter<String> dataAdapterAlasanMenunggak = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listAlasanMenunggak);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerAlasanMenunggak.setAdapter(dataAdapterAlasanMenunggak);


        List<String> listTindakLanjut = new ArrayList<String>();
        listTindakLanjut.add("Surat");
        listTindakLanjut.add("Litigasi");
        listTindakLanjut.add("Restrukturisasi Kredit");
        listTindakLanjut.add("Klaim Asuransi");

        ArrayAdapter<String> dataAdapterTindakLanjut = new ArrayAdapter<String>(this, R.layout.item_simple_spinner, listTindakLanjut);
        dataAdapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        mBinding.spinnerTindakLanjut.setAdapter(dataAdapterTindakLanjut);
    }

    private void initNoSelectValue() {
        mFormVisitViewModel.tujuanKunjungan.set(getString(R.string.FormVisit_TujuanKunjunganInitial));
        mFormVisitViewModel.tanggalJanjiDebitur.set(getString(R.string.FormVisit_TanggalJanjiDebiturInitial));
        mFormVisitViewModel.tanggalTindakLanjut.set(getString(R.string.FormVisit_TanggalTindakLanjutInitial));
        mFormVisitViewModel.isFotoAgunan2Show.set(false);
    }

    final private int REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE = 123;
    private void getPermission(final String aPermission, final int aRequestCode, String aDescription, int aId) {
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
        getPicture(aId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPicture(mViewId);
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
                    String imagePath = "";
                    if (requestCode == ACTION_TAKE_PICTURE_DEBITUR) {
                        imagePath = mFotoDebiturFilePath;
                    }
                    else if (requestCode == ACTION_TAKE_PICTURE_AGUNAN_1) {
                        imagePath = mFotoAgunan1FilePath;
                    }
                    else {
                        imagePath = mFotoAgunan2FilePath;
                    }

                    int widthHeight = Utils.convertDensityPixel(96, getResources());
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
                    if (requestCode == ACTION_TAKE_PICTURE_DEBITUR) {
                        mBinding.imageViewFotoDebitur.setImageBitmap(resizeBmp);
                    } else if (requestCode == ACTION_TAKE_PICTURE_AGUNAN_1) {
                        mBinding.imageViewFotoAgunan1.setImageBitmap(resizeBmp);
                    } else {
                        mBinding.imageViewFotoAgunan2.setImageBitmap(resizeBmp);
                    }
                    break;
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
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

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
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
                getString(R.string.FormVisit_external_storage_permission_description), R.id.button_foto_debitur);
    }

    @OnClick(R.id.button_foto_agunan_1)
    public void onAmbilFotoAgunan1Clicked(View view) {
        mViewId = R.id.button_foto_agunan_1;
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE,
                getString(R.string.FormVisit_external_storage_permission_description), R.id.button_foto_agunan_1);
    }

    @OnClick(R.id.button_foto_agunan_2)
    public void onAmbilFotoAgunan2Clicked(View view) {
        mViewId = R.id.button_foto_agunan_2;
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS_FROM_PICTURE,
                getString(R.string.FormVisit_external_storage_permission_description), R.id.button_foto_agunan_2);
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.FormVisit_KonfirmasiSubmit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(HomeActivity.instantiateClearTask(FormVisitActivity.this));
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
