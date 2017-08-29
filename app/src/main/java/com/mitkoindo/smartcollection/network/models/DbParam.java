package com.mitkoindo.smartcollection.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class DbParam {

    @SerializedName("Page")
    @Expose
    private Integer page;
    @SerializedName("Limit")
    @Expose
    private Integer limit;
    @SerializedName("Sort")
    @Expose
    private List<Sort> sort = null;
    @SerializedName("Filter")
    @Expose
    private List<Filter> filter = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<Sort> getSort() {
        return sort;
    }

    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    public List<Filter> getFilter() {
        return filter;
    }

    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }

}


