package com.mitkoindo.smartcollection.module.laporan.staffproductivity;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.StaffProductivityBody;
import com.mitkoindo.smartcollection.objectdata.StaffProductivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by ericwijaya on 9/17/17.
 */

public class StaffProductivityViewModel extends BaseObservable implements ILifecycleViewModel {

    public static final int NOT_FOUND_ERROR_TYPE = 404;
    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<StaffProductivity>> obsListStaffProductivity = new ObservableField<>();
    public ObservableField<StaffProductivity> obsStaffProductivity = new ObservableField<>();
    public ObservableField<String> obsTanggal = new ObservableField<>();
    public ObservableField<String> obsTanggalLayout = new ObservableField<>();
    public int errorType = 0;

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public StaffProductivityViewModel(String accessToken) {
        mAccessToken = accessToken;
    }

    public void getStaffProductivity(String userId, String date) {
        StaffProductivityBody staffProductivityBody = new StaffProductivityBody();
        staffProductivityBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        staffProductivityBody.setSpName(RestConstants.STAFF_PRODUCTIVITY_SP_NAME);

        StaffProductivityBody.SpParameter spParameter = new StaffProductivityBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setDate(date);

        staffProductivityBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getStaffProductivity(staffProductivityBody)
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
                .subscribeWith(new DisposableObserver<List<StaffProductivity>>() {
                    @Override
                    public void onNext(List<StaffProductivity> listStaffProductivity) {
                        obsListStaffProductivity.set(listStaffProductivity);
                        if (listStaffProductivity.size() > 0) {
                            obsStaffProductivity.set(listStaffProductivity.get(0));
                        }
                        Timber.i("getStaffProductivity Success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 404) {
                                errorType = NOT_FOUND_ERROR_TYPE;
                            }
                        }
                        error.set(e);
                        Timber.e("getStaffProductivity " + e.getMessage());
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
