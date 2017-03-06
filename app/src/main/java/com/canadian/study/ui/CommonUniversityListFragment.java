package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canadian.study.R;
import com.canadian.study.bean.University;
import com.canadian.study.ui.adapter.OnItemClickListener;
import com.canadian.study.ui.adapter.UniversityItemAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zheng on 16/9/12.
 */

public class CommonUniversityListFragment extends Fragment implements OnItemClickListener {

    public static void openCommonUniversityListFragment(FragmentActivity activity, List<University> universitys){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("universitys", new ArrayList<>(universitys));
        activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                Fragment.instantiate(activity, CommonUniversityListFragment.class.getName(),bundle))
                .addToBackStack("university").commit();
    }

    private ArrayList<University> mUniversities;
    private UniversityItemAdapter mUniversityItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_university_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.backImage).setOnClickListener(view -> {
            if (getActivity() != null) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (getArguments() != null) {
            mUniversities = getArguments().getParcelableArrayList("universitys");
        }

        if (mUniversities != null) {
            mUniversityItemAdapter = new UniversityItemAdapter(this);
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
            recyclerView.setAdapter(mUniversityItemAdapter);
            recyclerView.setHasFixedSize(true);
            Observable.just(mUniversities).subscribe(mUniversityItemAdapter);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        University item = mUniversityItemAdapter.getItem(position);
        mUniversityItemAdapter.setSelectPosition(position);
        UniversityDetailFragment.openUniversityDetailFragment(getActivity(), item);
    }
}
