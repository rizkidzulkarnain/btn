package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityListDebiturBinding;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.event.EventRefreshListDebitur;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.module.laporan.LaporanCallActivity;
import com.mitkoindo.smartcollection.module.laporan.LaporanVisitActivity;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressDb;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DetailDebiturDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.OnClick;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class ListDebiturActivity extends BaseActivity {

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_TYPE_PENUGASAN_VALUE = "penugasan";
    public static final String EXTRA_TYPE_TAMBAH_KONTAK_VALUE = "tambah_kontak";
    public static final String EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE = "account_assignment";

    private ListDebiturViewModel mListDebiturViewModel;
    private ActivityListDebiturBinding mBinding;
    private ListDebiturViewPagerAdapter mViewPagerAdapter;
    private ViewPager.SimpleOnPageChangeListener mPageChangeListener;

    private String mType;


    public static Intent instantiate(Context context, String type) {
        Intent intent = new Intent(context, ListDebiturActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mType.equals(ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE)) {
            setupToolbar(getString(R.string.ListDebitur_PageTitle));
        } else {
            setupToolbar(getString(R.string.ListDebitur_PageTitleTambahKontak));
        }

        setupViewPager();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getExtra();

        mListDebiturViewModel = new ListDebiturViewModel(getAccessToken());
        mBinding = DataBindingUtil.bind(contentView);

        mListDebiturViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            }
        });
        mListDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMendapatkanData);
            }
        });
        mListDebiturViewModel.obsOfflineBundleResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsOfflineBundleResponse.get() != null) {
                    OfflineBundleResponse offlineBundleResponse = mListDebiturViewModel.obsOfflineBundleResponse.get();

                    List<DebiturItemDb> debiturItemDbList = new ArrayList<DebiturItemDb>();
                    for (DebiturItem debiturItem : offlineBundleResponse.getDebiturList()) {
                        debiturItemDbList.add(new DebiturItemDb(debiturItem));
                    }
                    RealmHelper.deleteListDebiturItem();
                    RealmHelper.storeListDebiturItem(debiturItemDbList);

                    List<DetailDebiturDb> detailDebiturDbList = new ArrayList<DetailDebiturDb>();
                    for (DetailDebitur detailDebitur : offlineBundleResponse.getDebiturDetailList()) {
                        detailDebiturDbList.add(new DetailDebiturDb(detailDebitur));
                    }
                    RealmHelper.deleteListDetailDebitur();
                    RealmHelper.storeListDetailDebitur(detailDebiturDbList);

                    List<PhoneNumberDb> phoneNumberDbList = new ArrayList<PhoneNumberDb>();
                    for (PhoneNumber phoneNumber : offlineBundleResponse.getDebiturPhoneList()) {
                        phoneNumberDbList.add(new PhoneNumberDb(phoneNumber));
                    }
                    RealmHelper.deleteListPhoneNumber();
                    RealmHelper.storeListPhoneNumber(phoneNumberDbList);

                    List<DropDownAddressDb> dropDownAddressDbList = new ArrayList<DropDownAddressDb>();
                    for (DropDownAddress downAddress : offlineBundleResponse.getDebiturAddressList()) {
                        dropDownAddressDbList.add(new DropDownAddressDb(downAddress));
                    }
                    RealmHelper.deleteListDropDownAddress();
                    RealmHelper.storeListAddress(dropDownAddressDbList);

                    displayMessage(R.string.ListDebitur_GetBundleSuccess);
                }
            }
        });
        mListDebiturViewModel.obsFormCallResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsFormCallResponse.get() != null) {
                    if (mListDebiturViewModel.obsFormCallResponse.get().size() > 0) {
                        Intent intent = new Intent(ListDebiturActivity.this, LaporanCallActivity.class);
                        intent.putExtra("CallID", mListDebiturViewModel.obsFormCallResponse.get().get(0).getMessage());

                        Random r = new Random();
                        int notificationId = r.nextInt((10000 - 10) + 1) + 10;
                        PendingIntent contentIntent = PendingIntent.getActivity(ListDebiturActivity.this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

//                                            Show Notification
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(ListDebiturActivity.this)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(getString(R.string.BackgroundService_DataOfflineFormCallDikirimkan)))
                                        .setContentTitle(getString(R.string.BackgroundService_NotificationTitle))
                                        .setContentText(getString(R.string.BackgroundService_DataOfflineFormCallDikirimkan))
                                        .setContentIntent(contentIntent)
                                        .setAutoCancel(true);

                        // Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(notificationId, mBuilder.build());
                    }
                }
            }
        });
        mListDebiturViewModel.obsFormVisitResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsFormVisitResponse.get() != null) {
                    if (mListDebiturViewModel.obsFormVisitResponse.get().size() > 0) {
                        Intent intent = new Intent(ListDebiturActivity.this, LaporanVisitActivity.class);
                        intent.putExtra("VisitID", mListDebiturViewModel.obsFormVisitResponse.get().get(0).getMessage());

                        Random r = new Random();
                        int notificationId = r.nextInt((10000 - 10) + 1) + 10;
                        PendingIntent contentIntent = PendingIntent.getActivity(ListDebiturActivity.this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

//                                            Show Notification
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(ListDebiturActivity.this)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(getString(R.string.BackgroundService_DataOfflineFormVisitDikirimkan)))
                                        .setContentTitle(getString(R.string.BackgroundService_NotificationTitle))
                                        .setContentText(getString(R.string.BackgroundService_DataOfflineFormVisitDikirimkan))
                                        .setContentIntent(contentIntent)
                                        .setAutoCancel(true);

                        // Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(notificationId, mBuilder.build());
                    }
                }
            }
        });
        mListDebiturViewModel.obsSuccessSendFormVisitAndCall.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsSuccessSendFormVisitAndCall.get()) {
                    displayMessage(R.string.ListDebitur_SendOfflineDataSuccess);

//                    Refresh List Debitur Pending
                    EventBus.getDefault().post(new EventRefreshListDebitur(RestConstants.LIST_DEBITUR_STATUS_PENDING_VALUE));
                }
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mType = getIntent().getExtras().getString(EXTRA_TYPE);
        }
    }

    private void setupViewPager() {
        mViewPagerAdapter = new ListDebiturViewPagerAdapter(getSupportFragmentManager(), mType);
        mBinding.viewPager.setOffscreenPageLimit(4);
        mBinding.viewPager.setAdapter(mViewPagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        if (mPageChangeListener != null) {
            mBinding.viewPager.removeOnPageChangeListener(mPageChangeListener);
        }
        mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        };
        mBinding.viewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @OnClick(R.id.image_btn_toolbar_download)
    public void onDownloadMenuClick(View view) {
        mListDebiturViewModel.getBundle();
    }
}
