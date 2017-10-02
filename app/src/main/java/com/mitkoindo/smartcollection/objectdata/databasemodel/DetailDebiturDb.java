package com.mitkoindo.smartcollection.objectdata.databasemodel;

import com.mitkoindo.smartcollection.objectdata.DetailDebitur;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/9/17.
 */

public class DetailDebiturDb extends RealmObject {

    private String namaDebitur;

    private String noCif;

    @PrimaryKey
    private String noRekening;

    private String totalTunggakan;

    private String lastPaymentDate;

    private String dpd;

    private String angsuranPerBulan;

    private String totalKewajiban;

    private String kolektabilitas;

    private String tindakLanjut;

    private String tindakLanjutDesc;

    private String status;

    private String ptp;

    private String besaranPtp;

    private String alamatRumah;

    private String alamatAgunan;

    private String alamatKantor;

    private String alamatSaatIni;

    private String userAssignDate;


    public DetailDebiturDb() {
    }

    public DetailDebiturDb(DetailDebitur detailDebitur) {
        namaDebitur = detailDebitur.getNamaDebitur();
        noCif = detailDebitur.getNoCif();
        noRekening = detailDebitur.getNoRekening();
        totalTunggakan = detailDebitur.getTotalTunggakan();
        lastPaymentDate = detailDebitur.getLastPaymentDate();
        dpd = detailDebitur.getDpd();
        angsuranPerBulan = detailDebitur.getAngsuranPerBulan();
        totalKewajiban = detailDebitur.getTotalKewajiban();
        kolektabilitas = detailDebitur.getKolektabilitas();
        tindakLanjut = detailDebitur.getTindakLanjut();
        tindakLanjutDesc = detailDebitur.getTindakLanjutDesc();
        status = detailDebitur.getStatus();
        ptp = detailDebitur.getPtp();
        besaranPtp = detailDebitur.getBesaranPtp();
        alamatRumah = detailDebitur.getAlamatRumah();
        alamatAgunan = detailDebitur.getAlamatAgunan();
        alamatKantor = detailDebitur.getAlamatKantor();
        alamatSaatIni = detailDebitur.getAlamatSaatIni();
        userAssignDate = detailDebitur.getUserAssignDate();
    }

    public DetailDebitur toDetailDebitur() {
        DetailDebitur detailDebitur = new DetailDebitur();
        detailDebitur.setNamaDebitur(namaDebitur);
        detailDebitur.setNoCif(noCif);
        detailDebitur.setNoRekening(noRekening);
        detailDebitur.setTotalTunggakan(totalTunggakan);
        detailDebitur.setLastPaymentDate(lastPaymentDate);
        detailDebitur.setDpd(dpd);
        detailDebitur.setAngsuranPerBulan(angsuranPerBulan);
        detailDebitur.setTotalKewajiban(totalKewajiban);
        detailDebitur.setKolektabilitas(kolektabilitas);
        detailDebitur.setTindakLanjut(tindakLanjut);
        detailDebitur.setTindakLanjutDesc(tindakLanjutDesc);
        detailDebitur.setStatus(status);
        detailDebitur.setPtp(ptp);
        detailDebitur.setBesaranPtp(besaranPtp);
        detailDebitur.setAlamatRumah(alamatRumah);
        detailDebitur.setAlamatAgunan(alamatAgunan);
        detailDebitur.setAlamatKantor(alamatKantor);
        detailDebitur.setAlamatSaatIni(alamatSaatIni);
        detailDebitur.setUserAssignDate(userAssignDate);

        return detailDebitur;
    }

    public String getNamaDebitur() {
        return namaDebitur;
    }

    public void setNamaDebitur(String namaDebitur) {
        this.namaDebitur = namaDebitur;
    }

    public String getNoCif() {
        return noCif;
    }

    public void setNoCif(String noCif) {
        this.noCif = noCif;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getTotalTunggakan() {
        return totalTunggakan;
    }

    public void setTotalTunggakan(String totalTunggakan) {
        this.totalTunggakan = totalTunggakan;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getDpd() {
        return dpd;
    }

    public void setDpd(String dpd) {
        this.dpd = dpd;
    }

    public String getAngsuranPerBulan() {
        return angsuranPerBulan;
    }

    public void setAngsuranPerBulan(String angsuranPerBulan) {
        this.angsuranPerBulan = angsuranPerBulan;
    }

    public String getTotalKewajiban() {
        return totalKewajiban;
    }

    public void setTotalKewajiban(String totalKewajiban) {
        this.totalKewajiban = totalKewajiban;
    }

    public String getKolektabilitas() {
        return kolektabilitas;
    }

    public void setKolektabilitas(String kolektabilitas) {
        this.kolektabilitas = kolektabilitas;
    }

    public String getTindakLanjut() {
        return tindakLanjut;
    }

    public void setTindakLanjut(String tindakLanjut) {
        this.tindakLanjut = tindakLanjut;
    }

    public String getTindakLanjutDesc() {
        return tindakLanjutDesc;
    }

    public void setTindakLanjutDesc(String tindakLanjutDesc) {
        this.tindakLanjutDesc = tindakLanjutDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPtp() {
        return ptp;
    }

    public void setPtp(String ptp) {
        this.ptp = ptp;
    }

    public String getBesaranPtp() {
        return besaranPtp;
    }

    public void setBesaranPtp(String besaranPtp) {
        this.besaranPtp = besaranPtp;
    }

    public String getAlamatRumah() {
        return alamatRumah;
    }

    public void setAlamatRumah(String alamatRumah) {
        this.alamatRumah = alamatRumah;
    }

    public String getAlamatAgunan() {
        return alamatAgunan;
    }

    public void setAlamatAgunan(String alamatAgunan) {
        this.alamatAgunan = alamatAgunan;
    }

    public String getAlamatKantor() {
        return alamatKantor;
    }

    public void setAlamatKantor(String alamatKantor) {
        this.alamatKantor = alamatKantor;
    }

    public String getAlamatSaatIni() {
        return alamatSaatIni;
    }

    public void setAlamatSaatIni(String alamatSaatIni) {
        this.alamatSaatIni = alamatSaatIni;
    }

    public String getUserAssignDate() {
        return userAssignDate;
    }

    public void setUserAssignDate(String userAssignDate) {
        this.userAssignDate = userAssignDate;
    }
}
