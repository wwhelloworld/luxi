package com.lcsd.luxi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.XCZB_Info;
import com.lcsd.luxi.utils.GlideUtils;


import java.util.List;

/**
 * Created by wei on 2017/7/25.
 */
public class XCZB_adapter extends BaseAdapter {
    private Context context;
    private List<XCZB_Info.TContent.TRslist> list;

    public XCZB_adapter(Context context, List<XCZB_Info.TContent.TRslist> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        XCZBholder holder = null;
        if (view == null) {
            holder = new XCZBholder();
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_xczb, null);
            holder.imageView = view.findViewById(R.id.iv_item_listview_xczb);
            holder.textView =  view.findViewById(R.id.tv_item_listview_xczb1);
            holder.textView2 = view.findViewById(R.id.iv_item_listview_xczb2);

            view.setTag(holder);
        } else {
            holder = (XCZBholder) view.getTag();
        }
        if (list != null && list.size() > 0) {
            holder.textView.setText(list.get(i).getTitle());
            GlideUtils.loadbig(list.get(i).getThumb(),holder.imageView);
            if (list.get(i).getZbstatus().equals("1")) {
                holder.textView2.setText("直播");
            } else if (list.get(i).getZbstatus().equals("2")) {
                holder.textView2.setText("直播");
            } else if (list.get(i).getZbstatus().equals("3")) {
                holder.textView2.setText("回顾");
            } else if (list.get(i).getZbstatus().equals("4")) {
                holder.textView2.setText("筹划");
            }

        }
        return view;
    }

    class XCZBholder {
        ImageView imageView;
        TextView textView, textView2;
    }
}
