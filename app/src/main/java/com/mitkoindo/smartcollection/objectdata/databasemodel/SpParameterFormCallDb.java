package com.mitkoindo.smartcollection.objectdata.databasemodel;


import com.mitkoindo.smartcollection.network.body.FormCallBody;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/7/17.
 */

public class SpParameterFormCallDb extends RealmObject {

    @PrimaryKey
    private String accountNo;                   // Mandatory accountNo = nomor rekening

    private String userId;                      // Mandatory userId

    private String tujuan;                      // Mandatory tujuan

    private String contactNo;

    private String relationship;                // Mandatory relationship = hubungan dengan debitur

    private String spokeTo;                     // Mandatory spokeTo = berbicara dengan

    private String notes;                               // Mandatory notes = catatan

    private String result;                      // Mandatory result = hasil panggilan

    private String reasonNoPayment;             // Mandatory reasonNoPayment = alasan menunggak

    private String reasonNonPaymentDesc;

    private String dateAction;                  // Mandatory dateAction = tanggal tindak lanjut

    private String nextAction;                  // Mandatory nextAction = tindak lanjut

    private String resultDate;                  // Mandatory resultDate = tanggal janji bayar

    private double ptpAmount;                   // Mandatory ptpAmount = jumlah yang akan  disetor

    private double geoLatitude;

    private double geoLongitude;

    private String geoAddress;


    public SpParameterFormCallDb() {
    }

    public SpParameterFormCallDb(FormCallBody.SpParameterFormCall spParameterFormCall) {
        accountNo = spParameterFormCall.getAccountNo();
        userId = spParameterFormCall.getUserId();
        tujuan = spParameterFormCall.getTujuan();
        contactNo = spParameterFormCall.getContactNo();
        relationship = spParameterFormCall.getRelationship();
        spokeTo = spParameterFormCall.getSpokeTo();
        notes = spParameterFormCall.getNotes();
        result = spParameterFormCall.getResult();
        reasonNoPayment = spParameterFormCall.getReasonNoPayment();
        reasonNonPaymentDesc = spParameterFormCall.getReasonNonPaymentDesc();
        dateAction = spParameterFormCall.getDateAction();
        nextAction = spParameterFormCall.getNextAction();
        resultDate = spParameterFormCall.getResultDate();
        ptpAmount = spParameterFormCall.getPtpAmount();
        geoLatitude = spParameterFormCall.getGeoLatitude();
        geoLongitude = spParameterFormCall.getGeoLongitude();
        geoAddress = spParameterFormCall.getGeoAddress();
    }

    public FormCallBody.SpParameterFormCall toSpParameterFormCall() {
        FormCallBody.SpParameterFormCall spParameterFormCall = new FormCallBody.SpParameterFormCall();
        spParameterFormCall.setAccountNo(accountNo);
        spParameterFormCall.setUserId(userId);
        spParameterFormCall.setTujuan(tujuan);
        spParameterFormCall.setContactNo(contactNo);
        spParameterFormCall.setRelationship(relationship);
        spParameterFormCall.setSpokeTo(spokeTo);
        spParameterFormCall.setNotes(notes);
        spParameterFormCall.setResult(result);
        spParameterFormCall.setReasonNoPayment(reasonNoPayment);
        spParameterFormCall.setReasonNonPaymentDesc(reasonNonPaymentDesc);
        spParameterFormCall.setDateAction(dateAction);
        spParameterFormCall.setNextAction(nextAction);
        spParameterFormCall.setResultDate(resultDate);
        spParameterFormCall.setPtpAmount(ptpAmount);
        spParameterFormCall.setGeoLatitude(geoLatitude);
        spParameterFormCall.setGeoLongitude(geoLongitude);
        spParameterFormCall.setGeoAddress(geoAddress);

        return spParameterFormCall;
    }

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
