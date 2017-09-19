package com.mitkoindo.smartcollection.objectdata.databasemodel;

import com.mitkoindo.smartcollection.network.body.FormVisitBody;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/8/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class SpParameterFormVisitDb extends RealmObject {

    private String userId;                  // Mandatory userId = userId

    @PrimaryKey
    private String accNo;                   // Mandatory accNo = nomor rekening

    private String personVisitRel;          // Mandatory personVisitRel = hubungan orang yang dikunjungi

    private String personVisit;             // Mandatory personVisit = nama orang yang dikunjungi

    private String notes;                   // Mandatory notes = catatan

    private String cuAddr;                  // Mandatory cuAddr = alamat

    private String tujuan;                  // Mandatory tujuan = tujuan kunjungan

    private String nextAction;              // Mandatory nextAction = tindak lanjut

    private String nextActionDate;          // Mandatory nextActionDate = tanggal tindak lanjut

    private String collStatDesc;            // Mandatory collStatDesc = status agunan

    private String collCondDesc;            // Mandatory collCondDesc = kondisi agunan

    private String reasonNonPayment;        // Mandatory reasonNonPayment = alasan tidak bayar

    private String reasonNonPaymentDesc;

    private String result;                  // Mandatory result = hasil kunjungan

    private String resultDate;              // Mandatory resultDate = tanggal janji debitur

    private double ptpAmount;               // Mandatory ptpAmount = jumlah yang akan disetor

    private double geoLatitude;

    private double geoLongitude;

    private String geoAddress;

    private String photoDebiturPath;
    private String photoAgunan1Path;
    private String photoAgunan2Path;
    private String photoSignaturePath;
    private String photoDebitur;
    private String photoAgunan1;
    private String photoAgunan2;
    private String photoSignature;


    public SpParameterFormVisitDb() {
    }

    public SpParameterFormVisitDb(FormVisitBody.SpParameter spParameter) {
        userId = spParameter.getUserId();
        accNo = spParameter.getAccNo();
        personVisitRel = spParameter.getPersonVisitRel();
        personVisit = spParameter.getPersonVisit();
        notes = spParameter.getNotes();
        cuAddr = spParameter.getCuAddr();
        tujuan = spParameter.getTujuan();
        nextAction = spParameter.getNextAction();
        nextActionDate = spParameter.getNextActionDate();
        collStatDesc = spParameter.getCollStatDesc();
        collCondDesc = spParameter.getCollCondDesc();
        reasonNonPayment = spParameter.getReasonNonPayment();
        reasonNonPaymentDesc = spParameter.getReasonNonPaymentDesc();
        result = spParameter.getResult();
        resultDate = spParameter.getResultDate();
        ptpAmount = spParameter.getPtpAmount();
        geoLatitude = spParameter.getGeoLatitude();
        geoLongitude = spParameter.getGeoLongitude();
        geoAddress = spParameter.getGeoAddress();
        photoDebitur = spParameter.getPhotoDebitur();
        photoAgunan1 = spParameter.getPhotoAgunan1();
        photoAgunan2 = spParameter.getPhotoAgunan2();
        photoSignature = spParameter.getPhotoSignature();
    }

    public FormVisitBody.SpParameter toSpParameterFormVisit() {
        FormVisitBody.SpParameter spParameter = new FormVisitBody.SpParameter();
        spParameter.setUserId(userId);
        spParameter.setAccNo(accNo);
        spParameter.setPersonVisitRel(personVisitRel);
        spParameter.setPersonVisit(personVisit);
        spParameter.setNotes(notes);
        spParameter.setCuAddr(cuAddr);
        spParameter.setTujuan(tujuan);
        spParameter.setNextAction(nextAction);
        spParameter.setNextActionDate(nextActionDate);
        spParameter.setCollStatDesc(collStatDesc);
        spParameter.setCollCondDesc(collCondDesc);
        spParameter.setReasonNonPayment(reasonNonPayment);
        spParameter.setReasonNonPaymentDesc(reasonNonPaymentDesc);
        spParameter.setResult(result);
        spParameter.setResultDate(resultDate);
        spParameter.setPtpAmount(ptpAmount);
        spParameter.setGeoLatitude(geoLatitude);
        spParameter.setGeoLongitude(geoLongitude);
        spParameter.setGeoAddress(geoAddress);
        spParameter.setPhotoDebitur(photoDebitur);
        spParameter.setPhotoAgunan1(photoAgunan1);
        spParameter.setPhotoAgunan2(photoAgunan2);
        spParameter.setPhotoSignature(photoSignature);

        return spParameter;
    }

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

    public String getPhotoDebiturPath() {
        return photoDebiturPath;
    }

    public void setPhotoDebiturPath(String photoDebiturPath) {
        this.photoDebiturPath = photoDebiturPath;
    }

    public String getPhotoAgunan1Path() {
        return photoAgunan1Path;
    }

    public void setPhotoAgunan1Path(String photoAgunan1Path) {
        this.photoAgunan1Path = photoAgunan1Path;
    }

    public String getPhotoAgunan2Path() {
        return photoAgunan2Path;
    }

    public void setPhotoAgunan2Path(String photoAgunan2Path) {
        this.photoAgunan2Path = photoAgunan2Path;
    }

    public String getPhotoSignaturePath() {
        return photoSignaturePath;
    }

    public void setSignaturePath(String photoSignaturePath) {
        this.photoSignaturePath = photoSignaturePath;
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
