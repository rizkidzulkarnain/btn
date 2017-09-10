package com.mitkoindo.smartcollection.dialog;

import android.databinding.Bindable;
import android.view.View;

import com.mitkoindo.smartcollection.bindings.ChoiceObservable;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ericwijaya on 9/8/17.
 */

public class DialogTwoLineSpinnerItemViewModel extends ChoiceObservable<Object> {
    private DialogTwoLineSpinnerAdapter.TwoLineSpinner item;
    private int viewId;

    public DialogTwoLineSpinnerItemViewModel(DialogTwoLineSpinnerAdapter.TwoLineSpinner item, int viewId) {
        this.item = item;
        this.viewId = viewId;
    }

    @Bindable
    public String getTitle() {
        return item.title;
    }

    @Bindable
    public String getDescription() {
        return item.description;
    }

    public View.OnClickListener onItemClicked() {
        return v -> EventBus.getDefault().post(new EventDialogSimpleSpinnerSelected(item.description, viewId));
    }
}
