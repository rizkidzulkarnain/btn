package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.text.TextUtils;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.body.ListDebiturBody;
import com.mitkoindo.smartcollection.network.body.StaffProductivityDebiturBody;
import com.mitkoindo.smartcollection.network.response.FormCallResponse;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.network.response.OfflineBundleResponse;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormCallDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;
import com.mitkoindo.smartcollection.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public ObservableField<List<FormCallResponse>> obsFormCallResponse = new ObservableField<>();
    public ObservableField<List<FormVisitResponse>> obsFormVisitResponse = new ObservableField<>();
    public ObservableBoolean obsSuccessSendFormVisitAndCall = new ObservableBoolean(false);

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
                        sendFormCall();
                        Timber.i("getBundle success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        sendFormCall();
                        Timber.e("getBundle " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private FormCallBody createFormCallBody(FormCallBody.SpParameterFormCall spParameterFormCall) {
        FormCallBody formCallBody = new FormCallBody();
        formCallBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formCallBody.setSpName(RestConstants.FORM_CALL_SP_NAME);
        formCallBody.setSpParameterFormCall(spParameterFormCall);

        return formCallBody;
    }

    public void sendFormCall() {
        if (RealmHelper.getListFormCall().size() > 0) {
            Disposable disposable = Observable.fromIterable(RealmHelper.getListFormCall())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .flatMap(new Function<SpParameterFormCallDb, ObservableSource<List<FormCallResponse>>>() {
                        @Override
                        public ObservableSource<List<FormCallResponse>> apply(@io.reactivex.annotations.NonNull SpParameterFormCallDb spParameterFormCallDb) throws Exception {
                            FormCallBody.SpParameterFormCall spParameterFormCall = spParameterFormCallDb.toSpParameterFormCall();

                            return ApiUtils.getRestServices(mAccessToken).saveFormCall(createFormCallBody(spParameterFormCall))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<List<FormCallResponse>>() {
                                        @Override
                                        public void accept(List<FormCallResponse> formCallResponses) throws Exception {
                                            Timber.i("___sendFormCall doOnNext " + formCallResponses.get(0).getMessage());

//                                            Delete Form Call from Db
                                            RealmHelper.deleteFormCall(spParameterFormCall.getAccountNo());
                                        }
                                    });
                        }
                    })
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
                    .subscribeWith(new DisposableObserver<List<FormCallResponse>>() {
                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull List<FormCallResponse> formCallResponses) {
                            obsFormCallResponse.set(formCallResponses);
                            Timber.i("___sendFormCall onNext " + formCallResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Timber.e("___sendFormCall Error");
                            sendFormVisit();
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("___sendFormCall onComplete");
                            sendFormVisit();
                        }
                    });

            composites.add(disposable);
        } else {
            sendFormVisit();
        }
    }

    private FormVisitBody createFormVisitBody(SpParameterFormVisitDb spParameterFormVisitDb) {
        FormVisitBody formVisitBody = new FormVisitBody();
        formVisitBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitBody.setSpName(RestConstants.FORM_VISIT_SP_NAME);
        FormVisitBody.SpParameter spParameter = spParameterFormVisitDb.toSpParameterFormVisit();
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
                        public ObservableSource<List<FormVisitResponse>> apply(@io.reactivex.annotations.NonNull SpParameterFormVisitDb spParameterFormVisitDb) throws Exception {

                            File fileFotoDebitur = new File(spParameterFormVisitDb.getPhotoDebiturPath());
                            Uri uriFotoDebitur = Uri.fromFile(fileFotoDebitur);
                            RequestBody requestFileFotoDebitur = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoDebitur)), fileFotoDebitur);
                            MultipartBody.Part bodyDebitur = MultipartBody.Part.createFormData("file", fileFotoDebitur.getName(), requestFileFotoDebitur);

                            return ApiUtils.getMultipartServices(mAccessToken).uploadFile(bodyDebitur)
                                    .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                                        @Override
                                        public ObservableSource<MultipartResponse> apply(@io.reactivex.annotations.NonNull MultipartResponse multipartResponse) throws Exception {
                                            spParameterFormVisitDb.setPhotoDebitur(multipartResponse.getRelativePath());

                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan1Path())) {
                                                File fileFotoAgunan1 = new File(spParameterFormVisitDb.getPhotoAgunan1Path());
                                                Uri uriFotoAgunan1 = Uri.fromFile(fileFotoAgunan1);
                                                RequestBody requestFileFotoAgunan1 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan1)), fileFotoAgunan1);
                                                MultipartBody.Part bodyAgunan1 = MultipartBody.Part.createFormData("file", fileFotoAgunan1.getName(), requestFileFotoAgunan1);

                                                return ApiUtils.getMultipartServices(mAccessToken).uploadFile(bodyAgunan1);
                                            } else {
                                                return Observable.just(multipartResponse);
                                            }
                                        }
                                    })
                                    .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                                        @Override
                                        public ObservableSource<MultipartResponse> apply(@io.reactivex.annotations.NonNull MultipartResponse multipartResponse) throws Exception {
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan1Path())) {
                                                spParameterFormVisitDb.setPhotoAgunan1(multipartResponse.getRelativePath());
                                            } else {
                                                spParameterFormVisitDb.setPhotoAgunan1("");
                                            }

                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan2Path())) {
                                                File fileFotoAgunan2 = new File(spParameterFormVisitDb.getPhotoAgunan2Path());
                                                Uri uriFotoAgunan2 = Uri.fromFile(fileFotoAgunan2);
                                                RequestBody requestFileFotoAgunan2 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan2)), fileFotoAgunan2);
                                                MultipartBody.Part bodyAgunan2 = MultipartBody.Part.createFormData("file", fileFotoAgunan2.getName(), requestFileFotoAgunan2);

                                                return ApiUtils.getMultipartServices(mAccessToken).uploadFile(bodyAgunan2);
                                            } else {
                                                return Observable.just(multipartResponse);
                                            }
                                        }
                                    })
                                    .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                                        @Override
                                        public ObservableSource<MultipartResponse> apply(@io.reactivex.annotations.NonNull MultipartResponse multipartResponse) throws Exception {
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan2Path())) {
                                                spParameterFormVisitDb.setPhotoAgunan2(multipartResponse.getRelativePath());
                                            }

                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoSignaturePath())) {
                                                File fileSignature = new File(spParameterFormVisitDb.getPhotoSignaturePath());
                                                Uri uriSignature = Uri.fromFile(fileSignature);
                                                RequestBody requestFileSignature = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriSignature)), fileSignature);
                                                MultipartBody.Part bodySignature = MultipartBody.Part.createFormData("file", fileSignature.getName(), requestFileSignature);

                                                return ApiUtils.getMultipartServices(mAccessToken).uploadFile(bodySignature);
                                            } else {
                                                return Observable.just(multipartResponse);
                                            }
                                        }
                                    })
                                    .flatMap(new Function<MultipartResponse, ObservableSource<List<FormVisitResponse>>>() {
                                        @Override
                                        public ObservableSource<List<FormVisitResponse>> apply(@io.reactivex.annotations.NonNull MultipartResponse multipartResponse) throws Exception {
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoSignaturePath())) {
                                                spParameterFormVisitDb.setPhotoSignature(multipartResponse.getRelativePath());
                                            } else {
                                                spParameterFormVisitDb.setPhotoSignature("");
                                            }

                                            return ApiUtils.getRestServices(mAccessToken).saveFormVisit(createFormVisitBody(spParameterFormVisitDb));
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<List<FormVisitResponse>>() {
                                        @Override
                                        public void accept(List<FormVisitResponse> formVisitResponses) throws Exception {
                                            Timber.i("sendFormVisit doOnNext " + formVisitResponses.get(0).getMessage());

//                                            Delete file photo
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoDebiturPath())) {
                                                FileUtils.deleteFile(spParameterFormVisitDb.getPhotoDebiturPath());
                                            }
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan1Path())) {
                                                FileUtils.deleteFile(spParameterFormVisitDb.getPhotoAgunan1Path());
                                            }
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoAgunan2Path())) {
                                                FileUtils.deleteFile(spParameterFormVisitDb.getPhotoAgunan2Path());
                                            }
                                            if (!TextUtils.isEmpty(spParameterFormVisitDb.getPhotoSignaturePath())) {
                                                FileUtils.deleteFile(spParameterFormVisitDb.getPhotoSignaturePath());
                                            }

//                                            Delete Form Visit from Db
                                            RealmHelper.deleteFormVisit(spParameterFormVisitDb.getAccNo());
                                        }
                                    });
                        }
                    })
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
                    .subscribeWith(new DisposableObserver<List<FormVisitResponse>>() {
                        @Override
                        public void onNext(List<FormVisitResponse> formVisitResponses) {
                            obsFormVisitResponse.set(formVisitResponses);
                            Timber.i("___sendFormVisit onNext " + formVisitResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Timber.e("___sendFormVisit Error");
                        }

                        @Override
                        public void onComplete() {
                            obsSuccessSendFormVisitAndCall.set(true);
                            Timber.i("___sendFormVisit onComplete");
                        }
                    });

            composites.add(disposable);
        }
    }

    public List<DebiturItemDb> getListDebiturFromDb() {
        return RealmHelper.getListDebiturItem();
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
