package com.canadian.study.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.Message;
import com.canadian.study.ui.adapter.BaseListAdapter;
import com.canadian.study.ui.adapter.OnItemClickListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zheng on 16/9/18.
 */

public class MessageListActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        findViewById(R.id.backImage).setOnClickListener(view -> finish());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMessageAdapter = new MessageAdapter(this);
        recyclerView.setAdapter(mMessageAdapter);


        Observable.just(loadMessage4Db()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    if (messages != null) {
                        Observable.just(messages).subscribe(mMessageAdapter);
                    }
                });
    }

    private List<Message> loadMessage4Db(){
        List<Message> messages = null;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Message> messageRealmResults = realm.where(Message.class).findAllSorted("time", Sort.ASCENDING);
        if (messageRealmResults != null) {
           messages = realm.copyFromRealm(messageRealmResults);
        }
        return messages;
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra("message", mMessageAdapter.getItem(position));
        startActivity(intent);
    }

    private static class MessageAdapter extends BaseListAdapter<Message>{

        public MessageAdapter(OnItemClickListener onItemClickListener) {
            super(onItemClickListener);
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_item_layout, parent, false)) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            TextView contentView = (TextView) holder.itemView.findViewById(R.id.contentView);
            contentView.setText(Html.fromHtml(datas.get(position).content));
        }
    }
}
