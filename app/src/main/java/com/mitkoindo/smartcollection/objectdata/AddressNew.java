package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 10/1/17.
 */

public class AddressNew {

    @SerializedName("KodeAlamat")
    @Expose
    private String kodeAlamat;

    @SerializedName("TipeAlamat")
    @Expose
    private String tipeAlamat;

    @SerializedName("Alamat")
    @Expose
    private String alamat;


    public String getKodeAlamat() {
        return kodeAlamat;
    }

    public void setKodeAlamat(String kodeAlamat) {
        this.kodeAlamat = kodeAlamat;
    }

    public String getTipeAlamat() {
        return tipeAlamat;
    }

    public void setTipeAlamat(String tipeAlamat) {
        this.tipeAlamat = tipeAlamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}

