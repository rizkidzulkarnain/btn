package com.mitkoindo.smartcollection.backgroundservice;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.response.FormCallResponse;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressDb;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DetailDebiturDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormCallDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ericwijaya on 9/6/17.
 */

public class MyJobService extends JobService {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_ID = "user_id";

    private CompositeDisposable mComposites = new CompositeDisposable();
    private JobParameters mJobParameters;
    private String mAccessToken;
    private String mUserId;


    @Override
    public boolean onStartJob(JobParameters job) {
        Timber.i("onStartJob");

        mJobParameters = job;
        mAccessToken = job.getExtras().getString(ACCESS_TOKEN);
        mUserId = job.getExtras().getString(USER_ID);

        getBundle();
        sendFormCall();
        sendFormVisit();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Timber.i("onStopJob");
        return false;
    }

    private void getBundle() {

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getBundle(RestConstants.DATABASE_ID_VALUE, "20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OfflineBundleResponse>() {
                    @Override
                    public void onNext(OfflineBundleResponse offlineBundleResponse) {
                        Timber.i("getBundle success");

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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("getBundle Error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mComposites.add(disposable);
    }

    private FormCallBody createFormCallBody(FormCallBody.SpParameterFormCall spParameterFormCall) {
        FormCallBody formCallBody = new FormCallBody();
        formCallBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formCallBody.setSpName(RestConstants.FORM_CALL_SP_NAME);
        formCallBody.setSpParameterFormCall(spParameterFormCall);

        return formCallBody;
    }

    private void sendFormCall() {
        if (RealmHelper.getListFormCall().size() > 0) {
            Disposable disposable = Observable.fromIterable(RealmHelper.getListFormCall())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .flatMap(new Function<SpParameterFormCallDb, ObservableSource<List<FormCallResponse>>>() {
                        @Override
                        public ObservableSource<List<FormCallResponse>> apply(@NonNull SpParameterFormCallDb spParameterFormCallDb) throws Exception {
                            FormCallBody.SpParameterFormCall spParameterFormCall = spParameterFormCallDb.toSpParameterFormCall();

                            return ApiUtils.getRestServices(mAccessToken).saveFormCall(createFormCallBody(spParameterFormCall))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<List<FormCallResponse>>() {
                                        @Override
                                        public void accept(List<FormCallResponse> formCallResponses) throws Exception {
                                            Timber.i("sendFormCall doOnNext " + formCallResponses.get(0).getMessage());
                                            RealmHelper.deleteFormCall(spParameterFormCall.getAccountNo());
                                        }
                                    });
                        }
                    })
                    .subscribeWith(new DisposableObserver<List<FormCallResponse>>() {
                        @Override
                        public void onNext(@NonNull List<FormCallResponse> formCallResponses) {
                            Timber.i("sendFormCall onNext " + formCallResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("sendFormCall Error");
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("sendFormCall onComplete");
                        }
                    });

            mComposites.add(disposable);
        }
    }

    private FormVisitBody createFormVisitBody(FormVisitBody.SpParameter spParameter) {
        FormVisitBody formVisitBody = new FormVisitBody();
        formVisitBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitBody.setSpName(RestConstants.FORM_VISIT_SP_NAME);
        formVisitBody.setSpParameter(spParameter);

        return formVisitBody;
    }

    private void sendFormVisit() {
        if (RealmHelper.getListFormVisit().size() > 0) {
            Disposable disposable = Observable.fromIterable(RealmHelper.getListFormVisit())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .flatMap(new Function<SpParameterFormVisitDb, ObservableSource<List<FormVisitResponse>>>() {
                        @Override
                        public ObservableSource<List<FormVisitResponse>> apply(@NonNull SpParameterFormVisitDb spParameterFormVisitDb) throws Exception {
                            FormVisitBody.SpParameter spParameterFormVisit = spParameterFormVisitDb.toSpParameterFormVisit();

                            return ApiUtils.getRestServices(mAccessToken).saveFormVisit(createFormVisitBody(spParameterFormVisit))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<List<FormVisitResponse>>() {
                                        @Override
                                        public void accept(List<FormVisitResponse> formVisitResponses) throws Exception {
                                            Timber.i("sendFormVisit doOnNext " + formVisitResponses.get(0).getMessage());
                                            RealmHelper.deleteFormVisit(spParameterFormVisit.getAccNo());
                                        }
                                    });
                        }
                    })
                    .subscribeWith(new DisposableObserver<List<FormVisitResponse>>() {
                        @Override
                        public void onNext(@NonNull List<FormVisitResponse> formVisitResponses) {
                            Timber.i("sendFormVisit onNext " + formVisitResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("sendFormVisit Error");
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("sendFormVisit onComplete");
                        }
                    });

            mComposites.add(disposable);
        }
    }
}
