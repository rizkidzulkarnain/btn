package com.mitkoindo.smartcollection.objectdata.databasemodel;

import com.mitkoindo.smartcollection.objectdata.PhoneNumber;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 9/9/17.
 */

public class PhoneNumberDb extends RealmObject {

    @PrimaryKey
    private String id;

    private String nomorRekening;

    private String namaKontak;

    private String hubungan;

    private String jenisKontak;

    private String nomorKontak;


    public PhoneNumberDb() {
    }

    public PhoneNumberDb(PhoneNumber phoneNumber) {
        id = UUID.randomUUID().toString();
        nomorRekening = phoneNumber.getNomorRekening();
        namaKontak = phoneNumber.getNamaKontak();
        hubungan = phoneNumber.getHubungan();
        jenisKontak = phoneNumber.getJenisKontak();
        nomorKontak = phoneNumber.getNomorKontak();
    }

    public PhoneNumber toPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNomorRekening(nomorRekening);
        phoneNumber.setNamaKontak(namaKontak);
        phoneNumber.setHubungan(hubungan);
        phoneNumber.setJenisKontak(jenisKontak);
        phoneNumber.setNomorKontak(nomorKontak);

        return phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getNamaKontak() {
        return namaKontak;
    }

    public void setNamaKontak(String namaKontak) {
        this.namaKontak = namaKontak;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getJenisKontak() {
        return jenisKontak;
    }

    public void setJenisKontak(String jenisKontak) {
        this.jenisKontak = jenisKontak;
    }

    public String getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(String nomorKontak) {
        this.nomorKontak = nomorKontak;
    }
}
