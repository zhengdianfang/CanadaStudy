package com.canadian.study.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by zheng on 16/6/16.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter implements Observer<List<T>> {

    protected OnItemClickListener mOnItemClickListener;

    public BaseListAdapter(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected List<T> datas = new ArrayList<>();

    private boolean isLoadMore = false;

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    public void onDestory(){

    }

    @Override
    public void onNext(List<T> ts) {
        if (!isLoadMore){
            datas.clear();
        }
        if(null != ts){
            datas.addAll(ts);
        }

        notifyDataSetChanged();
    }

    public T getItem(int pos){
        return datas.get(pos);
    }

    public boolean isCanLoadmore(){
        int m = datas.size() % 20;
        return datas.size() != 0 && m == 0;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
           holder.itemView.setOnClickListener(v -> {
               mOnItemClickListener.onItemClick(v, position);
           });
        }
    }



}
