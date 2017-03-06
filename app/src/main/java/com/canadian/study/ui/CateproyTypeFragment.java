package com.canadian.study.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
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

public class CateproyTypeFragment extends Fragment implements OnItemClickListener {

    private UniversityItemAdapter universityItemAdapter;

    private int[] tabIds = {R.id.tabView1, R.id.tabView2, R.id.tabView3};
    private TextView[] tabViews = new TextView[3];
    private int selectedTabIndex = 0;
    private String[] cateproyNames = {"高等教育机构", "中小学", "私立机构"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cateproy_type_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        universityItemAdapter = new UniversityItemAdapter(this);
        recyclerView.setAdapter(universityItemAdapter);
        recyclerView.setHasFixedSize(true);

        for (int i = 0; i < tabIds.length; i++) {
            tabViews[i] = (TextView) getView().findViewById(tabIds[i]);
            tabViews[i].setTag(i);
            tabViews[i].setOnClickListener(view -> {
                int index = (int) view.getTag();
                if (selectedTabIndex != index){
                    changeTabBackground(index);
                    selectedTabIndex = index;
                    queryUniversityListObservable();
                }
            });
        }
        queryUniversityListObservable();
    }

    private void queryUniversityListObservable() {

        Observable.just(DatasUtils.sUniversities)
                .flatMap(new Func1<List<University>, Observable<List<University>>>() {
                    @Override
                    public Observable<List<University>> call(List<University> universities) {
                        List<University> results = new ArrayList<>();
                        for (University university : universities) {
                            if (university.type.equals(cateproyNames[selectedTabIndex])) {
                                results.add(university);
                            }
                        }
                        return Observable.just(results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(universities -> {
                    if (universities != null) {
                        Observable.just(universities).subscribe(universityItemAdapter);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }


    private void changeTabBackground(int index) {
        for (int i = 0; i < tabViews.length; i++) {
            if (i == index){
                tabViews[i].setBackgroundColor(Color.WHITE);
                tabViews[i].setTextColor(Color.BLACK);
            }else {
                tabViews[i].setBackgroundColor(Color.TRANSPARENT);
                tabViews[i].setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        University item = universityItemAdapter.getItem(position);
        universityItemAdapter.setSelectPosition(position);
        UniversityDetailFragment.openUniversityDetailFragment(getActivity(), item);
    }
}
