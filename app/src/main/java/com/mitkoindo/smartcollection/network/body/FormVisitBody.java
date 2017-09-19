package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class FormVisitBody {

    @SerializedName("DatabaseID")
    @Expose
    private String databaseId;

    @SerializedName("SpName")
    @Expose
    private String spName;

    @SerializedName("SpParameter")
    @Expose
    private SpParameter spParameter;


    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public SpParameter getSpParameter() {
        return spParameter;
    }

    public void setSpParameter(SpParameter spParameter) {
        this.spParameter = spParameter;
    }


    @Parcel(Parcel.Serialization.BEAN)
    public static class SpParameter {
        @SerializedName("USERID")
        @Expose
        private String userId;                  // Mandatory userId = userId

        @SerializedName("ACC_NO")
        @Expose
        private String accNo;                   // Mandatory accNo = nomor rekening

        @SerializedName("PERSON_VISIT_REL")
        @Expose
        private String personVisitRel;          // Mandatory personVisitRel = hubungan orang yang dikunjungi

        @SerializedName("PERSON_VISIT")
        @Expose
        private String personVisit;             // Mandatory personVisit = nama orang yang dikunjungi

        @SerializedName("NOTES")
        @Expose
        private String notes;                   // Mandatory notes = catatan

        @SerializedName("CU_ADDR")
        @Expose
        private String cuAddr;                  // Mandatory cuAddr = alamat

        @SerializedName("tujuan")
        @Expose
        private String tujuan;                  // Mandatory tujuan = tujuan kunjungan

        @SerializedName("NEXT_ACTION")
        @Expose
        private String nextAction;              // Mandatory nextAction = tindak lanjut

        @SerializedName("NEXT_ACTION_DATE")
        @Expose
        private String nextActionDate;          // Mandatory nextActionDate = tanggal tindak lanjut

        @SerializedName("COLLSTATDESC")
        @Expose
        private String collStatDesc;            // Mandatory collStatDesc = status agunan

        @SerializedName("COLLCONDDESC")
        @Expose
        private String collCondDesc;            // Mandatory collCondDesc = kondisi agunan

        @SerializedName("REASON_NON_PAYMENT")
        @Expose
        private String reasonNonPayment;        // Mandatory reasonNonPayment = alasan tidak bayar

        @SerializedName("REASON_NON_PAYMENT_DESC")
        @Expose
        private String reasonNonPaymentDesc;

        @SerializedName("RESULT")
        @Expose
        private String result;                  // Mandatory result = hasil kunjungan

        @SerializedName("RESULTDATE")
        @Expose
        private String resultDate;              // Mandatory resultDate = tanggal janji debitur

        @SerializedName("PTPAMOUNT")
        @Expose
        private double ptpAmount;              // Mandatory ptpAmount = jumlah yang akan disetor

        @SerializedName("GEO_LATITUDE")
        @Expose
        private double geoLatitude;

        @SerializedName("GEO_LONGITUDE")
        @Expose
        private double geoLongitude;

        @SerializedName("GEO_ADDRESS")
        @Expose
        private String geoAddress;

        private String photoDebitur;
        private String photoAgunan1;
        private String photoAgunan2;
        private String photoSignature;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }

        public String getPersonVisitRel() {
            return personVisitRel;
        }

        public void setPersonVisitRel(String personVisitRel) {
            this.personVisitRel = personVisitRel;
        }

        public String getPersonVisit() {
            return personVisit;
        }

        public void setPersonVisit(String personVisit) {
            this.personVisit = personVisit;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getCuAddr() {
            return cuAddr;
        }

        public void setCuAddr(String cuAddr) {
            this.cuAddr = cuAddr;
        }

        public String getTujuan() {
            return tujuan;
        }

        public void setTujuan(String tujuan) {
            this.tujuan = tujuan;
        }

        public String getNextAction() {
            return nextAction;
        }

        public void setNextAction(String nextAction) {
            this.nextAction = nextAction;
        }

        public String getNextActionDate() {
            return nextActionDate;
        }

        public void setNextActionDate(String nextActionDate) {
            this.nextActionDate = nextActionDate;
        }

        public String getCollStatDesc() {
            return collStatDesc;
        }

        public void setCollStatDesc(String collStatDesc) {
            this.collStatDesc = collStatDesc;
        }

        public String getCollCondDesc() {
            return collCondDesc;
        }

        public void setCollCondDesc(String collCondDesc) {
            this.collCondDesc = collCondDesc;
        }

        public String getReasonNonPayment() {
            return reasonNonPayment;
        }

        public void setReasonNonPayment(String reasonNonPayment) {
            this.reasonNonPayment = reasonNonPayment;
        }

        public String getReasonNonPaymentDesc() {
            return reasonNonPaymentDesc;
        }

        public void setReasonNonPaymentDesc(String reasonNonPaymentDesc) {
            this.reasonNonPaymentDesc = reasonNonPaymentDesc;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResultDate() {
            return resultDate;
        }

        public void setResultDate(String resultDate) {
            this.resultDate = resultDate;
        }

        public double getPtpAmount() {
            return ptpAmount;
        }

        public void setPtpAmount(double ptpAmount) {
            this.ptpAmount = ptpAmount;
        }

        public double getGeoLatitude() {
            return geoLatitude;
        }

        public void setGeoLatitude(double geoLatitude) {
            this.geoLatitude = geoLatitude;
        }

        public double getGeoLongitude() {
            return geoLongitude;
        }

        public void setGeoLongitude(double geoLongitude) {
            this.geoLongitude = geoLongitude;
        }

        public String getGeoAddress() {
            return geoAddress;
        }

        public void setGeoAddress(String geoAddress) {
            this.geoAddress = geoAddress;
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
    }
}
