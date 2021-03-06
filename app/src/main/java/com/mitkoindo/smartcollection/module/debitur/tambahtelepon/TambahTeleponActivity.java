package com.mitkoindo.smartcollection.module.debitur.tambahtelepon;

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
import com.mitkoindo.smartcollection.databinding.ActivityTambahTeleponBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownTeleponType;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/3/17.
 */

public class TambahTeleponActivity extends BaseActivity {

    public static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_CUSTOMER_REFERENCE = "extra_customer_reference";

    private TambahTeleponViewModel mTambahTeleponViewModel;
    private ActivityTambahTeleponBinding mBinding;
    private Dialog mSpinnerDialog;

    private List<DropDownTeleponType> mListDropDownTeleponType;
    private List<DropDownRelationship> mListDropDownRelationship;

    private List<String> mListTeleponType = new ArrayList<String>();
    private List<String> mListHubunganDenganDebitur = new ArrayList<String>();

    private String mNomorRekening;
    private String mCustomerReference;


    public static Intent instantiate(Context context, String noRekening, String customerReference) {
        Intent intent = new Intent(context, TambahTeleponActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_CUSTOMER_REFERENCE, customerReference);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.TambahTelepon_PageTitle));
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
        return R.layout.activity_tambah_telepon;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mTambahTeleponViewModel = new TambahTeleponViewModel();
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setTambahTeleponViewModel(mTambahTeleponViewModel);

        mTambahTeleponViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mTambahTeleponViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mTambahTeleponViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMenambahNomorTelepon);
            }
        });
        mTambahTeleponViewModel.obsIsSaveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mTambahTeleponViewModel.obsIsSaveSuccess.get()) {
                    displayMessage(R.string.TambahTelepon_Success);
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
        mListDropDownTeleponType = RealmHelper.getListDropDownTeleponType();
        for (DropDownTeleponType dropDownTeleponType : mListDropDownTeleponType) {
            if (dropDownTeleponType.getCtDesc() != null) {
                mListTeleponType.add(dropDownTeleponType.getCtDesc());
            }
        }

        mListDropDownRelationship = RealmHelper.getListDropDownRelationship();
        for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
            if (dropDownRelationship.getRelDesc() != null) {
                mListHubunganDenganDebitur.add(dropDownRelationship.getRelDesc());
            }
        }
    }

    private void initNoSelectValue() {
        mTambahTeleponViewModel.typeTelepon.set(getString(R.string.TambahTelepon_TeleponTypeInitial));
        mTambahTeleponViewModel.hubunganDenganDebitur.set(getString(R.string.TambahTelepon_HubunganDenganDebiturInitial));
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
                case R.id.card_view_type_telepon: {
                    for (DropDownTeleponType dropDownTeleponType : mListDropDownTeleponType) {
                        if (!TextUtils.isEmpty(dropDownTeleponType.getCtDesc()) && dropDownTeleponType.getCtDesc().equals(event.getName())) {
                            mTambahTeleponViewModel.typeTeleponId.set(dropDownTeleponType.getCtCode());
                            mTambahTeleponViewModel.typeTelepon.set(dropDownTeleponType.getCtDesc());
                            break;
                        }
                    }
                    break;
                }
                case R.id.card_view_hubungan_dengan_debitur: {
                    for (DropDownRelationship dropDownRelationship : mListDropDownRelationship) {
                        if (!TextUtils.isEmpty(dropDownRelationship.getRelDesc()) && dropDownRelationship.getRelDesc().equals(event.getName())) {
                            mTambahTeleponViewModel.hubunganDenganDebiturId.set(dropDownRelationship.getRelId());
                            mTambahTeleponViewModel.hubunganDenganDebitur.set(dropDownRelationship.getRelDesc());
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @OnClick(R.id.card_view_type_telepon)
    public void onTypeTeleponClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListTeleponType, getString(R.string.TambahTelepon_TeleponTypeInitial), R.id.card_view_type_telepon);
    }

    @OnClick(R.id.card_view_hubungan_dengan_debitur)
    public void onHubunganDenganDebiturClicked(View view) {
        showInstallmentDialogSimpleSpinner(mListHubunganDenganDebitur, getString(R.string.TambahTelepon_HubunganDenganDebiturInitial), R.id.card_view_hubungan_dengan_debitur);
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClicked(View view) {
        if (isValid()) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.FormCall_KonfirmasiSubmit))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTambahTeleponViewModel.tambahTelepon(getAccessToken(), getUserId(), mNomorRekening, mCustomerReference);
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
        if (TextUtils.isEmpty(mTambahTeleponViewModel.nama.get())
                || mTambahTeleponViewModel.nama.get().equals(getString(R.string.TambahTelepon_NamaHint))) {
            displayMessage(getString(R.string.TambahTelepon_NamaHint));
            return false;
        } else if (TextUtils.isEmpty(mTambahTeleponViewModel.hubunganDenganDebitur.get())
                || mTambahTeleponViewModel.hubunganDenganDebitur.get().equals(getString(R.string.TambahTelepon_HubunganDenganDebiturInitial))) {
            displayMessage(getString(R.string.TambahTelepon_HubunganDenganDebiturInitial));
            return false;
        } else if (TextUtils.isEmpty(mTambahTeleponViewModel.typeTelepon.get())
                || mTambahTeleponViewModel.typeTelepon.get().equals(getString(R.string.TambahTelepon_TeleponTypeInitial))) {
            displayMessage(getString(R.string.TambahTelepon_TeleponTypeInitial));
            return false;
        } else if (TextUtils.isEmpty(mTambahTeleponViewModel.telepon.get())
                || mTambahTeleponViewModel.hubunganDenganDebitur.get().equals(getString(R.string.TambahTelepon_NomorTeleponHint))) {
            displayMessage(getString(R.string.TambahTelepon_NomorTeleponHint));
            return false;
        }
        return true;
    }
}
