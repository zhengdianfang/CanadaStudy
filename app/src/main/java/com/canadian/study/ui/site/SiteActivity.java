package com.canadian.study.ui.site;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.common.Constants;

public class SiteActivity extends AppCompatActivity {




    private int selectTabIndex = 0;
    private String[] fragmentNames = {
            SiteFragment1.class.getName(),
            SiteFragment2.class.getName(),
            SiteFragment3.class.getName(),
            SiteFragment8.class.getName(),
            SiteFragment10.class.getName(),
    };
    private ViewGroup tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        findViewById(R.id.backImage).setOnClickListener(view -> {
            finish();
        });
        tablayout = (ViewGroup) findViewById(R.id.tablayout);
        assembleTabLayout();
        changeTabBackground(selectTabIndex);
        switchFragmentShow(selectTabIndex);
    }

    private void assembleTabLayout() {

        for (int i = 0; i < Constants.citynames.length; i++) {
            TextView tabView = (TextView) LayoutInflater.from(this).inflate(R.layout.city_tab_item_layout, null);
            tabView.setText(Constants.citynames[i]);
            tabView.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewNormalBackgroundIds[i], 0, 0);
            tabView.setTag(i);
            tabView.setPadding(72, 0, 72, 0);
            tabView.setOnClickListener(view -> {
                int tag = (int) view.getTag();
                if (tag != selectTabIndex){
                    selectTabIndex = tag;
                    changeTabBackground(selectTabIndex);
                    switchFragmentShow(selectTabIndex);
                }
            });
            tablayout.addView(tabView);
        }
    }


    private void changeTabBackground(int i) {
        for (int i1 = 0; i1 < tablayout.getChildCount(); i1++) {
            TextView childAt = (TextView) tablayout.getChildAt(i1);
            if (i1 == i){
                childAt.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewSelectedBackgroundIds[i1], 0, 0);
            }else {
                childAt.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewNormalBackgroundIds[i1],0 ,0);
            }
        }

    }

    private void switchFragmentShow(int index){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentNames.length; i++) {
            BaseSiteFragment fragment = (BaseSiteFragment) Fragment.instantiate(this, fragmentNames[i]);
            fragment.setCanClick(false);
            if (i == index){
                fragmentTransaction.add(R.id.contentFrame, fragment);
            }else {
                if (fragment.isAdded()) {
                    fragmentTransaction.remove(fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }
}
