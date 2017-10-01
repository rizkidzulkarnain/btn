package com.mitkoindo.smartcollection.module.debitur.tambahalamat;

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

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityTambahAlamatBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressType;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/4/17.
 */

public class TambahAlamatActivity extends BaseActivity {

    public static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_CUSTOMER_REFERENCE = "extra_customer_reference";

    private TambahAlamatViewModel mTambahAlamatViewModel;
    private ActivityTambahAlamatBinding mBinding;
    private Dialog mSpinnerDialog;

    private List<DropDownAddressType> mListDropDownAddressType;
    private List<DropDownRelationship> mListDropDownRelationship;
    private List<String> mListAddressType = new ArrayList<String>();
    private List<String> mListHubunganDenganDebitur = new ArrayList<String>();

    private String mNomorRekening;
    private String mCustomerReference;


    public static Intent instantiate(Context context, String noRekening, String customerReference) {
        Intent intent = new Intent(context, TambahAlamatActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_CUSTOMER_REFERENCE, customerReference);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.TambahAlamatSaja_PageTitle));
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
        return R.layout.activity_tambah_alamat;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mTambahAlamatViewModel = new TambahAlamatViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setTambahAlamatViewModel(mTambahAlamatViewModel);

        mTambahAlamatViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mTambahAlamatViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mTambahAlamatViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMenambahAlamat);
            }
        });
        mTambahAlamatViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mTambahAlamatViewModel.obsIsSaveSuccess.get()) {
                    displayMessage(R.string.TambahAlamatSaja_Success);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNomorRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
            mCustomerReference = getIntent().getExtras().getString(EXTRA_CUSTOMER_REFERENCE);
        }
    }

    private void initArrayDropDown() {
        mListDropDownRelationship = RealmHelper.getListDropDownRelationship();
        for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
            if (dropDownRelationship.getRelDesc() != null) {
                mListHubunganDenganDebitur.add(dropDownRelationship.getRelDesc());
            }
        }

        mListDropDownAddressType = RealmHelper.getListDropDownAddressType();
        for (DropDownAddressType dropDownAddressType : mListDropDownAddressType) {
            if (dropDownAddressType.getAtDesc() != null) {
                mListAddressType.add(dropDownAddressType.getAtDesc());
            }
        }
    }

    private void initNoSelectValue() {
        mTambahAlamatViewModel.typeAddress.set(getString(R.string.TambahAlamatSaja_AddressTypeInitial));
        mTambahAlamatViewModel.hubunganDenganDebitur.set(getString(R.string.TambahAlamatSaja_HubunganDenganDebiturInitial));
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
                case R.id.card_view_jenis_alamat: {
                    for (DropDownAddressType dropDownAddressType : mListDropDownAddressType) {
                        if (!TextUtils.isEmpty(dropDownAddressType.getAtDesc()) && dropDownAddressType.getAtDesc().equals(event.getName())) {
                            mTambahAlamatViewModel.typeAddressId.set(dropDownAddressType.getAtCode());
                            mTambahAlamatViewModel.typeAddress.set(dropDownAddressType.getAtDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hubungan_dengan_debitur: {
                    for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
                        if (!TextUtils.isEmpty(dropDownRelationship.getRelDesc()) && dropDownRelationship.getRelDesc().equals(event.getName())) {
                            mTambahAlamatViewModel.hubunganDenganDebiturId.set(dropDownRelationship.getRelId());
                            mTambahAlamatViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @OnClick(R.id.card_view_jenis_alamat)
    public void onTypeAddressClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListAddressType, getString(R.string.TambahAlamatSaja_AddressTypeInitial), R.id.card_view_jenis_alamat);
    }

    @OnClick(R.id.card_view_hubungan_dengan_debitur)
    public void onHubunganDenganDebiturClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHubunganDenganDebitur, getString(R.string.TambahAlamatSaja_HubunganDenganDebiturInitial), R.id.card_view_hubungan_dengan_debitur);
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        if (isValid()) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.FormCall_KonfirmasiSubmit))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTambahAlamatViewModel.tambahAlamat(getAccessToken(), getUserId(), mNomorRekening, mCustomerReference);
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
        if (TextUtils.isEmpty(mTambahAlamatViewModel.nama.get())
                || mTambahAlamatViewModel.nama.get().equals(getString(R.string.TambahAlamatSaja_NamaHint))) {
            displayMessage(getString(R.string.TambahAlamatSaja_NamaHint));
            return false;
        } else if (TextUtils.isEmpty(mTambahAlamatViewModel.hubunganDenganDebitur.get())
                || mTambahAlamatViewModel.hubunganDenganDebitur.get().equals(getString(R.string.TambahAlamatSaja_HubunganDenganDebiturInitial))) {
            displayMessage(getString(R.string.TambahAlamatSaja_HubunganDenganDebiturInitial));
            return false;
        } else if (TextUtils.isEmpty(mTambahAlamatViewModel.typeAddress.get())
                || mTambahAlamatViewModel.typeAddress.get().equals(getString(R.string.TambahAlamatSaja_AddressTypeInitial))) {
            displayMessage(getString(R.string.TambahAlamatSaja_AddressTypeInitial));
            return false;
        } else if (TextUtils.isEmpty(mTambahAlamatViewModel.alamat1.get())
                || mTambahAlamatViewModel.alamat1.get().equals(getString(R.string.TambahAlamatSaja_AlamatHint))) {
            displayMessage(getString(R.string.TambahAlamatSaja_AlamatHint));
            return false;
        } else if (TextUtils.isEmpty(mTambahAlamatViewModel.alamat2.get())
                || mTambahAlamatViewModel.alamat2.get().equals(getString(R.string.TambahAlamatSaja_KotaHint))) {
            displayMessage(getString(R.string.TambahAlamatSaja_KotaHint));
            return false;
        } else if (TextUtils.isEmpty(mTambahAlamatViewModel.alamat3.get())
                || mTambahAlamatViewModel.alamat3.get().equals(getString(R.string.TambahAlamatSaja_PropinsiHint))) {
            displayMessage(getString(R.string.TambahAlamatSaja_PropinsiHint));
            return false;
        }
        return true;
    }
}
