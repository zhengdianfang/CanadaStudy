package com.canadian.study.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.University;

/**
 * Created by zheng on 16/9/12.
 */

public class UniversityItemAdapter extends BaseListAdapter<University> {

    private String cityName;
    private int selectPosition = -1;

    public UniversityItemAdapter(OnItemClickListener onItemClickListener) {
        super(onItemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_university_item_layout, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindViewHolder(holder, position, datas.get(position));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, University university){
        TextView enameView = (TextView) holder.itemView.findViewById(R.id.enameView);
        TextView cnameView = (TextView) holder.itemView.findViewById(R.id.cnameView);
        TextView cityNameView = (TextView) holder.itemView.findViewById(R.id.cityNameView);
        TextView typenameView = (TextView) holder.itemView.findViewById(R.id.typenameView);
        enameView.setText(university.englishName);
        cnameView.setText(university.chineseName);
        typenameView.setText("学校类型: " + university.type);
        if (TextUtils.isEmpty(cityName)){
            if (university.schoolCities != null) if (!university.schoolCities.isEmpty()) {
                cityNameView.setText("参展城市: " + university.schoolCities.get(0).joinCity);
            }
        }else {
            cityNameView.setText("参展城市: " + cityName);
        }


        if (selectPosition == position){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_dddddd));
        }else {
            if (position%2== 0){
                holder.itemView.setBackgroundResource(R.drawable.blue_selector);
            }else {
                holder.itemView.setBackgroundResource(R.drawable.white_selector);
            }
        }
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
