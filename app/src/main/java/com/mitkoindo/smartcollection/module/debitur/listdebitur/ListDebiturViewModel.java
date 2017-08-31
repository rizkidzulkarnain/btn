package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.ListDebiturBody;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class ListDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<DebiturItem>> obsDebiturResponse = new ObservableField<>();
    public ObservableField<List<DebiturItem>> obsDebiturResponseLoadMore = new ObservableField<>();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);

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
        /*spParameter.setOrderBy(RestConstants.LIST_DEBITUR_ORDER_BY_VALUE);*/
        spParameter.setPage(page);
        spParameter.setLimit(15);
        spParameter.setOrderDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);
        spParameter.setStatus(status);

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
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Log.e("ListDebiturViewModel", e.getMessage());
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
