package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.canadian.study.R;
import com.canadian.study.bean.NetData;
import com.canadian.study.common.Constants;
import com.canadian.study.ui.adapter.CommonNetworkAdapter;
import com.canadian.study.ui.adapter.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zheng on 16/9/8.
 */

public class CommonNetworkActivity extends AppCompatActivity implements OnItemClickListener {

    private CommonNetworkAdapter commonNetworkAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_network_list_layout);

        findViewById(R.id.backImage).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.backTextView).setOnClickListener(view -> onBackPressed());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        commonNetworkAdapter = new CommonNetworkAdapter(this);
        recyclerView.setAdapter(commonNetworkAdapter);

        Observable.just(pariseJson2List())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(netDatas -> {
                    if (netDatas != null) {
                        Observable.just(netDatas).subscribe(commonNetworkAdapter);
                    }
                });

    }

    @Override
    public void onItemClick(View v, int position) {

    }

    private ArrayList<NetData> pariseJson2List(){
        Gson gson = new GsonBuilder().create();
        ArrayList<NetData> datas = gson.fromJson(Constants.PRACTIAL_NET_JSON, new TypeToken<ArrayList<NetData>>(){}.getType());
        return datas;
    }
}
