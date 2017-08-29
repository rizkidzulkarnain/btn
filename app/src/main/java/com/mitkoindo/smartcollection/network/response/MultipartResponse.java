package com.mitkoindo.smartcollection.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/26/17.
 */

public class MultipartResponse {

    @SerializedName("Size")
    @Expose
    private Integer size;
    @SerializedName("RelativePath")
    @Expose
    private String relativePath;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
