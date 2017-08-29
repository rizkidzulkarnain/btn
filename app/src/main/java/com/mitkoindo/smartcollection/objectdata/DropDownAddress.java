package com.mitkoindo.smartcollection.objectdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class DropDownAddress extends RealmObject {

    @PrimaryKey
    @SerializedName("RowNumber")
    @Expose
    private Integer rowNumber;
    @SerializedName("ACC_NO")
    @Expose
    private String accNo;
    @SerializedName("AT_DESC")
    @Expose
    private String atDesc;
    @SerializedName("Alamat")
    @Expose
    private String alamat;
    @SerializedName("CU_REF")
    @Expose
    private String cuRef;
    @SerializedName("CA_ID")
    @Expose
    private Integer caId;
    @SerializedName("CA_CONTACTNAME")
    @Expose
    private String caContactName;
    @SerializedName("CA_RELATIONSHIP")
    @Expose
    private String caRelationship;
    @SerializedName("CA_ADDRTYPE")
    @Expose
    private String caAddrType;
    @SerializedName("CA_ADDRNAME")
    @Expose
    private String caAddrName;
    @SerializedName("CA_ADDR1")
    @Expose
    private String caAddr1;
    @SerializedName("CA_ADDR2")
    @Expose
    private String caAddr2;
    @SerializedName("CA_ADDR3")
    @Expose
    private String caAddr3;
    @SerializedName("CA_RT")
    @Expose
    private String caRt;
    @SerializedName("CA_RW")
    @Expose
    private String caRw;
    @SerializedName("CA_KEL")
    @Expose
    private String caKel;
    @SerializedName("CA_KEC")
    @Expose
    private String caKec;
    @SerializedName("CA_CITY")
    @Expose
    private String caCity;
    @SerializedName("CA_PROVINSI")
    @Expose
    private String caProvinsi;
    @SerializedName("CA_ZIPCODE")
    @Expose
    private String caZipCode;
    @SerializedName("CA_BI_CITY_CODE")
    @Expose
    private String caBiCityCode;
    @SerializedName("CA_ADDR_INVALID")
    @Expose
    private String caAddrInvalid;
    @SerializedName("CA_PHNAREA")
    @Expose
    private String caPhnArea;
    @SerializedName("CA_PHNNUM")
    @Expose
    private String caPhnNum;
    @SerializedName("CA_PHNEXT")
    @Expose
    private String caPhNext;
    @SerializedName("CA_PHNNUM_INVALID")
    @Expose
    private String caPhnNumInvalid;
    @SerializedName("CA_HPPHNNUM")
    @Expose
    private String caHpPhnNum;
    @SerializedName("CA_HPPHNNUM_INVALID")
    @Expose
    private String caHpPhnNumInvalid;
    @SerializedName("CA_FAXAREA")
    @Expose
    private String caFaxArea;
    @SerializedName("CA_FAXNUM")
    @Expose
    private String caFaxNum;
    @SerializedName("CA_FAXEXT")
    @Expose
    private String caFaxExt;
    @SerializedName("CA_FAXNUM_INVALID")
    @Expose
    private String caFaxNumInvalid;
    @SerializedName("CA_NOTE")
    @Expose
    private String caNote;
    @SerializedName("CA_PRIORITY")
    @Expose
    private Integer caPriority;
    @SerializedName("CA_BAGIAN")
    @Expose
    private String caBagian;

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAtDesc() {
        return atDesc;
    }

    public void setAtDesc(String atDesc) {
        this.atDesc = atDesc;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCuRed() {
        return cuRef;
    }

    public void setCuRef(String cuRef) {
        this.cuRef = cuRef;
    }

    public Integer getCaId() {
        return caId;
    }

    public void setCaId(Integer caId) {
        this.caId = caId;
    }

    public String getCaContactName() {
        return caContactName;
    }

    public void setCaContactNameString (String caContactName) {
        this.caContactName = caContactName;
    }

    public String getCaRelationship() {
        return caRelationship;
    }

    public void setCaRelationship(String caRelationship) {
        this.caRelationship = caRelationship;
    }

    public String getCaAddrType() {
        return caAddrType;
    }

    public void setCaAddrType(String caAddrType) {
        this.caAddrType = caAddrType;
    }

    public String getCaAddrName() {
        return caAddrName;
    }

    public void setCaAddrName(String caAddrName) {
        this.caAddrName = caAddrName;
    }

    public String getCaAddr1() {
        return caAddr1;
    }

    public void setCaAddr1(String caAddr1) {
        this.caAddr1 = caAddr1;
    }

    public String getCaAddr2() {
        return caAddr2;
    }

    public void setCaAddr2(String caAddr2) {
        this.caAddr2 = caAddr2;
    }

    public String getCaAddr3() {
        return caAddr3;
    }

    public void setCaAddr3(String caAddr3) {
        this.caAddr3 = caAddr3;
    }

    public String getCaRt() {
        return caRt;
    }

    public void setCaRt(String caRt) {
        this.caRt = caRt;
    }

    public String getCaRw() {
        return caRw;
    }

    public void setCaRw(String caRw) {
        this.caRw = caRw;
    }

    public String getCaKel() {
        return caKel;
    }

    public void setCaKel(String caKel) {
        this.caKel = caKel;
    }

    public String getCaKec() {
        return caKec;
    }

    public void setCaKec(String caKec) {
        this.caKec = caKec;
    }

    public String getCaCity() {
        return caCity;
    }

    public void setCaCity(String caCity) {
        this.caCity = caCity;
    }

    public String getCaProvinsi() {
        return caProvinsi;
    }

    public void setCaProvinsi(String caProvinsi) {
        this.caProvinsi = caProvinsi;
    }

    public String getCaZipCode() {
        return caZipCode;
    }

    public void setCaZipCode(String caZipCode) {
        this.caZipCode = caZipCode;
    }

    public String getCaBiCityCode() {
        return caBiCityCode;
    }

    public void setCaBiCityCode(String caBiCityCode) {
        this.caBiCityCode = caBiCityCode;
    }

    public String getCaAddrInvalid() {
        return caAddrInvalid;
    }

    public void setCaAddrInvalid(String caAddrInvalid) {
        this.caAddrInvalid = caAddrInvalid;
    }

    public String getCaPpnArea() {
        return caPhnArea;
    }

    public void setCaPhnArea(String caPhnArea) {
        this.caPhnArea = caPhnArea;
    }

    public String getCaPhnNum() {
        return caPhnNum;
    }

    public void setCaPhnNum(String caPhnNum) {
        this.caPhnNum = caPhnNum;
    }

    public String getCaPhnExt() {
        return caPhNext;
    }

    public void setCaPhnExt(String caPhNext) {
        this.caPhNext = caPhNext;
    }

    public String getCaPhnNumInvalid() {
        return caPhnNumInvalid;
    }

    public void setCaPhnNumInvalid(String caPhnNumInvalid) {
        this.caPhnNumInvalid = caPhnNumInvalid;
    }

    public String getCaHpPhnNum() {
        return caHpPhnNum;
    }

    public void setCaHpPhnNum(String caHpPhnNum) {
        this.caHpPhnNum = caHpPhnNum;
    }

    public String getCaHpPhnNumInvalid() {
        return caHpPhnNumInvalid;
    }

    public void setCaHpPhnNumInvalid(String caHpPhnNumInvalid) {
        this.caHpPhnNumInvalid = caHpPhnNumInvalid;
    }

    public String getCaFaxArea() {
        return caFaxArea;
    }

    public void setCaFaxArea(String caFaxArea) {
        this.caFaxArea = caFaxArea;
    }

    public String getCaFaxNum() {
        return caFaxNum;
    }

    public void setCaFaxNum(String caFaxNum) {
        this.caFaxNum = caFaxNum;
    }

    public String getCaFaxExt() {
        return caFaxExt;
    }

    public void setCaFaxExt(String caFaxExt) {
        this.caFaxExt = caFaxExt;
    }

    public String getCaFaxNumInvalid() {
        return caFaxNumInvalid;
    }

    public void setCaFaxNumInvalid(String caFaxNumInvalid) {
        this.caFaxNumInvalid = caFaxNumInvalid;
    }

    public String getCaNote() {
        return caNote;
    }

    public void setCaNote(String caNote) {
        this.caNote = caNote;
    }

    public Integer getCaPrioriy() {
        return caPriority;
    }

    public void setCaPriority(Integer caPriority) {
        this.caPriority = caPriority;
    }

    public String getCaBagian() {
        return caBagian;
    }

    public void setCaBagian(String caBagian) {
        this.caBagian = caBagian;
    }
}
