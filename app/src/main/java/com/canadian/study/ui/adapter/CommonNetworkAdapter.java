package com.canadian.study.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canadian.study.R;
import com.canadian.study.bean.NetData;

/**
 * Created by zheng on 16/9/8.
 */

public class CommonNetworkAdapter extends BaseListAdapter<NetData> {

    public CommonNetworkAdapter(OnItemClickListener onItemClickListener) {
        super(onItemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_networik_item_layout, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TextView netNameView = ((TextView) holder.itemView.findViewById(R.id.netNameView));
        TextView netUrlView = ((TextView) holder.itemView.findViewById(R.id.netUrlView));
        NetData netData = datas.get(position);
        netNameView.setText(netData.name);
        netUrlView.setText(netData.url);
        final String url = netData.url;
        netUrlView.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(url) &&  url.startsWith("http")){
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                holder.itemView.getContext().startActivity(it);
            }
        });
    }
}
