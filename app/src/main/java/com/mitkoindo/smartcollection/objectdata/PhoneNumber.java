package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class PhoneNumber {

    @SerializedName("NamaKontak")
    @Expose
    private String namaKontak;
    @SerializedName("Hubungan")
    @Expose
    private String hubungan;
    @SerializedName("JenisKontak")
    @Expose
    private String jenisKontak;
    @SerializedName("NomorKontak")
    @Expose
    private String nomorKontak;

    public String getNamaKontak() {
        return namaKontak;
    }

    public void setNamaKontak(String namaKontak) {
        this.namaKontak = namaKontak;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getJenisKontak() {
        return jenisKontak;
    }

    public void setJenisKontak(String jenisKontak) {
        this.jenisKontak = jenisKontak;
    }

    public String getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(String nomorKontak) {
        this.nomorKontak = nomorKontak;
    }
}
