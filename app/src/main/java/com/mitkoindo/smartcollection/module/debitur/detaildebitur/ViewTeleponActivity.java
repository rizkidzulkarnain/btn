package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityViewTeleponBinding;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.module.debitur.tambahtelepon.TambahTeleponActivity;
import com.mitkoindo.smartcollection.module.formcall.FormCallActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 9/9/17.
 */

public class ViewTeleponActivity extends BaseActivity {

    public static final String EXTRA_NO_REKENING = "extra_no_rekening";
    private static final String EXTRA_CUSTOMER_REFERENCE = "extra_customer_reference";

    private DetailDebiturViewModel mDetailDebiturViewModel;
    private ActivityViewTeleponBinding mBinding;
    private FastItemAdapter mFastAdapter;

    private String mNoRekening = "";
    private String mCustomerReference = "";


    public static Intent instantiate(Context context, String noRekening, String customerReference) {
        Intent intent = new Intent(context, ViewTeleponActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        intent.putExtra(EXTRA_CUSTOMER_REFERENCE, customerReference);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.ViewTelepon_PageTitle));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view_telepon;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

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
                    mBinding.swipeRefreshLayoutPhone.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mDetailDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.mErrorType == DetailDebiturViewModel.GET_PHONE_LIST_ERROR) {

                    List<PhoneNumber> phoneNumberList = new ArrayList<PhoneNumber>();
                    List<PhoneNumberDb> phoneNumberDbList = RealmHelper.getPhoneNumber(mNoRekening);
                    for (PhoneNumberDb phoneNumberDb : phoneNumberDbList) {
                        phoneNumberList.add(phoneNumberDb.toPhoneNumber());
                    }

                    mDetailDebiturViewModel.obsListPhoneNumber.set(phoneNumberList);
                }
            }
        });
        mDetailDebiturViewModel.obsListPhoneNumber.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mDetailDebiturViewModel.obsListPhoneNumber.get() != null) {
                    mFastAdapter.clear();
                    mFastAdapter.add(mDetailDebiturViewModel.obsListPhoneNumber.get());
                    if (mDetailDebiturViewModel.obsListPhoneNumber.get().size() > 0) {
                        mDetailDebiturViewModel.obsIsPhoneEmpty.set(false);
                    } else {
                        mDetailDebiturViewModel.obsIsPhoneEmpty.set(true);
                    }
                }
            }
        });

        setupRecyclerView();
        mDetailDebiturViewModel.getPhoneList(mNoRekening);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
            mCustomerReference = getIntent().getExtras().getString(EXTRA_CUSTOMER_REFERENCE);
        }
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewPhone.addItemDecoration(itemDecoration);
        mBinding.recyclerViewPhone.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewPhone.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<PhoneNumber>() {
            @Override
            public boolean onClick(View v, IAdapter<PhoneNumber> adapter, PhoneNumber item, int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + item.getNomorKontak()));
                startActivity(intent);

                return true;
            }
        });

        mBinding.swipeRefreshLayoutPhone.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutPhone.setRefreshing(true);
                mDetailDebiturViewModel.getPhoneList(mNoRekening);
            }
        });
    }

    @OnClick(R.id.image_btn_toolbar_tambah_telepon)
    public void onTambahTeleponClick(View view) {
        startActivity(TambahTeleponActivity.instantiate(ViewTeleponActivity.this, mNoRekening, mCustomerReference));
    }
}
