package com.mitkoindo.smartcollection.module.formvisit;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.util.Log;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.utils.FileUtils;

import java.io.File;

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
import retrofit2.HttpException;

/**
 * Created by ericwijaya on 8/27/17.
 */

public class FormVisitKonfirmasiViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<Throwable> errorSave = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);

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
    public ObservableField<Boolean> isFotoAgunan2Show = new ObservableField<>();

    public FormVisitBody.SpParameter spParameter = new FormVisitBody.SpParameter();

    public FormVisitKonfirmasiViewModel() {
    }

    @Override
    public void onDestroy() {
        composites.clear();
    }

    public void saveFormVisit(String accessToken) {
        File fileFotoDebitur = new File(spParameter.getPhotoDebiturPath());
        Uri uriFotoDebitur = Uri.fromFile(fileFotoDebitur);
        RequestBody requestFileFotoDebitur = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoDebitur)), fileFotoDebitur);
        MultipartBody.Part bodyDebitur = MultipartBody.Part.createFormData("file", fileFotoDebitur.getName(), requestFileFotoDebitur);

        File fileFotoAgunan1 = new File(spParameter.getPhotoAgunan1Path());
        Uri uriFotoAgunan1 = Uri.fromFile(fileFotoAgunan1);
        RequestBody requestFileFotoAgunan1 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan1)), fileFotoAgunan1);
        MultipartBody.Part bodyAgunan1 = MultipartBody.Part.createFormData("file", fileFotoAgunan1.getName(), requestFileFotoAgunan1);

        MultipartBody.Part bodyAgunan2;
        if (isFotoAgunan2Show.get()) {
            File fileFotoAgunan2 = new File(spParameter.getPhotoAgunan2Path());
            Uri uriFotoAgunan2 = Uri.fromFile(fileFotoAgunan2);
            RequestBody requestFileFotoAgunan2 = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoAgunan2)), fileFotoAgunan2);
            bodyAgunan2 = MultipartBody.Part.createFormData("file", fileFotoAgunan2.getName(), requestFileFotoAgunan2);
        }

        File fileSignature = new File(spParameter.getSignaturePath());
        Uri uriSignature = Uri.fromFile(fileSignature);
        RequestBody requestFileSignature = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriSignature)), fileSignature);
        MultipartBody.Part bodySignature = MultipartBody.Part.createFormData("file", fileSignature.getName(), requestFileSignature);

        Disposable disposable = ApiUtils.getMultipartServices(accessToken).uploadFile(bodyDebitur)
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        spParameter.setPhotoDebitur(multipartResponse.getRelativePath());

                        return ApiUtils.getMultipartServices(accessToken).uploadFile(bodyAgunan1);
                    }
                })
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        spParameter.setPhotoAgunan1(multipartResponse.getRelativePath());

                        return ApiUtils.getMultipartServices(accessToken).uploadFile(bodySignature);
                    }
                })
                .flatMap(new Function<MultipartResponse, ObservableSource<Void>>() {
                    @Override
                    public ObservableSource<Void> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
                        spParameter.setSignature(multipartResponse.getRelativePath());

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
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void v) {
                        obsIsSaveSuccess.set(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
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
        formVisitBody.setSpParameter(spParameter);

        return formVisitBody;
    }
}
