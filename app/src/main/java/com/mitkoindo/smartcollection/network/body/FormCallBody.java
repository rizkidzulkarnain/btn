package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class FormCallBody {

    @SerializedName("DatabaseID")
    @Expose
    private String databaseId;

    @SerializedName("SpName")
    @Expose
    private String spName;

    @SerializedName("SpParameter")
    @Expose
    private SpParameterFormCall spParameterFormCall;


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

    public SpParameterFormCall getSpParameterFormCall() {
        return spParameterFormCall;
    }

    public void setSpParameterFormCall(SpParameterFormCall spParameterFormCall) {
        this.spParameterFormCall = spParameterFormCall;
    }


    public static class SpParameterFormCall {

        @SerializedName("ACC_NO")
        @Expose
        private String accountNo;                   // Mandatory accountNo = nomor rekening

        @SerializedName("USERID")
        @Expose
        private String userId;                      // Mandatory userId

        @SerializedName("TUJUAN")
        @Expose
        private String tujuan;                      // Mandatory tujuan

        @SerializedName("CONTACT_NO")
        @Expose
        private String contactNo;

        @SerializedName("RELATIONSHIP")
        @Expose
        private String relationship;                // Mandatory relationship = hubungan dengan debitur

        @SerializedName("SPOKE_TO")
        @Expose
        private String spokeTo;                     // Mandatory spokeTo = berbicara dengan

        @SerializedName("NOTES")
        @Expose
        private String notes;                               // Mandatory notes = catatan

        @SerializedName("RESULT")
        @Expose
        private String result;                      // Mandatory result = hasil panggilan

        @SerializedName("REASON_NON_PAYMENT")
        @Expose
        private String reasonNoPayment;             // Mandatory reasonNoPayment = alasan menunggak

        @SerializedName("REASON_NON_PAYMENT_DESC")
        @Expose
        private String reasonNonPaymentDesc;

        @SerializedName("DATE_ACTION")
        @Expose
        private String dateAction;                  // Mandatory dateAction = tanggal tindak lanjut

        @SerializedName("NEXT_ACTION")
        @Expose
        private String nextAction;                  // Mandatory nextAction = tindak lanjut

        @SerializedName("RESULTDATE")
        @Expose
        private String resultDate;                  // Mandatory resultDate = tanggal janji bayar

        @SerializedName("PTP_AMOUNT")
        @Expose
        private double ptpAmount;                   // Mandatory ptpAmount = jumlah yang akan  disetor

        @SerializedName("GEO_LATITUDE")
        @Expose
        private double geoLatitude;

        @SerializedName("GEO_LONGITUDE")
        @Expose
        private double geoLongitude;

        @SerializedName("GEO_ADDRESS")
        @Expose
        private String geoAddress;

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTujuan() {
            return tujuan;
        }

        public void setTujuan(String tujuan) {
            this.tujuan = tujuan;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getSpokeTo() {
            return spokeTo;
        }

        public void setSpokeTo(String spokeTo) {
            this.spokeTo = spokeTo;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getReasonNoPayment() {
            return reasonNoPayment;
        }

        public void setReasonNoPayment(String reasonNoPayment) {
            this.reasonNoPayment = reasonNoPayment;
        }

        public String getReasonNonPaymentDesc() {
            return reasonNonPaymentDesc;
        }

        public void setReasonNonPaymentDesc(String reasonNonPaymentDesc) {
            this.reasonNonPaymentDesc = reasonNonPaymentDesc;
        }

        public String getDateAction() {
            return dateAction;
        }

        public void setDateAction(String dateAction) {
            this.dateAction = dateAction;
        }

        public String getNextAction() {
            return nextAction;
        }

        public void setNextAction(String nextAction) {
            this.nextAction = nextAction;
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
    }
}
