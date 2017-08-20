package com.mitkoindo.smartcollection.event;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class EventDialogSimpleSpinnerSelected {

    private String name;
    private int viewId;

    public EventDialogSimpleSpinnerSelected(String name, int viewId) {
        this.name = name;
        this.viewId = viewId;
    }

    public String getName() {
        return name;
    }

    public int getViewId() {
        return viewId;
    }
}
