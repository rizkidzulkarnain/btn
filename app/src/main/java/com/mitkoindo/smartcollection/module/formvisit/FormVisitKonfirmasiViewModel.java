package com.mitkoindo.smartcollection.module.formvisit;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;
import com.mitkoindo.smartcollection.utils.FileUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * Created by ericwijaya on 8/27/17.
 */

public class FormVisitKonfirmasiViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<Throwable> errorSave = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowTanggalJanjiDebitur = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowJumlahYangAkanDisetor = new ObservableBoolean(false);
    public ObservableBoolean obsIsSignatureShow = new ObservableBoolean(false);

    public ObservableField<String> tujuanKunjungan = new ObservableField<>();
    public ObservableField<String> alamatYangDikunjungi = new ObservableField<>();
    public ObservableField<String> namaOrangYangDikunjungi = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebitur = new ObservableField<>();
    public ObservableField<String> hasilKunjungan = new ObservableField<>();
    public ObservableField<String> tanggalJanjiDebitur = new ObservableField<>();
    public ObservableField<String> jumlahYangAkanDisetor = new ObservableField<>("0");
    public ObservableField<String> statusAgunan = new ObservableField<>();
    public ObservableField<String> kondisiAgunan = new ObservableField<>();
    public ObservableField<String> alasanMenunggak = new ObservableField<>();
    public ObservableField<String> tindakLanjut = new ObservableField<>();
    public ObservableField<String> tanggalTindakLanjut = new ObservableField<>();
    public ObservableField<String> catatan = new ObservableField<>();
    public ObservableField<Boolean> isFotoAgunan1Show = new ObservableField<>();
    public ObservableField<Boolean> isFotoAgunan2Show = new ObservableField<>();

    public SpParameterFormVisitDb spParameterFormVisitDb = new SpParameterFormVisitDb();

    public FormVisitKonfirmasiViewModel() {
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void saveFormVisit(String accessToken) {
        File fileFotoDebitur = new File(spParameterFormVisitDb.getPhotoDebiturPath());
        Uri uriFotoDebitur = Uri.fromFile(fileFotoDebitur);
        RequestBody requestFileFotoDebitur = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoDebitur)), fileFotoDebitur);
        MultipartBody.Part bodyDebitur = MultipartBody.Part.createFormData("file", fileFotoDebitur.getName(), requestFileFotoDebitur);

        Disposable disposable = ApiUtils.getMultipartServices(accessToken).uploadFile(bodyDebitur)
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        spParameterFormVisitDb.setPhotoDebitur(multipartResponse.getRelativePath());

                        if (isFotoAgunan1Show.get()) {
                            File fileFotoAgunan1 = new File(spParameterFormVisitDb.getPhotoAgunan1Path());
                            Uri uriFotoAgunan1 = Uri.fromFile(fileFotoAgunan1);
                            RequestBody requestFileFotoAgunan1 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan1)), fileFotoAgunan1);
                            MultipartBody.Part bodyAgunan1 = MultipartBody.Part.createFormData("file", fileFotoAgunan1.getName(), requestFileFotoAgunan1);

                            return ApiUtils.getMultipartServices(accessToken).uploadFile(bodyAgunan1);
                        } else {
                            return Observable.just(multipartResponse);
                        }
                    }
                })
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        if (isFotoAgunan1Show.get()) {
                            spParameterFormVisitDb.setPhotoAgunan1(multipartResponse.getRelativePath());
                        } else {
                            spParameterFormVisitDb.setPhotoAgunan1("");
                        }

                        if (isFotoAgunan2Show.get()) {
                            File fileFotoAgunan2 = new File(spParameterFormVisitDb.getPhotoAgunan2Path());
                            Uri uriFotoAgunan2 = Uri.fromFile(fileFotoAgunan2);
                            RequestBody requestFileFotoAgunan2 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan2)), fileFotoAgunan2);
                            MultipartBody.Part bodyAgunan2 = MultipartBody.Part.createFormData("file", fileFotoAgunan2.getName(), requestFileFotoAgunan2);

                            return ApiUtils.getMultipartServices(accessToken).uploadFile(bodyAgunan2);
                        } else {
                            return Observable.just(multipartResponse);
                        }
                    }
                })
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        if (isFotoAgunan2Show.get()) {
                            spParameterFormVisitDb.setPhotoAgunan2(multipartResponse.getRelativePath());
                        }

                        if (obsIsSignatureShow.get()) {
                            File fileSignature = new File(spParameterFormVisitDb.getPhotoSignaturePath());
                            Uri uriSignature = Uri.fromFile(fileSignature);
                            RequestBody requestFileSignature = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriSignature)), fileSignature);
                            MultipartBody.Part bodySignature = MultipartBody.Part.createFormData("file", fileSignature.getName(), requestFileSignature);

                            return ApiUtils.getMultipartServices(accessToken).uploadFile(bodySignature);
                        } else {
                            return Observable.just(multipartResponse);
                        }
                    }
                })
                .flatMap(new Function<MultipartResponse, ObservableSource<List<FormVisitResponse>>>() {
                    @Override
                    public ObservableSource<List<FormVisitResponse>> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        if (obsIsSignatureShow.get()) {
                            spParameterFormVisitDb.setPhotoSignature(multipartResponse.getRelativePath());
                        } else {
                            spParameterFormVisitDb.setPhotoSignature("");
                        }

                        return ApiUtils.getRestServices(accessToken).saveFormVisit(createFormVisitBody());
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
                    public void onNext(List<FormVisitResponse> v) {
                        obsIsSaveSuccess.set(true);
                        Timber.i("Save Form Visit Success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        RealmHelper.storeFormVisit(spParameterFormVisitDb);
                        Timber.e("Save Form Visit Error : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void saveFormVisitNoFile(String accessToken) {
        Disposable disposable = ApiUtils.getRestServices(accessToken).saveFormVisit(createFormVisitBody())
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
                    public void onNext(List<FormVisitResponse> v) {
                        obsIsSaveSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        RealmHelper.storeFormVisit(spParameterFormVisitDb);
                        Timber.e("Save Form Visit with photo failed" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private FormVisitBody createFormVisitBody() {
        FormVisitBody formVisitBody = new FormVisitBody();
        formVisitBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitBody.setSpName(RestConstants.FORM_VISIT_SP_NAME);
        FormVisitBody.SpParameter spParameter = spParameterFormVisitDb.toSpParameterFormVisit();
        formVisitBody.setSpParameter(spParameter);

        return formVisitBody;
    }
}
