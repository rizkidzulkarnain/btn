package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.databinding.BaseObservable;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class ListDebiturViewModel extends BaseObservable implements ILifecycleViewModel {

    private CompositeDisposable composites = new CompositeDisposable();

    public ListDebiturViewModel() {

    }

    @Override
    public void onDestroy() {
        composites.clear();
    }
}
