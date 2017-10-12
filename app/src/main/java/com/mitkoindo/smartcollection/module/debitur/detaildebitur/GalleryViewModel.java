package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.GalleryBody;
import com.mitkoindo.smartcollection.objectdata.GalleryItem;

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
 * Created by ericwijaya on 9/19/17.
 */

public class GalleryViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<GalleryItem>> obsListGalleryItem = new ObservableField<>();
    public ObservableField<GalleryItem> obsGalleryItem = new ObservableField<>();
    public ObservableBoolean obsIsAgunan1Show = new ObservableBoolean();
    public ObservableBoolean obsIsAgunan2Show = new ObservableBoolean();
    public ObservableBoolean obsIsSignatureShow = new ObservableBoolean();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();

    public GalleryViewModel(String accessToken) {
        mAccessToken = accessToken;
    }

    public void getGallery(String nomorRekening) {
        GalleryBody galleryBody = new GalleryBody();
        galleryBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        galleryBody.setSpName(RestConstants.GALLERY_SP_NAME);

        GalleryBody.SpParameter spParameter = new GalleryBody.SpParameter();
        spParameter.setAccNo(nomorRekening);

        galleryBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getGallery(galleryBody)
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
                .subscribeWith(new DisposableObserver<List<GalleryItem>>() {
                    @Override
                    public void onNext(List<GalleryItem> listGallery) {
                        obsListGalleryItem.set(listGallery);
                        if (listGallery.size() > 0) {
                            obsGalleryItem.set(listGallery.get(0));
                            if (!TextUtils.isEmpty(listGallery.get(0).getPhotoAgunan1())) {
                                obsIsAgunan1Show.set(true);
                            } else {
                                obsIsAgunan1Show.set(false);
                            }
                            if (!TextUtils.isEmpty(listGallery.get(0).getPhotoAgunan2())) {
                                obsIsAgunan2Show.set(true);
                            } else {
                                obsIsAgunan2Show.set(false);
                            }
                            if (!TextUtils.isEmpty(listGallery.get(0).getPhotoSignature())) {
                                obsIsSignatureShow.set(true);
                            } else {
                                obsIsSignatureShow.set(false);
                            }
                        }
                        Timber.i("getGallery success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getGallery " + e.getMessage());
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
