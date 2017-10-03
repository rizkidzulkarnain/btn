package com.mitkoindo.smartcollection.event;

/**
 * Created by ericwijaya on 10/3/17.
 */

public class EventRefreshListDebitur {
    private String status;

    public EventRefreshListDebitur(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
