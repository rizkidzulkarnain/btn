package com.mitkoindo.smartcollection.module.formcall;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityFormCallBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCallActivity extends BaseActivity {

    private static final String EXTRA_NO_REKENING = "extra_no_rekening";

    private FormCallViewModel mFormCallViewModel;
    private ActivityFormCallBinding mBinding;

    private Dialog mSpinnerDialog;

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


    public static Intent instantiate(Context context, String noRekening) {
        Intent intent = new Intent(context, FormCallActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
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
                displayMessage(R.string.GagalMenyimpanFormCall);
            }
        });
        mFormCallViewModel.jumlahYangAkanDisetor.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    Double jumlahYangAkanDisetor = Double.parseDouble(mFormCallViewModel.jumlahYangAkanDisetor.get());
                    mFormCallViewModel.spParameter.setPtpAmount(jumlahYangAkanDisetor);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                    mFormCallViewModel.spParameter.setPtpAmount(0);
                }
            }
        });
        mFormCallViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mFormCallViewModel.obsIsSaveSuccess.get()) {
                    displayErrorDialog("", getString(R.string.FormCall_SaveFormSuccess));
                    startActivity(HomeActivity.instantiateClearTask(FormCallActivity.this));
                }
            }
        });
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
                            mFormCallViewModel.spParameter.setTujuan(dropDownPurpose.getPId());
                            mFormCallViewModel.tujuan.set(dropDownPurpose.getPDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hubungan_dengan_debitur: {
                    for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
                        if (!TextUtils.isEmpty(dropDownRelationship.getRelDesc()) && dropDownRelationship.getRelDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameter.setRelationship(dropDownRelationship.getRelId());
                            mFormCallViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hasil_panggilan: {
                    for (DropDownResult dropDownResult : mListDropDownResult) {
                        if (!TextUtils.isEmpty(dropDownResult.getResultDesc()) && dropDownResult.getResultDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameter.setResult(dropDownResult.getResultId());
                            mFormCallViewModel.hasilPanggilan.set(dropDownResult.getResultDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_alasan_tidak_bayar: {
                    for (DropDownReason dropDownReason : mListDropDownReason) {
                        if (!TextUtils.isEmpty(dropDownReason.getReasonDesc()) && dropDownReason.getReasonDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameter.setReasonNoPayment(dropDownReason.getReasonId());
                            mFormCallViewModel.alasanTidakBayar.set(dropDownReason.getReasonDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_tindak_lanjut: {
                    for (DropDownAction dropDownAction : mListDropDownAction) {
                        if (!TextUtils.isEmpty(dropDownAction.getActionDesc()) && dropDownAction.getActionDesc().equals(event.getName())) {
                            mFormCallViewModel.spParameter.setNextAction(dropDownAction.getActionId());
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
            mFormCallViewModel.setTanggalTindakLanjut(c.getTime());
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

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
                            mFormCallViewModel.saveFormCall(getAccessToken());
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
        mFormCallViewModel.spParameter.setAccountNo(mNoRekening);
        mFormCallViewModel.spParameter.setUserId("btn0100011");
        FormCallBody.SpParameter spParameter = mFormCallViewModel.spParameter;
        if (TextUtils.isEmpty(spParameter.getTujuan())) {
            displayMessage(getString(R.string.FormCall_TujuanCallInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getSpokeTo())) {
            displayMessage(getString(R.string.FormCall_BerbicaraDenganHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getRelationship())) {
            displayMessage(getString(R.string.FormCall_HubunganDenganDebiturInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getResult())) {
            displayMessage(getString(R.string.FormCall_HasilPanggilanInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getResultDate())) {
            displayMessage(getString(R.string.FormCall_TanggalHasilPanggilanInitial));
            return false;
        } else if (spParameter.getPtpAmount() == 0) {
            displayMessage(getString(R.string.FormCall_JumlahYangAkanDisetorHint));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getReasonNoPayment())) {
            displayMessage(getString(R.string.FormCall_AlasanTidakBayarInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getNextAction())) {
            displayMessage(getString(R.string.FormCall_TindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getDateAction())) {
            displayMessage(getString(R.string.FormCall_TanggalTindakLanjutInitial));
            return false;
        } else if (TextUtils.isEmpty(spParameter.getNotes())) {
            displayMessage(getString(R.string.FormCall_CatatanHint));
            return false;
        }
        return true;
    }

}
