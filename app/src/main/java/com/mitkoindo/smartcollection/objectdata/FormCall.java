package com.mitkoindo.smartcollection.objectdata;

import java.util.Date;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class FormCall {

    private String tujuan;
    private String berbicaraDengan;
    private String hubungan;
    private String hasilPanggilan;
    private Date tanggalJanjiBayar;
    private Double jumlahYangAkanDisetor;
    private Date tanggalHasilPanggilan;
    private String alasanTidakBayar;
    private String tindakLanjut;
    private Date tanggalTindakLanjut;
    private String catatan;
    private String noTeleponTujuan;

    public FormCall() {
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getBerbicaraDengan() {
        return berbicaraDengan;
    }

    public void setBerbicaraDengan(String berbicaraDengan) {
        this.berbicaraDengan = berbicaraDengan;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getHasilPanggilan() {
        return hasilPanggilan;
    }

    public void setHasilPanggilan(String hasilPanggilan) {
        this.hasilPanggilan = hasilPanggilan;
    }

    public Date getTanggalJanjiBayar() {
        return tanggalJanjiBayar;
    }

    public void setTanggalJanjiBayar(Date tanggalJanjiBayar) {
        this.tanggalJanjiBayar = tanggalJanjiBayar;
    }

    public Double getJumlahYangAkanDisetor() {
        return jumlahYangAkanDisetor;
    }

    public void setJumlahYangAkanDisetor(Double jumlahYangAkanDisetor) {
        this.jumlahYangAkanDisetor = jumlahYangAkanDisetor;
    }

    public Date getTanggalHasilPanggilan() {
        return tanggalHasilPanggilan;
    }

    public void setTanggalHasilPanggilan(Date tanggalHasilPanggilan) {
        this.tanggalHasilPanggilan = tanggalHasilPanggilan;
    }

    public String getAlasanTidakBayar() {
        return alasanTidakBayar;
    }

    public void setAlasanTidakBayar(String alasanTidakBayar) {
        this.alasanTidakBayar = alasanTidakBayar;
    }

    public String getTindakLanjut() {
        return tindakLanjut;
    }

    public void setTindakLanjut(String tindakLanjut) {
        this.tindakLanjut = tindakLanjut;
    }

    public Date getTanggalTindakLanjut() {
        return tanggalTindakLanjut;
    }

    public void setTanggalTindakLanjut(Date tanggalTindakLanjut) {
        this.tanggalTindakLanjut = tanggalTindakLanjut;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getNoTeleponTujuan() {
        return noTeleponTujuan;
    }

    public void setNoTeleponTujuan(String noTeleponTujuan) {
        this.noTeleponTujuan = noTeleponTujuan;
    }
}
