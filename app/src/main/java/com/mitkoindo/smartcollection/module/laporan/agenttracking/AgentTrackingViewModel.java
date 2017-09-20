package com.mitkoindo.smartcollection.module.laporan.agenttracking;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.mitkoindo.smartcollection.base.ILifecycleViewModel;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.AgentTrackingBody;
import com.mitkoindo.smartcollection.objectdata.AgentTracking;
import com.mitkoindo.smartcollection.utils.Constant;
import com.mitkoindo.smartcollection.utils.Utils;

import java.util.ArrayList;
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
 * Created by ericwijaya on 9/18/17.
 */

public class AgentTrackingViewModel extends BaseObservable implements ILifecycleViewModel {

    public ObservableBoolean obsIsLoading = new ObservableBoolean();
    public ObservableField<Throwable> error = new ObservableField<>();
    public ObservableField<List<AgentTracking>> obsAgentTrackingResponse = new ObservableField<>();
    public ObservableBoolean obsIsEmpty = new ObservableBoolean(false);
    public ObservableField<String> obsTanggal = new ObservableField<>();
    public ObservableField<String> obsTanggalLayout = new ObservableField<>();
    public ObservableBoolean obsIsMap = new ObservableBoolean(true);

    private String mAccessToken;
    private CompositeDisposable composites = new CompositeDisposable();


    public AgentTrackingViewModel(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public void getListAgentTracking(String userId, String day) {
        AgentTrackingBody agentTrackingBody = new AgentTrackingBody();
        agentTrackingBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        agentTrackingBody.setSpName(RestConstants.AGENT_TRACKING_SP_NAME);

        AgentTrackingBody.SpParameter spParameter = new AgentTrackingBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setDay(day);

        agentTrackingBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).getListAgentTracking(agentTrackingBody)
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
                .subscribeWith(new DisposableObserver<List<AgentTracking>>() {
                    @Override
                    public void onNext(List<AgentTracking> listAgentTracking) {
                        List<AgentTracking> temp = new ArrayList<AgentTracking>();
                        for (AgentTracking agentTracking : listAgentTracking) {
                            AgentTracking item = agentTracking;
                            String dateFormatted = Utils.changeDateFormat(item.getCreatedDate(), Constant.DATE_FORMAT_AGENT_TRACKING_FROM_TIME, Constant.DATE_FORMAT_TIME);
                            item.setTimeFormatted(dateFormatted);
                            int iconDrawable = item.getIconDrawable();
                            item.setIconDrawable(iconDrawable);

                            temp.add(agentTracking);
                        }

                        obsAgentTrackingResponse.set(temp);
                        Timber.i("getListAgentTracking success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.set(e);
                        Timber.e("getListAgentTracking " + e.getMessage());
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
