package com.mitkoindo.smartcollection.bindings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mitkoindo.smartcollection.BR;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class ChoiceObservable<T> extends BaseObservable {
    private T value;
    private boolean selected;

    public ChoiceObservable() {
    }

    public ChoiceObservable(T value) {
        this.value = value;
    }

    public ChoiceObservable(T value, boolean selected) {
        this.value = value;
        this.selected = selected;
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyPropertyChanged(BR.value);
    }
}
