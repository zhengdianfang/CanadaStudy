package com.canadian.study.ui.site;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canadian.study.R;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdianfang
 * Date: 14-2-22
 * Time: 下午9:22
 * Note: 展位图页面
 */
public class SiteFragment8 extends BaseSiteFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.site_image_layout8, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        cityName = getString(R.string.chengdu);
        super.onViewCreated(view, savedInstanceState);
    }

}
