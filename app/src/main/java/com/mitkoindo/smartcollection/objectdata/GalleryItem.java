package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 9/19/17.
 */

public class GalleryItem {

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("IDVisit")
    @Expose
    private String idVisit;

    @SerializedName("PhotoDebitur")
    @Expose
    private String photoDebitur;

    @SerializedName("PhotoAgunan1")
    @Expose
    private String photoAgunan1;

    @SerializedName("PhotoAgunan2")
    @Expose
    private String photoAgunan2;

    @SerializedName("PhotoSignature")
    @Expose
    private String photoSignature;

    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;


    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(String idVisit) {
        this.idVisit = idVisit;
    }

    public String getPhotoDebitur() {
        return photoDebitur;
    }

    public void setPhotoDebitur(String photoDebitur) {
        this.photoDebitur = photoDebitur;
    }

    public String getPhotoAgunan1() {
        return photoAgunan1;
    }

    public void setPhotoAgunan1(String photoAgunan1) {
        this.photoAgunan1 = photoAgunan1;
    }

    public String getPhotoAgunan2() {
        return photoAgunan2;
    }

    public void setPhotoAgunan2(String photoAgunan2) {
        this.photoAgunan2 = photoAgunan2;
    }

    public String getPhotoSignature() {
        return photoSignature;
    }

    public void setPhotoSignature(String photoSignature) {
        this.photoSignature = photoSignature;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
