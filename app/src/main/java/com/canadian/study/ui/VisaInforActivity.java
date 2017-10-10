package com.canadian.study.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.canadian.study.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VisaInforActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_infor);


        findViewById(R.id.backImage).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.backTextView).setOnClickListener(view -> onBackPressed());

        StringBuffer sb = new StringBuffer();
        InputStream in = getResources().openRawResource(R.raw.visa_introduction);
        InputStreamReader insr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(insr);
        String s;
        try {
            while ((s = br.readLine()) != null){
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                insr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
