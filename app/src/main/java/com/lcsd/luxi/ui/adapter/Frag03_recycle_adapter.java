package com.lcsd.luxi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Commen_Info;
import com.lcsd.luxi.entity.FuWu_List_Info;
import com.lcsd.luxi.permissions.PerimissionsCallback;
import com.lcsd.luxi.permissions.PermissionEnum;
import com.lcsd.luxi.permissions.PermissionManager;
import com.lcsd.luxi.ui.activity.NewsDetialActivity;
import com.lcsd.luxi.ui.activity.WebviewActivity;
import com.lcsd.luxi.ui_third_party.activity.JokesActivity;
import com.lcsd.luxi.ui_third_party.activity.LsActivity;
import com.lcsd.luxi.ui_third_party.activity.NewsTopActivity;
import com.lcsd.luxi.ui_third_party.activity.WXJXActivity;
import com.lcsd.luxi.ui_third_party.activity.WepiaoActivity;
import com.lcsd.luxi.utils.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class Frag03_recycle_adapter extends RecyclerView.Adapter<Frag03_recycle_adapter.ViewHolder> {
    private Context mContext;
    private List<FuWu_List_Info.TContent.TRslist> list1;
    private List<Commen_Info> list2;

    public Frag03_recycle_adapter(Context mContext, List<FuWu_List_Info.TContent.TRslist> list1, List<Commen_Info> list2) {
        this.mContext = mContext;
        this.list1 = list1;
        this.list2 = list2;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag03_recycleview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.textView.setText("生活服务");
            holder.view.setVisibility(View.GONE);
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            holder.recyclerView.setLayoutManager(manager);
            FuWu_adapter adapter = new FuWu_adapter(list1, null);
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new FuWu_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mContext.startActivity(new Intent(mContext, WebviewActivity.class)
                            .putExtra("url", list1.get(position).getMarklinker())
                            .putExtra("title", list1.get(position).getTitle()));
                }
            });
        } else if (position == 1) {
            holder.view.setVisibility(View.VISIBLE);
            holder.textView.setText("休闲娱乐");
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            holder.recyclerView.setLayoutManager(manager);
            FuWu_adapter adapter = new FuWu_adapter(null, list2);
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new FuWu_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (list2.get(position).getTitle().equals("新闻头条")) {
                        mContext.startActivity(new Intent(mContext, NewsTopActivity.class));
                    } else if (list2.get(position).getTitle().equals("微信精选")) {
                        mContext.startActivity(new Intent(mContext, WXJXActivity.class));
                    } else if (list2.get(position).getTitle().equals("历史的今天")) {
                        mContext.startActivity(new Intent(mContext, LsActivity.class));

                    } else if (list2.get(position).getTitle().equals("实时影讯")) {
                        mContext.startActivity(new Intent(mContext, WepiaoActivity.class));

                    } else if (list2.get(position).getTitle().equals("轻松一刻")) {
                        mContext.startActivity(new Intent(mContext, JokesActivity.class));
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    /**
     * 服务的的viewholder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycle_item_frag03);
            textView = itemView.findViewById(R.id.tv_fuwu_head_title);
            view = itemView.findViewById(R.id.fuwu_view);

        }
    }
}
