package com.canadian.study.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canadian.study.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleImageFragment extends Fragment {


    public SimpleImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_image, container, false);
    }

}
