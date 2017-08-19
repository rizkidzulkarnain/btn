package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.databinding.BaseObservable;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public DetailDebiturViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
