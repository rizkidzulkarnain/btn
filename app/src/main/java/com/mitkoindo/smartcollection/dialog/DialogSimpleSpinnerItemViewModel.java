package com.mitkoindo.smartcollection.dialog;

import android.databinding.Bindable;
import android.view.View;

import com.mitkoindo.smartcollection.bindings.ChoiceObservable;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class DialogSimpleSpinnerItemViewModel extends ChoiceObservable<Object> {
    private String name;
    private int viewId;

    public DialogSimpleSpinnerItemViewModel(String name, int viewId) {
        this.name = name;
        this.viewId = viewId;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public View.OnClickListener onItemClicked() {
        return v -> EventBus.getDefault().post(new EventDialogSimpleSpinnerSelected(name, viewId));
    }
}
