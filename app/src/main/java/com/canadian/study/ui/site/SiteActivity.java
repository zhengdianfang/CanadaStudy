package com.canadian.study.ui.site;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.canadian.study.R;

public class SiteActivity extends AppCompatActivity {

    private int[] tabViewIds = {R.id.beijingTabView, R.id.shanghaiTabView, R.id.guangzhouTabView, R.id.chengduTabView};

    private int[] tabViewNormalBackgroundIds = {R.drawable.beijing_icon_drawable_normal, R.drawable.shanghai_icon_drawable_normal,
            R.drawable.guangzhou_icon_drawable_normal, R.drawable.chengdu_icon_drawable_normal};

    private int[] tabViewSelectedBackgroundIds = {R.drawable.beijing_icon_drawable_selected, R.drawable.shanghai_icon_drawable_selected,
            R.drawable.guangzhou_icon_drawable_selected, R.drawable.chengdu_icon_drawable_selected};

    private String[] citynames = {"北京", "上海", "广州", "成都"};

    private TextView[] tabViews = new TextView[4];

    private int selectTabIndex = 0;
    private Fragment fragment1, fragment2, fragment3, fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        findViewById(R.id.backImage).setOnClickListener(view -> {
            finish();
        });
        fragment1 = Fragment.instantiate(this, SiteFragment1.class.getName());
        fragment2 = Fragment.instantiate(this, SiteFragment2.class.getName());
        fragment3 = Fragment.instantiate(this, SiteFragment3.class.getName());
        fragment4 = Fragment.instantiate(this, SiteFragment8.class.getName());

        for (int i = 0; i < tabViewIds.length; i++) {
            tabViews[i] = (TextView) findViewById(tabViewIds[i]);
            tabViews[i].setTag(i);
            tabViews[i].setOnClickListener(view -> {
                int tag = (int) view.getTag();
                if (tag != selectTabIndex){
                    selectTabIndex = tag;
                    changeTabBackground(selectTabIndex);
                    switchFragmentShow(selectTabIndex);
                }
            });
        }
        changeTabBackground(selectTabIndex);
        switchFragmentShow(selectTabIndex);
    }


    private void changeTabBackground(int i) {
        for (int i1 = 0; i1 < tabViews.length; i1++) {
            if (i1 == i){
                tabViews[i1].setCompoundDrawablesWithIntrinsicBounds(0, tabViewSelectedBackgroundIds[i1], 0, 0);
            }else {
                tabViews[i1].setCompoundDrawablesWithIntrinsicBounds(0, tabViewNormalBackgroundIds[i1],0 ,0);
            }
        }

    }

    private void switchFragmentShow(int index){
        Fragment[] fragments = {fragment1, fragment2, fragment3, fragment4};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == index){
                fragmentTransaction.add(R.id.contentFrame, fragments[i]);
            }else {
                if (fragments[i].isAdded()) {
                    fragmentTransaction.remove(fragments[i]);
                }
            }
        }
        fragmentTransaction.commit();
    }
}
