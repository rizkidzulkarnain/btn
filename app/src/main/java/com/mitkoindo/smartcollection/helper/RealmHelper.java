package com.mitkoindo.smartcollection.helper;

import com.mitkoindo.smartcollection.MyApplication;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ericwijaya on 8/24/17.
 */

public class RealmHelper {

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
}
