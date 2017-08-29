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
        @SerializedName("COLLECTOR_OFFR")
        @Expose
        private String collectorOffr;
        @SerializedName("PERSON_VISIT_REL")
        @Expose
        private String personVisitRel;          // Mandatory personVisitRel = hubungan orang yang dikunjungi
        @SerializedName("NOTES")
        @Expose
        private String notes;                   // Mandatory notes = catatan
        @SerializedName("EXPIRY_DATE")
        @Expose
        private String expirydate;
        @SerializedName("CU_ADDR")
        @Expose
        private String cuAddr;                  // Mandatory cuAddr = alamat
        @SerializedName("RESULT")
        @Expose
        private String result;                  // Mandatory result = hasil kunjungan
        @SerializedName("tujuan")
        @Expose
        private String tujuan;                  // Mandatory tujuan = tujuan kunjungan
        @SerializedName("RESULTDATE")
        @Expose
        private String resultDate;              // Mandatory resultDate = tanggal janji debitur
        @SerializedName("LossEvent")
        @Expose
        private String lossEvent;
        @SerializedName("NEXT_ACTION")
        @Expose
        private String nextAction;              // Mandatory nextAction = tindak lanjut
        @SerializedName("NEXT_ACTION_DATE")
        @Expose
        private String nextActionDate;          // Mandatory nextActionDate = tanggal tindak lanjut
        @SerializedName("KEMAMPUAN_DEBITUR")
        @Expose
        private double kemampuanDebitur;
        @SerializedName("spa")
        @Expose
        private String spa;
        @SerializedName("HOUSEPHONEDESC")
        @Expose
        private String housePhoneDesc;
        @SerializedName("HOUSEPHONENEW")
        @Expose
        private String housePhoneNew;
        @SerializedName("OFFICEPHONEDESC")
        @Expose
        private String officePhoneDesc;
        @SerializedName("OFFICEPHONENEW")
        @Expose
        private String officePhoneNew;
        @SerializedName("HPDESC")
        @Expose
        private String hpDesc;
        @SerializedName("HPNEW")
        @Expose
        private String hpNew;
        @SerializedName("CORRESPADDRDESC")
        @Expose
        private String corresspAddressC;
        @SerializedName("CORRESPADDRNEW")
        @Expose
        private String correspAddrNew;
        @SerializedName("CORRESPADDRNEW2")
        @Expose
        private String correspAddrNew2;
        @SerializedName("CORRESPADDRNEW3")
        @Expose
        private String correspAddrNew3;
        @SerializedName("CORRESPCITY")
        @Expose
        private String corresspCity;
        @SerializedName("CORRESPZIPCODE")
        @Expose
        private String corresspZipCode;
        @SerializedName("HOUSEADDRDESC")
        @Expose
        private String houseAddrDesc;
        @SerializedName("HOUSEADDRNEW")
        @Expose
        private String houseAddrNew;
        @SerializedName("HOUSEADDRNEW2")
        @Expose
        private String houseAddrDesc2;
        @SerializedName("HOUSEADDRNEW3")
        @Expose
        private String houseAddrDesc3;
        @SerializedName("HOUSEADDRCITY")
        @Expose
        private String houseAddrCity;
        @SerializedName("HOUSEZIPCODE")
        @Expose
        private String houseAddrZipCode;
        @SerializedName("OFFICEADDRDESC")
        @Expose
        private String officeAddrDesc;
        @SerializedName("OFFICEADDRNEW")
        @Expose
        private String officeAddrNew;
        @SerializedName("COLLSTATDESC")
        @Expose
        private String collStatDesc;            // Mandatory collStatDesc = status agunan
        @SerializedName("COLLSTATNEW")
        @Expose
        private String collStatNew;
        @SerializedName("COLLCONDDESC")
        @Expose
        private String collCondDesc;            // Mandatory collCondDesc = kondisi agunan
        @SerializedName("COLLCONDNEW")
        @Expose
        private String collCondNew;
        @SerializedName("PERSON_VISIT")
        @Expose
        private String personVisit;             // Mandatory personVisit = nama orang yang dikunjungi
        @SerializedName("LETTER_COND")
        @Expose
        private String letterCond;
        @SerializedName("REASON_NON_PAYMENT")
        @Expose
        private String reasonNonPayment;        // Mandatory reasonNonPayment = alasan tidak bayar
        @SerializedName("REASON_NON_PAYMENT_DESC")
        @Expose
        private String reasonNonPaymentDesc;
        @SerializedName("PTPAMOUNT")
        @Expose
        private double ptpAmount;              // Mandatory ptpAmount = jumlah yang akan disetor

        private String photoDebiturPath;
        private String photoAgunan1Path;
        private String photoAgunan2Path;
        private String signaturePath;
        private String photoDebitur;
        private String photoAgunan1;
        private String photoAgunan2;
        private String signature;

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

        public String getCollectorOffr() {
            return collectorOffr;
        }

        public void setCollectorOffr(String collectorOffr) {
            this.collectorOffr = collectorOffr;
        }

        public String getPersonVisitRel() {
            return personVisitRel;
        }

        public void setPersonVisitRel(String personVisitRel) {
            this.personVisitRel = personVisitRel;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getExpirydate() {
            return expirydate;
        }

        public void setExpirydate(String expirydate) {
            this.expirydate = expirydate;
        }

        public String getCuAddr() {
            return cuAddr;
        }

        public void setCuAddr(String cuAddr) {
            this.cuAddr = cuAddr;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
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

        public String getLossEvent() {
            return lossEvent;
        }

        public void setLossEvent(String lossEvent) {
            this.lossEvent = lossEvent;
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

        public double getKemampuanDebitur() {
            return kemampuanDebitur;
        }

        public void setKemampuanDebitur(double kemampuanDebitur) {
            this.kemampuanDebitur = kemampuanDebitur;
        }

        public String getSpa() {
            return spa;
        }

        public void setSpa(String spa) {
            this.spa = spa;
        }

        public String getHousePhoneDesc() {
            return housePhoneDesc;
        }

        public void setHousePhoneDesc(String housePhoneDesc) {
            this.housePhoneDesc = housePhoneDesc;
        }

        public String getHousePhoneNew() {
            return housePhoneNew;
        }

        public void setHousePhoneNew(String housePhoneNew) {
            this.housePhoneNew = housePhoneNew;
        }

        public String getOfficePhoneDesc() {
            return officePhoneDesc;
        }

        public void setOfficePhoneDesc(String officePhoneDesc) {
            this.officePhoneDesc = officePhoneDesc;
        }

        public String getOfficePhoneNew() {
            return officePhoneNew;
        }

        public void setOfficePhoneNew(String officePhoneNew) {
            this.officePhoneNew = officePhoneNew;
        }

        public String getHpDesc() {
            return hpDesc;
        }

        public void setHpDesc(String hpDesc) {
            this.hpDesc = hpDesc;
        }

        public String getHpNew() {
            return hpNew;
        }

        public void setHpNew(String hpNew) {
            this.hpNew = hpNew;
        }

        public String getCorresspAddressC() {
            return corresspAddressC;
        }

        public void setCorresspAddressC(String corresspAddressC) {
            this.corresspAddressC = corresspAddressC;
        }

        public String getCorrespAddrNew() {
            return correspAddrNew;
        }

        public void setCorrespAddrNew(String correspAddrNew) {
            this.correspAddrNew = correspAddrNew;
        }

        public String getCorrespAddrNew2() {
            return correspAddrNew2;
        }

        public void setCorrespAddrNew2(String correspAddrNew2) {
            this.correspAddrNew2 = correspAddrNew2;
        }

        public String getCorrespAddrNew3() {
            return correspAddrNew3;
        }

        public void setCorrespAddrNew3(String correspAddrNew3) {
            this.correspAddrNew3 = correspAddrNew3;
        }

        public String getCorresspCity() {
            return corresspCity;
        }

        public void setCorresspCity(String corresspCity) {
            this.corresspCity = corresspCity;
        }

        public String getCorresspZipCode() {
            return corresspZipCode;
        }

        public void setCorresspZipCode(String corresspZipCode) {
            this.corresspZipCode = corresspZipCode;
        }

        public String getHouseAddrDesc() {
            return houseAddrDesc;
        }

        public void setHouseAddrDesc(String houseAddrDesc) {
            this.houseAddrDesc = houseAddrDesc;
        }

        public String getHouseAddrNew() {
            return houseAddrNew;
        }

        public void setHouseAddrNew(String houseAddrNew) {
            this.houseAddrNew = houseAddrNew;
        }

        public String getHouseAddrDesc2() {
            return houseAddrDesc2;
        }

        public void setHouseAddrDesc2(String houseAddrDesc2) {
            this.houseAddrDesc2 = houseAddrDesc2;
        }

        public String getHouseAddrDesc3() {
            return houseAddrDesc3;
        }

        public void setHouseAddrDesc3(String houseAddrDesc3) {
            this.houseAddrDesc3 = houseAddrDesc3;
        }

        public String getHouseAddrCity() {
            return houseAddrCity;
        }

        public void setHouseAddrCity(String houseAddrCity) {
            this.houseAddrCity = houseAddrCity;
        }

        public String getHouseAddrZipCode() {
            return houseAddrZipCode;
        }

        public void setHouseAddrZipCode(String houseAddrZipCode) {
            this.houseAddrZipCode = houseAddrZipCode;
        }

        public String getOfficeAddrDesc() {
            return officeAddrDesc;
        }

        public void setOfficeAddrDesc(String officeAddrDesc) {
            this.officeAddrDesc = officeAddrDesc;
        }

        public String getOfficeAddrNew() {
            return officeAddrNew;
        }

        public void setOfficeAddrNew(String officeAddrNew) {
            this.officeAddrNew = officeAddrNew;
        }

        public String getCollStatDesc() {
            return collStatDesc;
        }

        public void setCollStatDesc(String collStatDesc) {
            this.collStatDesc = collStatDesc;
        }

        public String getCollStatNew() {
            return collStatNew;
        }

        public void setCollStatNew(String collStatNew) {
            this.collStatNew = collStatNew;
        }

        public String getCollCondDesc() {
            return collCondDesc;
        }

        public void setCollCondDesc(String collCondDesc) {
            this.collCondDesc = collCondDesc;
        }

        public String getCollCondNew() {
            return collCondNew;
        }

        public void setCollCondNew(String collCondNew) {
            this.collCondNew = collCondNew;
        }

        public String getPersonVisit() {
            return personVisit;
        }

        public void setPersonVisit(String personVisit) {
            this.personVisit = personVisit;
        }

        public String getLetterCond() {
            return letterCond;
        }

        public void setLetterCond(String letterCond) {
            this.letterCond = letterCond;
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

        public double getPtpAmount() {
            return ptpAmount;
        }

        public void setPtpAmount(double ptpAmount) {
            this.ptpAmount = ptpAmount;
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

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSignaturePath() {
            return signaturePath;
        }

        public void setSignaturePath(String signaturePath) {
            this.signaturePath = signaturePath;
        }
    }
}
