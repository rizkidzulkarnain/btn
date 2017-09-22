package com.mitkoindo.smartcollection;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class MyApplication extends MultiDexApplication {

    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initTimber();
        initCalligraphy();
        initRealm();
        initStetho();
    }

    public static Application getInstance() {
        return sInstance;
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
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
//        Default Stetho init
//        Stetho.initializeWithDefaults(this);

//        Stetho with Realm
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
