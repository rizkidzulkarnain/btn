package com.mitkoindo.smartcollection;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class MyApplication extends Application {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initCalligraphy();
        initRealm();
        initStetho();
    }

    public static Application getInstance() {
        return sInstance;
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_default_reg))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }
}
