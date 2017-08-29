package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mitkoindo.smartcollection.network.models.DbParam;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class FormVisitDropDownBody {

    @SerializedName("DatabaseID")
    @Expose
    private String databaseId;

    @SerializedName("ViewName")
    @Expose
    private String viewName;

    @SerializedName("DBParam")
    @Expose
    private DbParam dBParam;


    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public DbParam getdBParam() {
        return dBParam;
    }

    public void setdBParam(DbParam dBParam) {
        this.dBParam = dBParam;
    }
}
