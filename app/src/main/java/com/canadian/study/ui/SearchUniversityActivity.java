package com.canadian.study.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.canadian.study.R;
import com.canadian.study.bean.University;
import com.canadian.study.common.Constants;
import com.canadian.study.common.DatasUtils;
import com.canadian.study.ui.adapter.OnItemClickListener;
import com.canadian.study.ui.adapter.SearchUniversityItemAdapter;
import com.canadian.study.ui.views.PinnedHeaderItemDecoration;
import com.canadian.study.ui.views.QuickSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zheng on 16/9/12.
 */

public class SearchUniversityActivity extends AppCompatActivity implements OnItemClickListener, QuickSearchBar.OnClickListener {

    private SearchUniversityItemAdapter mSearchUniversityItemAdapter;
    private RecyclerView recyclerView;
    private Map<String, Integer> positionMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_university_layout);

        findViewById(R.id.backImage).setOnClickListener(view -> {
          finish();
        });
        mSearchUniversityItemAdapter = new SearchUniversityItemAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.universityListview);
        recyclerView.addItemDecoration(new PinnedHeaderItemDecoration());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mSearchUniversityItemAdapter);

        EditText editText = (EditText) findViewById(R.id.searchKeyView);
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH){
                String s = textView.getText().toString();
                if (!TextUtils.isEmpty(s)){
                    query(s);
                    Constants.closeSoftKey(SearchUniversityActivity.this);
                }
            }
            return true;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                query(editable.toString());
            }
        });

        QuickSearchBar quickSearchbar = ((QuickSearchBar) findViewById(R.id.quickSearchbar));
        quickSearchbar.setOnClickListener(this);
        query("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void query(String keychar){
        Observable.just(getUniversityList(keychar)).debounce(500, TimeUnit.MILLISECONDS)
                .map(universities -> {
                    List<SearchUniversityItemAdapter.ItemData> itemDatas = new ArrayList<>();
                    positionMap.clear();
                    for (int i = 0; i < universities.size(); i++) {
                        University university = universities.get(i);
                        if (i != 0){
                            University last = universities.get(i - 1);
                            if (!last.pingyingIndex.equals(university.pingyingIndex)){

                                itemDatas.add(new SearchUniversityItemAdapter.ItemData(SearchUniversityItemAdapter.ItemData.TYPE_SELECTION,
                                        university.pingyingIndex, null));
                                positionMap.put(university.pingyingIndex, itemDatas.size() - 1);
                            }
                            itemDatas.add(new SearchUniversityItemAdapter.ItemData(SearchUniversityItemAdapter.ItemData.TYPE_DATA,
                                    university.pingyingIndex, university));
                        }else {
                            positionMap.put(university.pingyingIndex, 0);
                            itemDatas.add(new SearchUniversityItemAdapter.ItemData(SearchUniversityItemAdapter.ItemData.TYPE_SELECTION,
                                    university.pingyingIndex, null));
                            itemDatas.add(new SearchUniversityItemAdapter.ItemData(SearchUniversityItemAdapter.ItemData.TYPE_DATA,
                                    university.pingyingIndex, university));
                        }
                    }
                    return itemDatas;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemDatas -> {
                    Observable.just(itemDatas).subscribe(mSearchUniversityItemAdapter);
                });

    }

    public List<University> getUniversityList(String keychar){

        if (TextUtils.isEmpty(keychar)){
            return DatasUtils.sUniversities;
        }else {
            ArrayList<University> results = new ArrayList<>();
            Observable.from(DatasUtils.sUniversities).filter(university -> university.chineseName.contains(keychar) || university.englishName.contains(keychar)
                    ||university.pingyingIndex.contains(keychar)).subscribe(university -> {
                results.add(university);
            });

            return results;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        SearchUniversityItemAdapter.ItemData item = mSearchUniversityItemAdapter.getItem(position);
        if (item.university != null){
            mSearchUniversityItemAdapter.setSelectPosition(position);
            UniversityDetailFragment.openUniversityDetailFragment(this, item.university);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(char c, int position) {
        String s = String.valueOf(c);
        if (!TextUtils.isEmpty(s)){
            Integer pos = positionMap.get(s.toLowerCase());
            if (pos != null){
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(pos, 0);
            }
        }
    }
}
