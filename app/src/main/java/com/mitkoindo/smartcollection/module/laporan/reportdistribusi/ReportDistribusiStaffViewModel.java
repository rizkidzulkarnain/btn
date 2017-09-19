package com.mitkoindo.smartcollection.module.laporan.reportdistribusi;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.ReportDistribusiStaffBody;
import com.mitkoindo.smartcollection.network.body.ReportDistribusiSummaryBody;
import com.mitkoindo.smartcollection.objectdata.ReportDistribusiStaff;
import com.mitkoindo.smartcollection.objectdata.ReportDistribusiSummary;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ericwijaya on 9/15/17.
 */

public class ReportDistribusiStaffViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<ReportDistribusiSummary>> obsReportDistribusiSummaryResponse = new ObservableField<>();
    public ObservableField<List<ReportDistribusiStaff>> obsReportDistribusiStaffResponse = new ObservableField<>();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public ReportDistribusiStaffViewModel(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public void getListReportDistribusiStaff(String userId) {
        ReportDistribusiSummaryBody reportDistribusiSummaryBody = new ReportDistribusiSummaryBody();
        reportDistribusiSummaryBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        reportDistribusiSummaryBody.setSpName(RestConstants.REPORT_DISTRIBUSI_SUMMARY_SP_NAME);

        ReportDistribusiSummaryBody.SpParameter spParameterSummary = new ReportDistribusiSummaryBody.SpParameter();
        spParameterSummary.setUserId(userId);

        reportDistribusiSummaryBody.setSpParameter(spParameterSummary);

        ReportDistribusiStaffBody reportDistribusiStaffBody = new ReportDistribusiStaffBody();
        reportDistribusiStaffBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        reportDistribusiStaffBody.setSpName(RestConstants.REPORT_DISTRIBUSI_STAFF_SP_NAME);

        ReportDistribusiStaffBody.SpParameter spParameterStaff = new ReportDistribusiStaffBody.SpParameter();
        spParameterStaff.setUserId(userId);

        reportDistribusiStaffBody.setSpParameter(spParameterStaff);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getReportDistribusiSummary(reportDistribusiSummaryBody)
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
                .doOnNext(new Consumer<List<ReportDistribusiSummary>>() {
                    @Override
                    public void accept(List<ReportDistribusiSummary> reportDistribusiSummaries) throws Exception {
                        obsReportDistribusiSummaryResponse.set(reportDistribusiSummaries);
                        Timber.i("getReportDistribusiSummary success");
                    }
                })
                .flatMap(new Function<List<ReportDistribusiSummary>, ObservableSource<List<ReportDistribusiStaff>>>() {
                    @Override
                    public ObservableSource<List<ReportDistribusiStaff>> apply(@NonNull List<ReportDistribusiSummary> reportDistribusiSummaries) throws Exception {
                        return ApiUtils.getRestServices(mAccessToken).getReportDistribusiStaff(reportDistribusiStaffBody)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribeWith(new DisposableObserver<List<ReportDistribusiStaff>>() {
                    @Override
                    public void onNext(List<ReportDistribusiStaff> listReportDistribusiStaff) {
                        obsReportDistribusiStaffResponse.set(listReportDistribusiStaff);
                        Timber.i("getListReportDistribusiStaff success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListReportDistribusiStaff " + e.getMessage());
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
