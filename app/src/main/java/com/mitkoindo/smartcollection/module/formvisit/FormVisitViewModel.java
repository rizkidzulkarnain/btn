package com.mitkoindo.smartcollection.module.formvisit;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.body.FormVisitDropDownBody;
import com.mitkoindo.smartcollection.network.models.DbParam;
import com.mitkoindo.smartcollection.network.models.Filter;
import com.mitkoindo.smartcollection.network.models.Sort;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class FormVisitViewModel extends BaseObservable implements ILifecycleViewModel {

    private Locale id = new Locale("in", "ID");
    private SimpleDateFormat dateFormatterDisplay = new SimpleDateFormat(Constant.DATE_FORMAT_DISPLAY_DATE, id);
    private SimpleDateFormat dateFormatterSend = new SimpleDateFormat(Constant.DATE_FORMAT_SEND_DATE, id);

    private CompositeDisposable composites = new CompositeDisposable();

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<Throwable> errorSave = new ObservableField<>();
    public ObservableBoolean obsIsSaveSuccess = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowTanggalJanjiDebitur = new ObservableBoolean(false);
    public ObservableBoolean obsIsShowJumlahYangAkanDisetor = new ObservableBoolean(false);

    public ObservableField<String> tujuanKunjungan = new ObservableField<>();
    public ObservableField<String> alamatYangDikunjungi = new ObservableField<>();
    public ObservableField<String> namaOrangYangDikunjungi = new ObservableField<>();
    public ObservableField<String> hubunganDenganDebitur = new ObservableField<>();
    public ObservableField<String> hasilKunjungan = new ObservableField<>();
    public ObservableField<String> tanggalJanjiDebitur = new ObservableField<>();
    public ObservableField<String> jumlahYangAkanDisetor = new ObservableField<>();
    public ObservableField<String> statusAgunan = new ObservableField<>();
    public ObservableField<String> kondisiAgunan = new ObservableField<>();
    public ObservableField<String> alasanMenunggak = new ObservableField<>();
    public ObservableField<String> tindakLanjut = new ObservableField<>();
    public ObservableField<String> tanggalTindakLanjut = new ObservableField<>();
    public ObservableField<String> catatan = new ObservableField<>();
    public ObservableField<Boolean> isFotoAgunan2Show = new ObservableField<>(false);
    public ObservableField<List<DropDownAddress>> mObsListDropDownAddress = new ObservableField<>();

    public SpParameterFormVisitDb spParameterFormVisitDb = new SpParameterFormVisitDb();


    public FormVisitViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }


    public void setTanggalJanjiDebitur(Date tanggalJanjiDebitur) {
        spParameterFormVisitDb.setResultDate(dateFormatterSend.format(tanggalJanjiDebitur));
        this.tanggalJanjiDebitur.set(dateFormatterDisplay.format(tanggalJanjiDebitur));
    }

    public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
        spParameterFormVisitDb.setNextActionDate(dateFormatterSend.format(tanggalTindakLanjut));
        this.tanggalTindakLanjut.set(dateFormatterDisplay.format(tanggalTindakLanjut));
    }

    public void getListAddress(String accessToken, String noRekening) {
        Disposable disposable = ApiUtils.getRestServices(accessToken).getDropDownAddress(createAddressBody("'" + noRekening + "'"))
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
                .subscribeWith(new DisposableObserver<List<DropDownAddress>>() {
                    @Override
                    public void onNext(List<DropDownAddress> listAddress) {
                        mObsListDropDownAddress.set(listAddress);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListAddress " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void saveFormVisit(String accessToken) {
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
                        Timber.i("Sukses save form visit");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("Save Form Visit Error : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    public void uploadFile(String accessToken, String filePath) {
        File file = new File(filePath);

        // create RequestBody instance from file
        Uri uri = Uri.fromFile(file);
        RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uri)), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Disposable disposable = ApiUtils.getMultipartServices(accessToken).uploadFile(body)
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
                .subscribeWith(new DisposableObserver<MultipartResponse>() {
                    @Override
                    public void onNext(MultipartResponse multipartResponse) {
                        String relativePath = multipartResponse.getRelativePath();
                        Timber.i("Sukses upload file");
                        Timber.i("Path " +  relativePath);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();

                        }
                        error.set(e);
                        Timber.e("Upload File Error : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        composites.add(disposable);
    }

    private FormVisitDropDownBody createAddressBody(String noRekening) {
        Sort sort = new Sort();
        sort.setProperty(RestConstants.DROP_DOWN_ADDRESS_SORT_PROPERTY);
        sort.setDirection(RestConstants.ORDER_DIRECTION_ASC_VALUE);

        List<Sort> listSort = new ArrayList<>();
        listSort.add(sort);

        Filter filter = new Filter();
        filter.setProperty(RestConstants.DROP_DOWN_ADDRESS_FILTER_PROPERTY);
        filter.setOperator(RestConstants.DROP_DOWN_ADDRESS_FILTER_OPERATOR);
        filter.setValue(noRekening);

        List<Filter> listFilter = new ArrayList<>();
        listFilter.add(filter);

        DbParam dbParam = new DbParam();
        dbParam.setPage(1);
        dbParam.setLimit(15);
        dbParam.setSort(listSort);
        dbParam.setFilter(listFilter);

        FormVisitDropDownBody formVisitDropDownBody = new FormVisitDropDownBody();
        formVisitDropDownBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        formVisitDropDownBody.setViewName(RestConstants.DROP_DOWN_ADDRESS_VIEW_NAME);
        formVisitDropDownBody.setdBParam(dbParam);

        return formVisitDropDownBody;
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
