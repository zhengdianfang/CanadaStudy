package com.canadian.study.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.canadian.study.R;
import com.canadian.study.bean.Booth;
import com.canadian.study.bean.Message;
import com.canadian.study.bean.SchoolCity;
import com.canadian.study.bean.University;
import com.canadian.study.common.Constants;
import com.canadian.study.common.DatasUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        try {
            updateAppChangeDatas();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        getNewMessages();

    }

    private void updateAppChangeDatas() throws PackageManager.NameNotFoundException {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String last_version = defaultSharedPreferences.getString("last_version", "1.0");
        String versionName = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        if (versionName.compareTo(last_version) > 0) {

            Realm defaultInstance = Realm.getDefaultInstance();
            defaultInstance.beginTransaction();
            defaultInstance.delete(University.class);
            defaultInstance.delete(SchoolCity.class);
            defaultInstance.commitTransaction();
            //update pics
            File file = new File(getCacheDir(), "schoolPics.zip");
            if (file.exists()) {
                file.delete();
                File picDic = new File(getCacheDir(), "school_pics");
                if (picDic.exists()) {
                    DatasUtils.deleteDir(picDic);
                }
            }
            defaultSharedPreferences.edit().putString("last_version", versionName).commit();
        }
    }

    private void initData() {
        Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                ArrayList<Booth> booths = new ArrayList<>();
                for (int boothRaw : Constants.boothArr){
                    List<Booth> boothList = DatasUtils.readBoothData(LoadingActivity.this.getApplicationContext(), boothRaw);
                    if (null != boothList){
                        booths.addAll(boothList);
                    }
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    for (int i = 0; i < booths.size(); i++) {
                        booths.get(i).id = i;
                        realm.copyToRealmOrUpdate(booths.get(i));
                    }
                    realm.commitTransaction();
                }
                DatasUtils.readSchoolBooth(LoadingActivity.this);
                DatasUtils.readSchoolData2Db(LoadingActivity.this);
//                DatasUtils.readNewMsg2Db(LoadingActivity.this);
                subscriber.onNext(true);
            }
            subscriber.onCompleted();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    finish();
                });
    }

    private void getNewMessages(){
        AndroidNetworking.get("http://www.chushanju.com/news.txt")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Realm realm = Realm.getDefaultInstance();
                            String[] arr = response.split("\n");
                            if (arr != null) {
                                realm.beginTransaction();
                                for (int i = 0; i < arr.length; i++) {
                                    Message message = parseMessageBean(arr[i]);
                                    long count = realm.where(Message.class).equalTo("content", message.content).count();
                                    if (count == 0 ){
                                        Number id = realm.where(Message.class).max("id");
                                        message.id = id == null ? 1 : id.longValue() + 1;
                                        realm.copyToRealm(message);
                                    }
                                }
                                realm.commitTransaction();
                            }
                        }
                        initData();
                    }

                    @Override
                    public void onError(ANError anError) {
                        initData();
                    }
                });
    }

    private Message parseMessageBean(String str){
        Message message = null;
        if (!TextUtils.isEmpty(str)){
            message = new Message();
            String[] data = str.split("###");
            for (int j=0; j<data.length; j++){
                if (j == 0){
                    String title = data[j];
                    message.title = title;
                }else if (j == 1){
                    String content = data[j];
                    message.content = content;
                }
            }
            message.time = System.currentTimeMillis();
        }
        return message;
    }
}
