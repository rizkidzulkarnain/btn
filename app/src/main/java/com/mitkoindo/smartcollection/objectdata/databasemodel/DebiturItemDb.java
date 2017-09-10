package com.mitkoindo.smartcollection.objectdata.databasemodel;

import com.mitkoindo.smartcollection.objectdata.DebiturItem;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/6/17.
 */

public class DebiturItemDb extends RealmObject {

    private String no;

    private String noCif;

    private String nama;

    @PrimaryKey
    private String noRekening;

    private String tagihan;

    private String dpd;

    private String lastPayment;

    private String useAssignDate;

    private String customerReference;

    private String resultDate;


    public DebiturItemDb() {
    }

    public DebiturItemDb(DebiturItem debiturItem) {
        no = debiturItem.getNo();
        noCif = debiturItem.getNoCif();
        nama = debiturItem.getNama();
        noRekening = debiturItem.getNoRekening();
        tagihan = debiturItem.getTagihan();
        dpd = debiturItem.getDpd();
        lastPayment = debiturItem.getLastPayment();
        useAssignDate = debiturItem.getUseAssignDate();
        customerReference = debiturItem.getCustomerReference();
        resultDate = debiturItem.getResultDate();
    }

    public DebiturItem toDebiturItem() {
        DebiturItem debiturItem = new DebiturItem();
        debiturItem.setNo(no);
        debiturItem.setNoCif(noCif);
        debiturItem.setNama(nama);
        debiturItem.setNoRekening(noRekening);
        debiturItem.setTagihan(tagihan);
        debiturItem.setDpd(dpd);
        debiturItem.setLastPayment(lastPayment);
        debiturItem.setUseAssignDate(useAssignDate);
        debiturItem.setCustomerReference(customerReference);
        debiturItem.setResultDate(resultDate);

        return debiturItem;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getTagihan() {
        return tagihan;
    }

    public void setTagihan(String tagihan) {
        this.tagihan = tagihan;
    }

    public String getDpd() {
        return dpd;
    }

    public void setDpd(String  dpd) {
        this.dpd = dpd;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNoCif() {
        return noCif;
    }

    public void setNoCif(String noCif) {
        this.noCif = noCif;
    }

    public String getUseAssignDate() {
        return useAssignDate;
    }

    public void setUseAssignDate(String useAssignDate) {
        this.useAssignDate = useAssignDate;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }
}
