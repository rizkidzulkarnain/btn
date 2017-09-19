package com.mitkoindo.smartcollection.helper;

import com.mitkoindo.smartcollection.MyApplication;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressDb;
import com.mitkoindo.smartcollection.objectdata.DropDownAddressType;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.DropDownTeleponType;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DebiturItemDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.DetailDebiturDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.PhoneNumberDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormCallDb;
import com.mitkoindo.smartcollection.objectdata.databasemodel.SpParameterFormVisitDb;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class RealmHelper {

//    Tujuan DropDown
    public static void storeListDropDownPurpose(List<DropDownPurpose> listDropDownPurpose) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownPurpose);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownPurpose> getListDropDownPurpose() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownPurpose> results = realm
                    .where(DropDownPurpose.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownPurpose() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownPurpose> results = realm
                    .where(DropDownPurpose.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Relationship DropDown
    public static void storeListDropDownRelationship(List<DropDownRelationship> listDropDownRelationship) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownRelationship);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownRelationship> getListDropDownRelationship() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownRelationship> results = realm
                    .where(DropDownRelationship.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownRelationship() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownRelationship> results = realm
                    .where(DropDownRelationship.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Result DropDown
    public static void storeListDropDownResult(List<DropDownResult> listDropDownResult) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownResult);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownResult> getListDropDownResult() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownResult> results = realm
                    .where(DropDownResult.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownResult() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownResult> results = realm
                    .where(DropDownResult.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Reason DropDown
    public static void storeListDropDownReason(List<DropDownReason> listDropDownReason) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownReason);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownReason> getListDropDownReason() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownReason> results = realm
                    .where(DropDownReason.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownReason() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownReason> results = realm
                    .where(DropDownReason.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Action DropDown
    public static void storeListDropDownAction(List<DropDownAction> listDropDownAction) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownAction);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownAction> getListDropDownAction() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAction> results = realm
                    .where(DropDownAction.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownAction() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAction> results = realm
                    .where(DropDownAction.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Status Agunan DropDown
    public static void storeListDropDownStatusAgunan(List<DropDownStatusAgunan> listDropDownStatusAgunan) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownStatusAgunan);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownStatusAgunan> getListDropDownStatusAgunan() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownStatusAgunan> results = realm
                    .where(DropDownStatusAgunan.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownStatusAgunan() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownStatusAgunan> results = realm
                    .where(DropDownStatusAgunan.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Telepon Type
    public static void storeListDropDownTeleponType(List<DropDownTeleponType> listDropDownTeleponType) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownTeleponType);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownTeleponType> getListDropDownTeleponType() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownTeleponType> results = realm
                    .where(DropDownTeleponType.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownTeleponType() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownTeleponType> results = realm
                    .where(DropDownTeleponType.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Address Type
    public static void storeListDropDownAddressType(List<DropDownAddressType> listDropDownAddressType) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownAddressType);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownAddressType> getListDropDownAddressType() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAddressType> results = realm
                    .where(DropDownAddressType.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownAddressType() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAddressType> results = realm
                    .where(DropDownAddressType.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Form Call
    public static void storeFormCall(SpParameterFormCallDb spParameterFormCallDb) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(spParameterFormCallDb);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<SpParameterFormCallDb> getListFormCall() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<SpParameterFormCallDb> results = realm
                    .where(SpParameterFormCallDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListFormCall() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<SpParameterFormCallDb> results = realm
                    .where(SpParameterFormCallDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static void deleteFormCall(String noRekening) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            SpParameterFormCallDb spParameterFormCallDb = realm
                    .where(SpParameterFormCallDb.class)
                    .equalTo("accountNo", noRekening)
                    .findFirst();

            if (spParameterFormCallDb != null) {
                realm.beginTransaction();
                spParameterFormCallDb.deleteFromRealm();
                realm.commitTransaction();
            }
        } finally {
            realm.close();
        }
    }

    //    Form Visit
    public static void storeFormVisit(SpParameterFormVisitDb spParameterFormVisitDb) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(spParameterFormVisitDb);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<SpParameterFormVisitDb> getListFormVisit() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<SpParameterFormVisitDb> results = realm
                    .where(SpParameterFormVisitDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListFormVisit() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<SpParameterFormVisitDb> results = realm
                    .where(SpParameterFormVisitDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static void deleteFormVisit(String noRekening) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            SpParameterFormVisitDb spParameterFormVisitDb = realm
                    .where(SpParameterFormVisitDb.class)
                    .equalTo("accNo", noRekening)
                    .findFirst();

            if (spParameterFormVisitDb != null) {
                realm.beginTransaction();
                spParameterFormVisitDb.deleteFromRealm();
                realm.commitTransaction();
            }
        } finally {
            realm.close();
        }
    }

//    Debitur Item
    public static void storeListDebiturItem(List<DebiturItemDb> listDebiturItemDb) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDebiturItemDb);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DebiturItemDb> getListDebiturItem() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DebiturItemDb> results = realm
                    .where(DebiturItemDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDebiturItem() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DebiturItemDb> results = realm
                    .where(DebiturItemDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Detail Debitur Item
    public static void storeListDetailDebitur(List<DetailDebiturDb> listDetailDebiturDb) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDetailDebiturDb);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DetailDebiturDb> getListDetailDebitur() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DetailDebiturDb> results = realm
                    .where(DetailDebiturDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static DetailDebiturDb getDetailDebitur(String noRekening) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            DetailDebiturDb result = realm
                    .where(DetailDebiturDb.class)
                    .equalTo("noRekening", noRekening)
                    .findFirst();
            if (result != null) {
                return realm.copyFromRealm(result);
            } else {
                return null;
            }
        } finally {
            realm.close();
        }
    }

    public static void deleteListDetailDebitur() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DetailDebiturDb> results = realm
                    .where(DetailDebiturDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Phone Number
    public static void storeListPhoneNumber(List<PhoneNumberDb> listPhoneNumberDb) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listPhoneNumberDb);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<PhoneNumberDb> getListPhoneNumber() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<PhoneNumberDb> results = realm
                    .where(PhoneNumberDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static List<PhoneNumberDb> getPhoneNumber(String noRekening) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            List<PhoneNumberDb> results = realm
                    .where(PhoneNumberDb.class)
                    .equalTo("nomorRekening", noRekening)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static void deleteListPhoneNumber() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<PhoneNumberDb> results = realm
                    .where(PhoneNumberDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

//    Address
    public static void storeListAddress(List<DropDownAddressDb> listDropDownAddresDbs) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(listDropDownAddresDbs);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public static List<DropDownAddressDb> getListDropDownAddress() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAddressDb> results = realm
                    .where(DropDownAddressDb.class)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }

    public static List<DropDownAddressDb> getAddress(String noRekening) {
        Realm realm = MyApplication.getRealmInstance();
        try {
            List<DropDownAddressDb> result = realm
                    .where(DropDownAddressDb.class)
                    .equalTo("accNo", noRekening)
                    .findAll();
            return realm.copyFromRealm(result);
        } finally {
            realm.close();
        }
    }

    public static void deleteListDropDownAddress() {
        Realm realm = MyApplication.getRealmInstance();
        try {
            RealmResults<DropDownAddressDb> results = realm
                    .where(DropDownAddressDb.class)
                    .findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }
}
