package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.ListDebiturBody;
import com.mitkoindo.smartcollection.network.body.StaffProductivityDebiturBody;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;

import java.util.ArrayList;
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
 * Created by ericwijaya on 8/17/17.
 */

public class ListDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<DebiturItem>> obsDebiturResponse = new ObservableField<>();
    public ObservableField<List<DebiturItem>> obsDebiturResponseLoadMore = new ObservableField<>();
    public ObservableField<OfflineBundleResponse> obsOfflineBundleResponse = new ObservableField<>();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);
    public ObservableField<String> obsSearch = new ObservableField<>("");
    public ObservableField<String> obsSort = new ObservableField<>();

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public ListDebiturViewModel(String accessToken) {
        mAccessToken = accessToken;
    }

    public void getListDebitur(String userId, String status, int page) {
        ListDebiturBody listDebiturBody = new ListDebiturBody();
        listDebiturBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        listDebiturBody.setSpName(RestConstants.LIST_DEBITUR_SP_NAME);

        ListDebiturBody.SpParameter spParameter = new ListDebiturBody.SpParameter();
        spParameter.setUserId(userId);
        if (obsSort.get().equals("Fullname")) {
            spParameter.setOrderBy(RestConstants.LIST_DEBITUR_ORDER_BY_FULL_NAME);
        } else if (obsSort.get().equals("Total Kewajiban")) {
            spParameter.setOrderBy(RestConstants.LIST_DEBITUR_ORDER_BY_TOTAL_KEWAJIBAN);
        } else {
            spParameter.setOrderBy(RestConstants.LIST_DEBITUR_ORDER_BY_ASSIGN_DATE);
        }
        spParameter.setPage(page);
        spParameter.setLimit(15);
        spParameter.setOrderDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);
        spParameter.setStatus(status);
        spParameter.setKeyword(obsSearch.get());

        listDebiturBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListDebitur(listDebiturBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (page == 1) {
                            obsIsLoading.set(true);
                        }
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<DebiturItem>>() {
                    @Override
                    public void onNext(List<DebiturItem> listDebitur) {
                        ArrayList<DebiturItem> temp = new ArrayList<DebiturItem>();
                        for (DebiturItem debiturItem : listDebitur) {
                            DebiturItem item = debiturItem;
                            item.setPenugasan(true);
                            temp.add(item);
                        }
                        if (page == 1) {
                            obsDebiturResponse.set(temp);
                        } else {
                            obsDebiturResponseLoadMore.set(temp);
                        }
                        Timber.i("getListDebitur success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListDebitur " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void getListDebiturReportDistribusi(String userId, int page) {
        ListDebiturBody listDebiturBody = new ListDebiturBody();
        listDebiturBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        listDebiturBody.setSpName(RestConstants.LIST_DEBITUR_SP_NAME);

        ListDebiturBody.SpParameter spParameter = new ListDebiturBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setPage(page);
        spParameter.setLimit(15);
        spParameter.setOrderDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);
        spParameter.setKeyword(obsSearch.get());

        listDebiturBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListDebitur(listDebiturBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (page == 1) {
                            obsIsLoading.set(true);
                        }
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        obsIsLoading.set(false);
                    }
                })
                .subscribeWith(new DisposableObserver<List<DebiturItem>>() {
                    @Override
                    public void onNext(List<DebiturItem> listDebitur) {
                        if (page == 1) {
                            obsDebiturResponse.set(listDebitur);
                        } else {
                            obsDebiturResponseLoadMore.set(listDebitur);
                        }
                        Timber.i("getListDebitur success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListDebitur " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }
    public void getListDebiturStaffProductivity(String userId, String date, String timeRange) {
        StaffProductivityDebiturBody staffProductivityDebiturBody = new StaffProductivityDebiturBody();
        staffProductivityDebiturBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        staffProductivityDebiturBody.setSpName(RestConstants.STAFF_PRODUCTIVITY_DEBITUR_SP_NAME);

        StaffProductivityDebiturBody.SpParameter spParameter = new StaffProductivityDebiturBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setActionDate(date);
        spParameter.setTimeRange(timeRange);

        staffProductivityDebiturBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListDebiturStaffProductivity(staffProductivityDebiturBody)
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
                .subscribeWith(new DisposableObserver<List<DebiturItem>>() {
                    @Override
                    public void onNext(List<DebiturItem> listDebitur) {
                        obsDebiturResponse.set(listDebitur);
                        Timber.i("getListDebitur success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListDebitur " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void getBundle() {

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getBundle(RestConstants.DATABASE_ID_VALUE, "20")
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
                .subscribeWith(new DisposableObserver<OfflineBundleResponse>() {
                    @Override
                    public void onNext(OfflineBundleResponse offlineBundleResponse) {
                        obsOfflineBundleResponse.set(offlineBundleResponse);
                        Timber.i("getBundle success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getBundle " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public List<DebiturItemDb> getListDebiturFromDb() {
        return RealmHelper.getListDebiturItem();
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
