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
import com.canadian.study.common.Constants;
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





    private UniversityItemAdapter universityItemAdapter;
    private int selectTabIndex = 0;
    private ViewGroup tablayout;

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
        tablayout = (ViewGroup) getView().findViewById(R.id.tablayout);
        assembleTabLayout();
        changeTabBackground(selectTabIndex);
        queryUniversityList();
    }

    void changeTabBackground(int i) {
        for (int i1 = 0; i1 < tablayout.getChildCount(); i1++) {
            TextView childAt = (TextView) tablayout.getChildAt(i1);
            if (i1 == i){
                childAt.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewSelectedBackgroundIds[i1], 0, 0);
            }else {
                childAt.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewNormalBackgroundIds[i1],0 ,0);
            }
        }

    }


    private void assembleTabLayout() {

        for (int i = 0; i < Constants.citynames.length; i++) {
            TextView tabView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.city_tab_item_layout, null);
            tabView.setText(Constants.citynames[i]);
            tabView.setCompoundDrawablesWithIntrinsicBounds(0, Constants.tabViewNormalBackgroundIds[i], 0, 0);
            tabView.setTag(i);
            tabView.setPadding(72, 0, 72, 0);
            tabView.setOnClickListener(view -> {
                int tag = (int) view.getTag();
                if (tag != selectTabIndex){
                    selectTabIndex = tag;
                    changeTabBackground(selectTabIndex);
                    queryUniversityList();
                }
            });
            tablayout.addView(tabView);
        }
    }

    private void queryUniversityList() {
        universityItemAdapter.setCityName(Constants.citynames[selectTabIndex]);
        Observable.just(DatasUtils.sSchoolCities).flatMap(new Func1<List<SchoolCity>, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(List<SchoolCity> schoolCities) {
                ArrayList<String> strings = new ArrayList<>();
                for (SchoolCity schoolCity : schoolCities) {
                    if (schoolCity.joinCity.equals(Constants.citynames[selectTabIndex])) {
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
