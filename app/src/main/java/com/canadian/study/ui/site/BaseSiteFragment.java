package com.canadian.study.ui.site;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.canadian.study.R;
import com.canadian.study.bean.Booth;
import com.canadian.study.bean.SchoolCity;
import com.canadian.study.bean.University;
import com.canadian.study.ui.CommonUniversityListFragment;
import com.canadian.study.ui.UniversityDetailFragment;
import com.canadian.study.ui.views.GestureImageView;
import com.canadian.study.ui.views.GestureImageViewListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdianfang
 * Date: 14-3-4
 * Time: 下午2:41
 * Note:
 */
public class BaseSiteFragment extends Fragment implements GestureImageViewListener, View.OnClickListener {


    private float pos_x;
    private float pos_y;
    private GestureImageView gestureImageView;
    private List<Booth> booths;
    private float diffx;
    private float diffy;

    protected String cityName;

    private static final float DEFLAUT_SCALE = 1.0f;
    private float nowScale;
    private boolean canClick = true;
    private Realm realm;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        RealmResults<Booth> boothRealmResults = realm.where(Booth.class).equalTo("joinCity", cityName).findAll();
        booths  = realm.copyFromRealm(boothRealmResults);
        gestureImageView = (GestureImageView) view.findViewById(R.id.image);
        gestureImageView.setGestureImageViewListener(this);
        float src_x = gestureImageView.getImageWidth()/2.0f * DEFLAUT_SCALE;
        float src_y = gestureImageView.getImageHeight()/2.0f * DEFLAUT_SCALE;
        gestureImageView.setStartingPosition(src_x, src_y);
        gestureImageView.setStartingScale(DEFLAUT_SCALE);
        gestureImageView.setOnClickListener(this);

    }

    @Override
    public void onTouch(float x, float y) {
        pos_x = x;
        pos_y = y;
    }

    @Override
    public void onScale(float scale) {
        nowScale = scale;
    }

    @Override
    public void onPosition(float x, float y) {
        diffx = Math.round(x - gestureImageView.getStartX());
        diffy = Math.round(y - gestureImageView.getStartY());
    }

    @Override
    public void onClick(View v) {
        if (!canClick){
            return;
        }
        Booth booth = null;
        for (Booth b : booths){
            RectF rect =  b.createRect(getActivity(),nowScale);
            rect.offset(diffx, diffy);
            if(rect.contains( pos_x, pos_y)){
                booth = b;
                break;
            }
        }
        if (null != booth){
            RealmResults<SchoolCity> schoolCityRealmResults = realm.where(SchoolCity.class).equalTo("siteNum", booth.mark).equalTo("joinCity", cityName).findAll();
            if (schoolCityRealmResults != null){
                List<SchoolCity> schoolCities  = realm.copyFromRealm(schoolCityRealmResults);
                if (null != schoolCities && !schoolCities.isEmpty()){
                    RealmQuery<University> where = realm.where(University.class).equalTo("chineseName", schoolCities.get(0).universityName);
                    for (int i = 1; i < schoolCities.size(); i++) {
                        where =  where.or().equalTo("chineseName", schoolCities.get(i).universityName);
                    }

                    RealmResults<University> all = where.findAll();
                    if (null != all){
                        List<University> universities = realm.copyFromRealm(all);
                        if (universities.size() > 1){
                            CommonUniversityListFragment.openCommonUniversityListFragment(getActivity(), universities);

                        }else if (universities.size() == 1){
                            UniversityDetailFragment.openUniversityDetailFragment(getActivity(), universities.get(0));
                        }
                    }

                }
            }

        }
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }
}
