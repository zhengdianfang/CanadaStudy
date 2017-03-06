package com.canadian.study.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;

public class CanadianInforActivity extends AppCompatActivity implements LeftMenuFragment.OnMenuClickListener {

    private LeftMenuFragment leftMenuFragment;
    private ViewPager viewPager;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canadian_infor);
        findViewById(R.id.backImage).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.backTextView).setOnClickListener(view -> finish());

        findViewById(R.id.menuImageButton).setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.END);
        });

        leftMenuFragment = ((LeftMenuFragment) getSupportFragmentManager().findFragmentById(R.id.leftMenuFragment));
        leftMenuFragment.setOnMenuClickListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ContentPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    leftMenuFragment.setSelectMenu(0);
                }else if (position == 1){
                    leftMenuFragment.setSelectMenu(1);
                }else if (position > 1 && position < 6){
                    leftMenuFragment.setSelectMenu(2);
                }else if (position == 6){
                    leftMenuFragment.setSelectMenu(3);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMenuClick(int index) {
        switch (index){
            case 0:
                viewPager.setCurrentItem(0, false);
                break;
            case 1:
                viewPager.setCurrentItem(1, false);
                break;

            case 2:
                viewPager.setCurrentItem(2, false);
                break;

            case 3:
                viewPager.setCurrentItem(6, false);
                break;
        }
        drawer.closeDrawer(GravityCompat.END);
    }

    private static class ContentPagerAdapter extends PagerAdapter{
        private int[] layoutIds = {R.layout.fragment_canadian_infor_1_layout, R.layout.fragment_canadian_infor_2_layout,
                R.layout.fragment_canadian_infor_3_layout, R.layout.fragment_canadian_infor_4_layout,
                R.layout.fragment_canadian_infor_5_layout,R.layout.fragment_canadian_infor_6_layout, R.layout.fragment_canadian_infor_19_layout};
        @Override
        public int getCount() {
            return layoutIds.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            LayoutInflater from = LayoutInflater.from(container.getContext());
            ViewDataBinding dataBinding = DataBindingUtil.inflate(from, layoutIds[position], container, false);
            container.addView(dataBinding.getRoot());
            TextView contentView = ((TextView) dataBinding.getRoot().findViewById(R.id.contentView));
            if (contentView != null) {
                contentView.setMovementMethod(new LinkMovementMethod());
            }
            return dataBinding.getRoot();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
