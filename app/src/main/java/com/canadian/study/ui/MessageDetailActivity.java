package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.Message;

/**
 * Created by zheng on 16/9/18.
 */

public class MessageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail_layout);

        Message message = getIntent().getParcelableExtra("message");

        findViewById(R.id.backImage).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.titleView)).setText(message.title);

        TextView contentView = (TextView) findViewById(R.id.contentView);
        contentView.setText(Html.fromHtml(message.content));
        contentView.setMovementMethod(new LinkMovementMethod());
    }
}
