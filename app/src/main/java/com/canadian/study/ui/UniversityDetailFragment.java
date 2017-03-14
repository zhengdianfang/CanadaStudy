package com.canadian.study.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.canadian.study.R;
import com.canadian.study.bean.SchoolCity;
import com.canadian.study.bean.University;
import com.canadian.study.ui.site.SiteActivity;
import com.canadian.study.ui.views.CircleImageView;

/**
 * Created by zheng on 16/9/12.
 */

public class UniversityDetailFragment extends Fragment {

    private CircleImageView circleImageView;
    private ImageAdapter imageAdapter;

    public static void openUniversityDetailFragment(FragmentActivity activity, University university){
        Bundle bundle = new Bundle();
        bundle.putParcelable("university", university);
        activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                Fragment.instantiate(activity, UniversityDetailFragment.class.getName(),bundle))
                .addToBackStack("university").commit();
    }

    private University mUniversity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_university_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindClicks();
        if (getArguments() != null) {
            mUniversity =  getArguments().getParcelable("university");
        }
        if (mUniversity != null) {
            ((TextView) getView().findViewById(R.id.cnameView)).setText(mUniversity.chineseName);
            ((TextView) getView().findViewById(R.id.enameView)).setText(mUniversity.englishName);
            TextView contentView = (TextView) getView().findViewById(R.id.contentView);
            StringBuffer detail = new StringBuffer(mUniversity.detail == null ? mUniversity.detail : "");
            detail.append("\n");
            if (!mUniversity.phonenum.equalsIgnoreCase("0")){
                detail.append(getString(R.string.school_phone, mUniversity.phonenum));
                detail.append("\n");
            }
            if (!mUniversity.email.equalsIgnoreCase("@")){
                detail.append(getString(R.string.school_email, mUniversity.email));
                detail.append("\n");
            }

            detail.append(getString(R.string.school_net_1, mUniversity.netUrl));
            detail.append("\n");

            if (null != mUniversity.schoolCities && !mUniversity.schoolCities.isEmpty()){
                detail.append(getString(R.string.join_city));
            }
            contentView.setText(detail);
            initCityGrid();
            initViewPager();
        }
    }

    void bindClicks() {
        getView().findViewById(R.id.backImage).setOnClickListener(view -> {
            if (getActivity() != null) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        getView().findViewById(R.id.applySchoolButton).setOnClickListener(view -> {
            sendEmail(mUniversity.email);
        });

        getView().findViewById(R.id.selectHelpButton).setOnClickListener(view -> {
            sendEmail("BEJING-Education@international.gc.ca");
        });

        getView().findViewById(R.id.schoolNetButton).setOnClickListener(view -> {
            String url = mUniversity.netUrl;
            if (!url.startsWith("http://")){
                url = "http://"+url;
            }
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(it);
      });
    }

    void initViewPager() {
        if (!TextUtils.isEmpty(mUniversity.picPaths)){
            String[] split = TextUtils.split(mUniversity.picPaths, ";;");
            imageAdapter = new ImageAdapter(split);
            ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
            circleImageView = (CircleImageView) getView().findViewById(R.id.circleView);
            viewPager.setAdapter(imageAdapter);
            circleImageView.setCount(imageAdapter.getCount(), 0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    circleImageView.setCount(imageAdapter.getCount(), position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    void initCityGrid() {

        GridLayout gridLayout = (GridLayout) getView().findViewById(R.id.gridView);
        if (mUniversity.schoolCities != null) if (!mUniversity.schoolCities.isEmpty()) {
            for (SchoolCity schoolCity : mUniversity.schoolCities) {
                TextView textView = new TextView(getContext());
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(12f);
                textView.setGravity(Gravity.CENTER);
                textView.setText(schoolCity.siteNum);
                textView.setCompoundDrawablePadding(16);
                textView.setPadding(16, 16, 16, 16);
                String a = schoolCity.joinCity;
                if (a.equals(getString(R.string.beijing))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.beijing_icon, 0, 0);
                }else if (a.equals(getString(R.string.shanghai))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.shanghai_icon, 0, 0);
                } else if (a.equals(getString(R.string.zhengzhou))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.zhengzhou, 0, 0);
                }else if (a.equals(getString(R.string.wuhan))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.wuhan_icon, 0, 0);
                }else if (a.equals(getString(R.string.nanjin))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nanjin, 0, 0);
                }else if (a.equals(getString(R.string.guangzhou))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.guangzhou, 0, 0);
                }else if (a.equals(getString(R.string.chongqing))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.chongqing, 0, 0);
                }else if (a.equals(getString(R.string.chengdu))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.chengdu, 0, 0);
                }else if (a.equals(getString(R.string.xian))){
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.xian, 0, 0);
                }
                textView.setOnClickListener(view -> startActivity(new Intent(getActivity(), SiteActivity.class)));
                gridLayout.addView(textView);
            }
        }
    }


    private void sendEmail(String sendTo){
        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        localIntent.setType("application/octet-stream");
        localIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{sendTo});
        localIntent.putExtra(Intent.EXTRA_TEXT,"\n\n\n\n\n\n\n[Study in Canada APP.Developed by Embassy of Canada in Beijing]");

        String title = Build.MODEL + " exception";

        localIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        startActivity(localIntent);
    }

    private static class ImageAdapter extends PagerAdapter{

        private String[] images;

        public ImageAdapter(String[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            Glide.with(container.getContext()).load(images[position]).centerCrop().crossFade()
                    .override(container.getContext().getResources().getDisplayMetrics().widthPixels, dip2px(container.getContext(), 150)).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

    }
}
