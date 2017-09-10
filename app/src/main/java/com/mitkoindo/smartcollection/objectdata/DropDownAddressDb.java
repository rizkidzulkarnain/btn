package com.mitkoindo.smartcollection.objectdata;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class DropDownAddressDb extends RealmObject {

    @PrimaryKey
    private String id;

    private int rowNumber;

    private String accNo;

    private String atDesc;

    private String alamat;

    private String cuRef;

    private int caId;

    private String caContactName;

    private String caRelationship;

    private String caAddrType;

    private String caAddrName;

    private String caAddr1;

    private String caAddr2;

    private String caAddr3;

    private String caRt;

    private String caRw;

    private String caKel;

    private String caKec;

    private String caCity;

    private String caProvinsi;

    private String caZipCode;

    private String caBiCityCode;

    private String caAddrInvalid;

    private String caPhnArea;

    private String caPhnNum;

    private String caPhNext;

    private String caPhnNumInvalid;

    private String caHpPhnNum;

    private String caHpPhnNumInvalid;

    private String caFaxArea;

    private String caFaxNum;

    private String caFaxExt;

    private String caFaxNumInvalid;

    private String caNote;

    private int caPriority;

    private String caBagian;


    public DropDownAddressDb() {
    }

    public DropDownAddressDb(DropDownAddress dropDownAddress) {
        id = UUID.randomUUID().toString();
        rowNumber = dropDownAddress.getRowNumber();
        accNo = dropDownAddress.getAccNo();
        atDesc = dropDownAddress.getAtDesc();
        alamat = dropDownAddress.getAlamat();
        cuRef = dropDownAddress.getCuRef();
        caId = dropDownAddress.getCaId();
        caContactName = dropDownAddress.getCaContactName();
        caRelationship = dropDownAddress.getCaRelationship();
        caAddrType = dropDownAddress.getCaAddrType();
        caAddrName = dropDownAddress.getCaAddrName();
        caAddr1 = dropDownAddress.getCaAddr1();
        caAddr2 = dropDownAddress.getCaAddr2();
        caAddr3 = dropDownAddress.getCaAddr3();
        caRt = dropDownAddress.getCaRt();
        caRw = dropDownAddress.getCaRw();
        caKel = dropDownAddress.getCaKel();
        caKec = dropDownAddress.getCaKec();
        caCity = dropDownAddress.getCaCity();
        caProvinsi = dropDownAddress.getCaProvinsi();
        caZipCode = dropDownAddress.getCaZipCode();
        caBiCityCode = dropDownAddress.getCaBiCityCode();
        caAddrInvalid = dropDownAddress.getCaAddrInvalid();
        caPhnArea = dropDownAddress.getCaPpnArea();
        caPhnNum = dropDownAddress.getCaPhnNum();
        caPhNext = dropDownAddress.getCaPhnExt();
        caPhnNumInvalid = dropDownAddress.getCaPhnNumInvalid();
        caFaxArea = dropDownAddress.getCaFaxArea();
        caFaxNum = dropDownAddress.getCaFaxNum();
        caFaxExt = dropDownAddress.getCaFaxExt();
        caFaxNumInvalid = dropDownAddress.getCaFaxNumInvalid();
        caNote = dropDownAddress.getCaNote();
        caPriority = dropDownAddress.getCaPrioriy();
        caBagian = dropDownAddress.getCaBagian();
    }

    public DropDownAddress toDropDownAddress() {
        DropDownAddress dropDownAddress = new DropDownAddress();
        dropDownAddress.setRowNumber(rowNumber);
        dropDownAddress.setAccNo(accNo);
        dropDownAddress.setAtDesc(atDesc);
        dropDownAddress.setAlamat(alamat);
        dropDownAddress.setCuRef(cuRef);
        dropDownAddress.setCaId(caId);
        dropDownAddress.setCaContactNameString(caContactName);
        dropDownAddress.setCaRelationship(caRelationship);
        dropDownAddress.setCaAddrType(caAddrType);
        dropDownAddress.setCaAddrName(caAddrName);
        dropDownAddress.setCaAddr1(caAddr1);
        dropDownAddress.setCaAddr2(caAddr2);
        dropDownAddress.setCaAddr3(caAddr3);
        dropDownAddress.setCaRt(caRt);
        dropDownAddress.setCaRw(caRw);
        dropDownAddress.setCaKel(caKel);
        dropDownAddress.setCaKec(caKec);
        dropDownAddress.setCaCity(caCity);
        dropDownAddress.setCaProvinsi(caProvinsi);
        dropDownAddress.setCaZipCode(caZipCode);
        dropDownAddress.setCaBiCityCode(caBiCityCode);
        dropDownAddress.setCaAddrInvalid(caAddrInvalid);
        dropDownAddress.setCaPhnArea(caPhnArea);
        dropDownAddress.setCaPhnNum(caPhnNum);
        dropDownAddress.setCaPhnExt(caPhNext);
        dropDownAddress.setCaPhnNumInvalid(caPhnNumInvalid);
        dropDownAddress.setCaFaxArea(caFaxArea);
        dropDownAddress.setCaFaxNum(caFaxNum);
        dropDownAddress.setCaFaxExt(caFaxExt);
        dropDownAddress.setCaFaxNumInvalid(caFaxNumInvalid);
        dropDownAddress.setCaNote(caNote);
        dropDownAddress.setCaPriority(caPriority);
        dropDownAddress.setCaBagian(caBagian);

        return dropDownAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
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

    public String getCuRef() {
        return cuRef;
    }

    public void setCuRef(String cuRef) {
        this.cuRef = cuRef;
    }

    public int getCaId() {
        return caId;
    }

    public void setCaId(int caId) {
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

    public int getCaPrioriy() {
        return caPriority;
    }

    public void setCaPriority(int caPriority) {
        this.caPriority = caPriority;
    }

    public String getCaBagian() {
        return caBagian;
    }

    public void setCaBagian(String caBagian) {
        this.caBagian = caBagian;
    }
}
