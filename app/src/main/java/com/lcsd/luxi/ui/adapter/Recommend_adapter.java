package com.lcsd.luxi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;

import java.util.List;


public class Recommend_adapter extends BaseAdapter {
    private List<Frag01_list.TVideoList> list;

    public Recommend_adapter(List<Frag01_list.TVideoList> list) {
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_tj, null);
            holder.textView = convertView.findViewById(R.id.tv_listview_item_tj);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (list.size() > 0) {
            holder.textView.setText(list.get(position).getTitle());
        }
        return convertView;
    }

    class Holder {
        TextView textView;
    }
}
