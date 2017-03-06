package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.SchoolCity;
import com.canadian.study.bean.University;
import com.canadian.study.common.DatasUtils;
import com.canadian.study.ui.adapter.OnItemClickListener;
import com.canadian.study.ui.adapter.UniversityItemAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zheng on 16/9/12.
 */

public class CityTypeFragment extends Fragment implements OnItemClickListener {

    private int[] tabViewIds = {R.id.beijingTabView, R.id.shanghaiTabView, R.id.guangzhouTabView, R.id.chengduTabView};

    private int[] tabViewNormalBackgroundIds = {R.drawable.beijing_icon_drawable_normal, R.drawable.shanghai_icon_drawable_normal,
            R.drawable.guangzhou_icon_drawable_normal, R.drawable.chengdu_icon_drawable_normal};

    private int[] tabViewSelectedBackgroundIds = {R.drawable.beijing_icon_drawable_selected, R.drawable.shanghai_icon_drawable_selected,
            R.drawable.guangzhou_icon_drawable_selected, R.drawable.chengdu_icon_drawable_selected};

    private String[] citynames = {"北京", "上海", "广州", "成都"};

    private TextView[] tabViews = new TextView[4];

    private UniversityItemAdapter universityItemAdapter;
    private int selectTabIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_type_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        universityItemAdapter = new UniversityItemAdapter(this);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(universityItemAdapter);

        for (int i = 0; i < tabViewIds.length; i++) {
            tabViews[i] = (TextView) getView().findViewById(tabViewIds[i]);
            tabViews[i].setTag(i);
            tabViews[i].setOnClickListener(view -> {
                int tag = (int) view.getTag();
                if (tag != selectTabIndex){
                    selectTabIndex = tag;
                    changeTabBackground(selectTabIndex);
                    queryUniversityList();
                }
            });
        }
        changeTabBackground(selectTabIndex);
        queryUniversityList();
    }

    void changeTabBackground(int i) {
        for (int i1 = 0; i1 < tabViews.length; i1++) {
            if (i1 == i){
                tabViews[i1].setCompoundDrawablesWithIntrinsicBounds(0, tabViewSelectedBackgroundIds[i1], 0, 0);
            }else {
                tabViews[i1].setCompoundDrawablesWithIntrinsicBounds(0, tabViewNormalBackgroundIds[i1],0 ,0);
            }
        }

    }

    private void queryUniversityList() {
        universityItemAdapter.setCityName(citynames[selectTabIndex]);
        Observable.just(DatasUtils.sSchoolCities).flatMap(new Func1<List<SchoolCity>, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(List<SchoolCity> schoolCities) {
                ArrayList<String> strings = new ArrayList<>();
                for (SchoolCity schoolCity : schoolCities) {
                    if (schoolCity.joinCity.equals(citynames[selectTabIndex])) {
                        strings.add(schoolCity.universityName);
                    }
                }
                return Observable.just(strings);
            }
        }).flatMap(new Func1<List<String>, Observable<List<University>>>() {
            @Override
            public Observable<List<University>> call(List<String> strings) {
                List<University> results = new ArrayList<>();
                Observable.from(DatasUtils.sUniversities).filter(university -> strings.contains(university.chineseName)).subscribe(university -> {
                    results.add(university);
                });
                return Observable.just(results);
            }
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(universities1 -> {
            if (universities1 != null) {
                Observable.just(universities1).subscribe(universityItemAdapter);
            }
        });


    }



    @Override
    public void onItemClick(View v, int position) {
        University item = universityItemAdapter.getItem(position);
        universityItemAdapter.setSelectPosition(position);
        UniversityDetailFragment.openUniversityDetailFragment(getActivity(), item);
    }
}
