package com.mitkoindo.smartcollection.objectdata.databasemodel;

import com.mitkoindo.smartcollection.objectdata.AddressNew;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ericwijaya on 10/1/17.
 */

public class AddressNewDb extends RealmObject {

    @PrimaryKey
    private String id;

    private String kodeAlamat;

    private String tipeAlamat;

    private String alamat;

    public AddressNewDb() {
    }

    public AddressNewDb(AddressNew addressNew) {
        this.id = UUID.randomUUID().toString();
        this.kodeAlamat = addressNew.getKodeAlamat();
        this.tipeAlamat = addressNew.getTipeAlamat();
        this.alamat = addressNew.getAlamat();
    }

    public AddressNew toAddressNew() {
        AddressNew addressNew = new AddressNew();
        addressNew.setKodeAlamat(kodeAlamat);
        addressNew.setTipeAlamat(tipeAlamat);
        addressNew.setAlamat(alamat);

        return addressNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodeAlamat() {
        return kodeAlamat;
    }

    public void setKodeAlamat(String kodeAlamat) {
        this.kodeAlamat = kodeAlamat;
    }

    public String getTipeAlamat() {
        return tipeAlamat;
    }

    public void setTipeAlamat(String tipeAlamat) {
        this.tipeAlamat = tipeAlamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
