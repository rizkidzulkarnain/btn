package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebitur {

    @SerializedName("NamaDebitur")
    @Expose
    private String namaDebitur;

    @SerializedName("NoCIF")
    @Expose
    private String noCif;

    @SerializedName("NomorRekening")
    @Expose
    private String noRekening;

    @SerializedName("TotalTunggakan")
    @Expose
    private String totalTunggakan;

    @SerializedName("LastPaymentDate")
    @Expose
    private String lastPaymentDate;

    @SerializedName("DPD")
    @Expose
    private String dpd;

    @SerializedName("AngsuranPerBulan")
    @Expose
    private String angsuranPerBulan;

    @SerializedName("TotalKewajiban")
    @Expose
    private String totalKewajiban;

    @SerializedName("Kolektibilitas")
    @Expose
    private String kolektabilitas;

    @SerializedName("TindakLanjut")
    @Expose
    private String tindakLanjut;

    @SerializedName("StatusAkhir")
    @Expose
    private String status;

    private String ptp;
    private String besaranPtp;

    @SerializedName("AlamatRumah")
    @Expose
    private String alamatRumah;

    @SerializedName("AlamatAgunan")
    @Expose
    private String alamatAgunan;

    @SerializedName("AlamatKantor")
    @Expose
    private String alamatKantor;
    private String alamatSaatIni;

    @SerializedName("UserAssignDate")
    @Expose
    private String userAssignDate;


    public DetailDebitur() {
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





    //parse form JSON
    public void ParseData(String jsonString)
    {
        try
        {
            //converst string to json object
            JSONObject dataObject = new JSONObject(jsonString);

            //get data
            noCif = dataObject.getString("NoCIF");
            noRekening = dataObject.getString("NomorRekening");
            namaDebitur = dataObject.getString("NamaDebitur");
            totalTunggakan = dataObject.getString("TotalTunggakan");
            lastPaymentDate = dataObject.getString("LastPaymentDate");
            dpd = dataObject.getString("DPD");
            angsuranPerBulan = dataObject.getString("AngsuranPerBulan");
            totalKewajiban = dataObject.getString("TotalKewajiban");
            kolektabilitas = dataObject.getString("Kolektibilitas");
            tindakLanjut = dataObject.getString("TindakLanjut");
            status = dataObject.optString("StatusAkhir", "");
            alamatRumah = dataObject.getString("AlamatRumah");
            alamatKantor = dataObject.getString("AlamatKantor");
            alamatAgunan = dataObject.optString("AlamatAgunan", "");
            userAssignDate = dataObject.getString("UserAssignDate");

            if (tindakLanjut == null || tindakLanjut.equals("null"))
                tindakLanjut = "";
            if (status == null || status.equals("null"))
                status = "";
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
