package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.University;
import com.canadian.study.common.Constants;
import com.canadian.study.ui.views.ImageLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by zheng on 16/9/8.
 */

public class ZoneTypeFragment extends Fragment {


    private int[] mapButtonIds = {R.id.map_button_1 , R.id.map_button_2, R.id.map_button_3, R.id.map_button_4,
            R.id.map_button_5, R.id.map_button_6, R.id.map_button_7
            , R.id.map_button_8};

    private TextView floatTextView;
    private List<University> universities;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zone_type_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        floatTextView = (TextView) getView().findViewById(R.id.floatTextView);
        getView().findViewById(R.id.imageLayout).setOnClickListener(view -> {
            floatTextView.setVisibility(View.GONE);
            universities = null;
        });
        for (int i = 0; i < mapButtonIds.length; i++) {
            View mapButton = getView().findViewById(mapButtonIds[i]);
            mapButton.setTag(i);
            mapButton.setOnClickListener(view -> {
                int index = (int) mapButton.getTag();
                floatTextView.setVisibility(View.GONE);
                floatTextView.setAnimation(createAnimation());
                universities = getUniversityListCount(Constants.zonenames[index]);
                floatTextView.setText(getActivity().getString(R.string.join_school_1, Constants.zonenames[index] + " (" + universities.size() + ") "));
                ImageLayout.LayoutParams buttonLayout = ((ImageLayout.LayoutParams) view.getLayoutParams());
                ImageLayout.LayoutParams layoutParams = new ImageLayout.LayoutParams();
                layoutParams.centerY = buttonLayout.centerY - 60;
                layoutParams.left = buttonLayout.left -10;
                floatTextView.setLayoutParams(layoutParams);
                floatTextView.setVisibility(View.VISIBLE);
                floatTextView.setOnClickListener(view1 -> {
                    if (getActivity() != null) {
                       CommonUniversityListFragment.openCommonUniversityListFragment(getActivity(), universities);
                    }
                });
            });
        }



    }



    private List<University> getUniversityListCount(String name){
        List<University> universities = new ArrayList<>();
        if (!TextUtils.isEmpty(name)){
            Realm realm = Realm.getDefaultInstance();
            RealmResults<University> all = realm.where(University.class).equalTo("zone", name).equalTo("isJoinShow", true).findAll();
            if (all != null) {
                universities = realm.copyFromRealm(all);
            }

        }
        return universities;
    }

    private AnimationSet createAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f ,TranslateAnimation.RELATIVE_TO_SELF,0.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(500);
        return animationSet;
    }
}
