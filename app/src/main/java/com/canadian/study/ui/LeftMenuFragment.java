package com.canadian.study.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;

/**
 * Created by zheng on 16/9/13.
 */

public class LeftMenuFragment extends Fragment {

    private View[] menuViews;

    public void setSelectMenu(int index) {
        for (int i1 = 0; i1 < menuViews.length; i1++) {
            TextView textView = (TextView) menuViews[i1];
            if (i1 == index){
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.project_ee3223));
            }else {
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(Color.BLACK);
            }
        }
    }

    public interface OnMenuClickListener{
        void onMenuClick(int index);
    }

    private OnMenuClickListener mOnMenuClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_menu_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menuViews = new View[]{getView().findViewById(R.id.listTab1),
                getView().findViewById(R.id.listTab2),
                getView().findViewById(R.id.listTab3),
                getView().findViewById(R.id.listTab4)};

        for (int i = 0; i < menuViews.length; i++) {
            menuViews[i].setTag(i);
            menuViews[i].setOnClickListener(view -> {
                int index = (int) view.getTag();
                setSelectMenu(index);
                if (null != mOnMenuClickListener){
                    mOnMenuClickListener.onMenuClick(index);
                }

            });
        }

    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }
}
