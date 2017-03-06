package com.canadian.study.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.canadian.study.CanadianApplication;
import com.canadian.study.R;
import com.canadian.study.bean.Message;
import com.canadian.study.bean.Version;
import com.canadian.study.net.SimpleXmlRequest;
import com.canadian.study.ui.site.SiteActivity;
import com.canadian.study.ui.views.MarqueeTextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private MarqueeTextView msgContentTextview;
    private AlertDialog mAlertDialog;
    private String downloadAppUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        //update app
        SimpleXmlRequest simpleXmlRequest = new SimpleXmlRequest(Request.Method.GET, "http://canada.tripphone.com.cn:8100/canada_apk_update.xml", Version.class, new Response.Listener<Version>() {
            @Override
            public void onResponse(Version response) {
                try {
                    String nowVersionCode =  getPackageManager().getPackageInfo(getPackageName(),0).versionName;
                    if (!nowVersionCode.equals(response.version)) {
                        downloadAppUrl = response.url;
                        mAlertDialog.show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }, error -> {
            error.printStackTrace();
        });

       CanadianApplication.instance.mRequestQueue.add(simpleXmlRequest);

    }

    private void initViews() {
        findViewById(R.id.commonUrlView).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CommonNetworkActivity.class));
        });

        findViewById(R.id.visaView).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, VisaInforActivity.class));
        });

        findViewById(R.id.joinSchoolView).setOnClickListener(view->{
            startActivity(new Intent(MainActivity.this, UniversityInforActivity.class));
        });

        findViewById(R.id.searchView).setOnClickListener(view->{
            startActivity(new Intent(MainActivity.this, SearchUniversityActivity.class));
        });

        findViewById(R.id.sitePicView).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SiteActivity.class));
        });

        findViewById(R.id.visaInfoView).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CanadianInforActivity.class));
        });

        findViewById(R.id.messageFrame).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MessageListActivity.class));
        });

        TextView visaInfoView = (TextView) findViewById(R.id.visaInfoView);
        visaInfoView.setText(Html.fromHtml(getString(R.string.main_text_1)));

        msgContentTextview = (MarqueeTextView) findViewById(R.id.msgContentTextview);
        realm = Realm.getDefaultInstance();
        RealmResults<Message> messageRealmResults = realm.where(Message.class).findAll().sort("time", Sort.DESCENDING);
        if (messageRealmResults != null) {
            List<Message> messages = realm.copyFromRealm(messageRealmResults);
            StringBuffer buffer = new StringBuffer();
            for (Message message : messages) {
                buffer.append(message.content).append("      ");
            }
            msgContentTextview.setText(Html.fromHtml(buffer.toString()));
        }


        realm.addChangeListener(element -> {
            Message message = element.where(Message.class).findAllSorted("id", Sort.DESCENDING).first();
            msgContentTextview.setText(Html.fromHtml(message.content));
        });

        mAlertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("更新")
                .setMessage("有新版本，是否更新？")
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadAppUrl));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(-1);
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).create();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeAllChangeListeners();
        CanadianApplication.instance.mRequestQueue.cancelAll("update");
    }
}
