package com.canadian.study.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.University;
import com.canadian.study.ui.views.PinnedHeaderItemDecoration;

/**
 * Created by zheng on 16/9/12.
 */

public class SearchUniversityItemAdapter extends BaseListAdapter<SearchUniversityItemAdapter.ItemData> implements PinnedHeaderItemDecoration.PinnedHeaderAdapter {


    private UniversityItemAdapter mUniversityItemAdapter;

    public SearchUniversityItemAdapter(OnItemClickListener onItemClickListener) {
        super(onItemClickListener);
        mUniversityItemAdapter = new UniversityItemAdapter(onItemClickListener);
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        return viewType == ItemData.TYPE_SELECTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemData.TYPE_SELECTION) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_selection_layout, parent, false)) {};
        }
        return mUniversityItemAdapter.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ItemData.TYPE_SELECTION){
            ((TextView) holder.itemView).setText(datas.get(position).abc.toUpperCase());
        }else {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> {
                    mOnItemClickListener.onItemClick(v, position);
                });
            }
            mUniversityItemAdapter.onBindViewHolder(holder, position, datas.get(position).university);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    public void setSelectPosition(int position) {
        mUniversityItemAdapter.setSelectPosition(position);
        notifyDataSetChanged();
    }

    public static class ItemData{

        public static final int TYPE_SELECTION = 0;
        public static final int TYPE_DATA = 1;

        public int type;
        public String abc;
        public University university;

        public ItemData(int type, String abc, University university) {
            this.type = type;
            this.abc = abc;
            this.university = university;
        }
    }
}
