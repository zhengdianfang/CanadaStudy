package com.canadian.study;

import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.canadian.study.common.Constants;
import com.canadian.study.common.DatasUtils;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zheng on 16/9/7.
 */

public class CanadianApplication extends MultiDexApplication {

    public RequestQueue mRequestQueue;
    public static CanadianApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AndroidNetworking.initialize(getApplicationContext());
        JPushInterface.init(this);
        JPushInterface.setDebugMode(false);
        initRealmClient();

        mRequestQueue =  Volley.newRequestQueue(this);

    }

    private void initRealmClient() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(Constants.CANADIA_DB)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatasUtils.sSchoolCities = null;
        DatasUtils.sUniversities = null;
        Realm defaultInstance = Realm.getDefaultInstance();
        if (!defaultInstance.isClosed()) {
            defaultInstance.close();
        }
        instance = null;
    }
}
