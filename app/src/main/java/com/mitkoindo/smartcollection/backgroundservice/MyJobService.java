package com.mitkoindo.smartcollection.backgroundservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.RealmHelper;
import com.mitkoindo.smartcollection.module.laporan.LaporanCallActivity;
import com.mitkoindo.smartcollection.module.laporan.LaporanVisitActivity;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.FormCallBody;
import com.mitkoindo.smartcollection.network.body.FormVisitBody;
import com.mitkoindo.smartcollection.network.response.FormCallResponse;
import com.mitkoindo.smartcollection.network.response.FormVisitResponse;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
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
import com.mitkoindo.smartcollection.utils.FileUtils;
import com.mitkoindo.smartcollection.utils.ProfileUtils;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public void onCreate() {
        super.onCreate();

        Timber.i("___onCreate");
    }

    @Override
    public void onDestroy() {
        Timber.i("___onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Timber.i("___onStartJob");

        mJobParameters = job;
        mAccessToken = job.getExtras().getString(ACCESS_TOKEN);
        mUserId = job.getExtras().getString(USER_ID);

        Timber.i("onStartJob " + mAccessToken);
        Timber.i("onStartJob " + mUserId);

        getBundle();
        sendFormCall();
        sendFormVisit();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Timber.i("___onStopJob");
        return false;
    }

    private void getBundle() {

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getBundle(RestConstants.DATABASE_ID_VALUE, "20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OfflineBundleResponse>() {
                    @Override
                    public void onNext(OfflineBundleResponse offlineBundleResponse) {
                        Timber.i("___getBundle success");

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

//                        NotificationCompat.Builder mBuilder =
//                                new NotificationCompat.Builder(MyJobService.this)
//                                        .setSmallIcon(R.drawable.ic_folder_download_white_24dp)
//                                        .setContentTitle("Get Bundle " + mUserId)
//                                        .setContentText("");
//
//                        Random r = new Random();
//                        int mNotificationId = r.nextInt((10000-10)+1)+10;
//                        // Gets an instance of the NotificationManager service
//                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        // Builds the notification and issues it.
//                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("___getBundle Error");
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

                                            if (formCallResponses.size() > 0) {
                                                Intent intent = new Intent(MyJobService.this, LaporanCallActivity.class);
                                                intent.putExtra("CallID", formCallResponses.get(0).getMessage());

                                                Random r = new Random();
                                                int notificationId = r.nextInt((10000 - 10) + 1) + 10;
                                                PendingIntent contentIntent = PendingIntent.getActivity(MyJobService.this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

//                                            Show Notification
                                                NotificationCompat.Builder mBuilder =
                                                        new NotificationCompat.Builder(MyJobService.this)
                                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .bigText(getString(R.string.BackgroundService_DataOfflineFormCallDikirimkan)))
                                                                .setContentTitle(getString(R.string.BackgroundService_NotificationTitle))
                                                                .setContentText(getString(R.string.BackgroundService_DataOfflineFormCallDikirimkan))
                                                                .setContentIntent(contentIntent)
                                                                .setAutoCancel(true);

                                                // Gets an instance of the NotificationManager service
                                                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                // Builds the notification and issues it.
                                                mNotifyMgr.notify(notificationId, mBuilder.build());
                                            }

//                                            Delete Form Call from Db
                                            RealmHelper.deleteFormCall(spParameterFormCall.getAccountNo());
                                        }
                                    });
                        }
                    })
                    .subscribeWith(new DisposableObserver<List<FormCallResponse>>() {
                        @Override
                        public void onNext(@NonNull List<FormCallResponse> formCallResponses) {
                            Timber.i("___sendFormCall onNext " + formCallResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("___sendFormCall Error");
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("___sendFormCall onComplete");
                        }
                    });

            mComposites.add(disposable);
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
                        public ObservableSource<List<FormVisitResponse>> apply(@NonNull SpParameterFormVisitDb spParameterFormVisitDb) throws Exception {

                            File fileFotoDebitur = new File(spParameterFormVisitDb.getPhotoDebiturPath());
                            Uri uriFotoDebitur = Uri.fromFile(fileFotoDebitur);
                            RequestBody requestFileFotoDebitur = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriFotoDebitur)), fileFotoDebitur);
                            MultipartBody.Part bodyDebitur = MultipartBody.Part.createFormData("file", fileFotoDebitur.getName(), requestFileFotoDebitur);

                            return ApiUtils.getMultipartServices(mAccessToken).uploadFile(bodyDebitur)
                                    .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                                        @Override
                                        public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
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
                                        public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
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
                                        public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
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
                                        public ObservableSource<List<FormVisitResponse>> apply(@NonNull MultipartResponse multipartResponse) throws Exception {
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


                                            if (formVisitResponses.size() > 0) {
                                                Intent intent = new Intent(MyJobService.this, LaporanVisitActivity.class);
                                                intent.putExtra("VisitID", formVisitResponses.get(0).getMessage());

                                                Random r = new Random();
                                                int notificationId = r.nextInt((10000 - 10) + 1) + 10;
                                                PendingIntent contentIntent = PendingIntent.getActivity(MyJobService.this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

//                                            Show Notification
                                                NotificationCompat.Builder mBuilder =
                                                        new NotificationCompat.Builder(MyJobService.this)
                                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .bigText(getString(R.string.BackgroundService_DataOfflineFormVisitDikirimkan)))
                                                                .setContentTitle(getString(R.string.BackgroundService_NotificationTitle))
                                                                .setContentText(getString(R.string.BackgroundService_DataOfflineFormVisitDikirimkan))
                                                                .setContentIntent(contentIntent)
                                                                .setAutoCancel(true);

                                                // Gets an instance of the NotificationManager service
                                                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                // Builds the notification and issues it.
                                                mNotifyMgr.notify(notificationId, mBuilder.build());
                                            }

//                                            Delete Form Visit from Db
                                            RealmHelper.deleteFormVisit(spParameterFormVisitDb.getAccNo());
                                        }
                                    });
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<FormVisitResponse>>() {
                        @Override
                        public void onNext(List<FormVisitResponse> formVisitResponses) {
                            Timber.i("___sendFormVisit onNext " + formVisitResponses.get(0).getMessage());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e("___sendFormVisit Error");
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("___sendFormVisit onComplete");
                        }
                    });

            mComposites.add(disposable);
        }
    }
}
