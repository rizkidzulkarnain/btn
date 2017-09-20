package com.mitkoindo.smartcollection.module.laporan.agenttracking;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.StaffDownlineBody;
import com.mitkoindo.smartcollection.network.body.StaffItemBody;
import com.mitkoindo.smartcollection.objectdata.StaffDownline;
import com.mitkoindo.smartcollection.objectdata.StaffItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ericwijaya on 9/19/17.
 */

public class StaffDownlineViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<StaffDownline>> obsStaffDownlineResponse = new ObservableField<>();
    public ObservableField<List<StaffItem>> obsStaffItemResponse = new ObservableField<>();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);
    public ObservableField<String> obsSearch = new ObservableField<>();

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public StaffDownlineViewModel(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public void getStaffDownline(String userId) {
        StaffDownlineBody staffDownlineBody = new StaffDownlineBody();
        staffDownlineBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        staffDownlineBody.setSpName(RestConstants.STAFF_DOWNLINE_SP_NAME);

        StaffDownlineBody.SpParameter spParameter = new StaffDownlineBody.SpParameter();
        spParameter.setUserId(userId);

        staffDownlineBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListStaffDownline(staffDownlineBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        obsIsLoading.set(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<StaffDownline>>() {
                    @Override
                    public void onNext(List<StaffDownline> listStaffDownline) {
                        obsStaffDownlineResponse.set(listStaffDownline);
                        Timber.i("getStaffDownline success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getStaffDownline " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void getStaff(String userId) {
        StaffItemBody staffItemBody = new StaffItemBody();
        staffItemBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        staffItemBody.setSpName(RestConstants.STAFF_SP_NAME);

        StaffItemBody.SpParameter spParameter = new StaffItemBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setKeyword(obsSearch.get());

        staffItemBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListStaff(staffItemBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        obsIsLoading.set(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<StaffItem>>() {
                    @Override
                    public void onNext(List<StaffItem> listStaffItem) {
                        obsStaffItemResponse.set(listStaffItem);
                        Timber.i("getStaff success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getStaff " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
