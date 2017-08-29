package com.mitkoindo.smartcollection.network.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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


    public static class SpParameter {

        @SerializedName("ACC_NO")
        @Expose
        private String accountNo;                   // Mandatory accountNo = nomor rekening

        @SerializedName("USERID")
        @Expose
        private String userId;                      // Mandatory userId

        @SerializedName("RESULT")
        @Expose
        private String result;                      // Mandatory result = hasil panggilan

        @SerializedName("REASON_NON_PAYMENT")
        @Expose
        private String reasonNoPayment;             // Mandatory reasonNoPayment = alasan menunggak

        @SerializedName("DATE_ACTION")
        @Expose
        private String dateAction;                  // Mandatory dateAction = tanggal tindak lanjut

        @SerializedName("NEXT_ACTION")
        @Expose
        private String nextAction;                  // Mandatory nextAction = tindak lanjut

        @SerializedName("CONTACT_NO")
        @Expose
        private String contactNo;

        @SerializedName("RELATIONSHIP")
        @Expose
        private String relationship;                // Mandatory relationship = hubungan dengan debitur

        @SerializedName("SPOKE_TO")
        @Expose
        private String spokeTo;                     // Mandatory spokeTo = berbicara dengan

        @SerializedName("TUJUAN")
        @Expose
        private String tujuan;                      // Mandatory tujuan

        @SerializedName("RESULTDATE")
        @Expose
        private String resultDate;                  // Mandatory resultDate = tanggal janji bayar

        @SerializedName("SPA")
        @Expose
        private String spa;

        @SerializedName("HOUSEPHONESTA")
        @Expose
        private String housePhoneSta;

        @SerializedName("HOUSEPHONEAREA")
        @Expose
        private String housePhoneArea;

        @SerializedName("HOUSEPHONENEW")
        @Expose
        private String housePhoneNew;

        @SerializedName("OFFICEPHONESTA")
        @Expose
        private String officePhoneSta;

        @SerializedName("OFFICEPHONEAREA")
        @Expose
        private String officePhoneArea;

        @SerializedName("OFFICEPHONENEW")
        @Expose
        private String officePhoneNew;

        @SerializedName("HPSTA")
        @Expose
        private String hpSta;

        @SerializedName("HPNEW")
        @Expose
        private String hpNew;

        @SerializedName("CORRESPADDRSTA")
        @Expose
        private String corresPaddSta;

        @SerializedName("CORRESPADDRNEW")
        @Expose
        private String corresPaddNew;

        @SerializedName("CORRESPADDRNEW2")
        @Expose
        private String corresPaddNew2;

        @SerializedName("CORRESPCITY")
        @Expose
        private String corresPCity;

        @SerializedName("CORRESPZIPCODE")
        @Expose
        private String corresPZipCode;

        @SerializedName("HOUSEADDRSTA")
        @Expose
        private String houseAddrSta;

        @SerializedName("HOUSEADDRNEW")
        @Expose
        private String houseAddrNew;

        @SerializedName("HOUSEADDRNEW2")
        @Expose
        private String houseAddrNew2;

        @SerializedName("HOUSECITY")
        @Expose
        private String houseCity;

        @SerializedName("HOUSEZIPCODE")
        @Expose
        private String houseZipCode;

        @SerializedName("OFFICEADDRSTA")
        @Expose
        private String officeAddrSta;

        @SerializedName("OFFICEADDRNEW")
        @Expose
        private String officeAddrNew;

        @SerializedName("NOTES")
        @Expose
        private String notes;                               // Mandatory notes = catatan

        @SerializedName("LETTER_COND")
        @Expose
        private String letterCond;

        @SerializedName("REASON_NON_PAYMENT_DESC")
        @Expose
        private String reasonNonPaymentDesc;

        @SerializedName("KEMAMPUAN_DEBITUR")
        @Expose
        private double kemampuanDebitur;

        @SerializedName("PTP_AMOUNT")
        @Expose
        private double ptpAmount;                              // Mandatory ptpAmount = jumlah yang akan  disetor

        @SerializedName("PAYMENT_TYPE")
        @Expose
        private String paymentType;

        @SerializedName("PTP_PERSON")
        @Expose
        private String ptpPerson;

        @SerializedName("TeleRequestID")
        @Expose
        private String teleRequestId;


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

        public String getTujuan() {
            return tujuan;
        }

        public void setTujuan(String tujuan) {
            this.tujuan = tujuan;
        }

        public String getResultDate() {
            return resultDate;
        }

        public void setResultDate(String resultDate) {
            this.resultDate = resultDate;
        }

        public String getSpa() {
            return spa;
        }

        public void setSpa(String spa) {
            this.spa = spa;
        }

        public String getHousePhoneSta() {
            return housePhoneSta;
        }

        public void setHousePhoneSta(String housePhoneSta) {
            this.housePhoneSta = housePhoneSta;
        }

        public String getHousePhoneArea() {
            return housePhoneArea;
        }

        public void setHousePhoneArea(String housePhoneArea) {
            this.housePhoneArea = housePhoneArea;
        }

        public String getHousePhoneNew() {
            return housePhoneNew;
        }

        public void setHousePhoneNew(String housePhoneNew) {
            this.housePhoneNew = housePhoneNew;
        }

        public String getOfficePhoneSta() {
            return officePhoneSta;
        }

        public void setOfficePhoneSta(String officePhoneSta) {
            this.officePhoneSta = officePhoneSta;
        }

        public String getOfficePhoneArea() {
            return officePhoneArea;
        }

        public void setOfficePhoneArea(String officePhoneArea) {
            this.officePhoneArea = officePhoneArea;
        }

        public String getOfficePhoneNew() {
            return officePhoneNew;
        }

        public void setOfficePhoneNew(String officePhoneNew) {
            this.officePhoneNew = officePhoneNew;
        }

        public String getHpSta() {
            return hpSta;
        }

        public void setHpSta(String hpSta) {
            this.hpSta = hpSta;
        }

        public String getHpNew() {
            return hpNew;
        }

        public void setHpNew(String hpNew) {
            this.hpNew = hpNew;
        }

        public String getCorresPaddSta() {
            return corresPaddSta;
        }

        public void setCorresPaddSta(String corresPaddSta) {
            this.corresPaddSta = corresPaddSta;
        }

        public String getCorresPaddNew() {
            return corresPaddNew;
        }

        public void setCorresPaddNew(String corresPaddNew) {
            this.corresPaddNew = corresPaddNew;
        }

        public String getCorresPaddNew2() {
            return corresPaddNew2;
        }

        public void setCorresPaddNew2(String corresPaddNew2) {
            this.corresPaddNew2 = corresPaddNew2;
        }

        public String getCorresPCity() {
            return corresPCity;
        }

        public void setCorresPCity(String corresPCity) {
            this.corresPCity = corresPCity;
        }

        public String getCorresPZipCode() {
            return corresPZipCode;
        }

        public void setCorresPZipCode(String corresPZipCode) {
            this.corresPZipCode = corresPZipCode;
        }

        public String getHouseAddrSta() {
            return houseAddrSta;
        }

        public void setHouseAddrSta(String houseAddrSta) {
            this.houseAddrSta = houseAddrSta;
        }

        public String getHouseAddrNew() {
            return houseAddrNew;
        }

        public void setHouseAddrNew(String houseAddrNew) {
            this.houseAddrNew = houseAddrNew;
        }

        public String getHouseAddrNew2() {
            return houseAddrNew2;
        }

        public void setHouseAddrNew2(String houseAddrNew2) {
            this.houseAddrNew2 = houseAddrNew2;
        }

        public String getHouseCity() {
            return houseCity;
        }

        public void setHouseCity(String houseCity) {
            this.houseCity = houseCity;
        }

        public String getHouseZipCode() {
            return houseZipCode;
        }

        public void setHouseZipCode(String houseZipCode) {
            this.houseZipCode = houseZipCode;
        }

        public String getOfficeAddrSta() {
            return officeAddrSta;
        }

        public void setOfficeAddrSta(String officeAddrSta) {
            this.officeAddrSta = officeAddrSta;
        }

        public String getOfficeAddrNew() {
            return officeAddrNew;
        }

        public void setOfficeAddrNew(String officeAddrNew) {
            this.officeAddrNew = officeAddrNew;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getLetterCond() {
            return letterCond;
        }

        public void setLetterCond(String letterCond) {
            this.letterCond = letterCond;
        }

        public String getReasonNonPaymentDesc() {
            return reasonNonPaymentDesc;
        }

        public void setReasonNonPaymentDesc(String reasonNonPaymentDesc) {
            this.reasonNonPaymentDesc = reasonNonPaymentDesc;
        }

        public double getKemampuanDebitur() {
            return kemampuanDebitur;
        }

        public void setKemampuanDebitur(double kemampuanDebitur) {
            this.kemampuanDebitur = kemampuanDebitur;
        }

        public double getPtpAmount() {
            return ptpAmount;
        }

        public void setPtpAmount(double ptpAmount) {
            this.ptpAmount = ptpAmount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getPtpPerson() {
            return ptpPerson;
        }

        public void setPtpPerson(String ptpPerson) {
            this.ptpPerson = ptpPerson;
        }

        public String getTeleRequestId() {
            return teleRequestId;
        }

        public void setTeleRequestId(String teleRequestId) {
            this.teleRequestId = teleRequestId;
        }

    }
}
