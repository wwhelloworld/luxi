package com.lcsd.luxi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.DianBo_Info;
import com.lcsd.luxi.entity.ZhiBo_Info;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ZhiBo_adapter extends BaseAdapter {
    private Context context;
    private List<ZhiBo_Info.TContent> list;

    public ZhiBo_adapter(Context context, List<ZhiBo_Info.TContent> list) {
        this.context = context;
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
        DianBoHolder holder = null;
        if (convertView == null) {
            holder = new DianBoHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_dianbo, null);
            holder.circleImageView = convertView.findViewById(R.id.item_civ_dianbo);
            holder.textView = convertView.findViewById(R.id.item_tv_dianbo);
            convertView.setTag(holder);
        } else {
            holder = (DianBoHolder) convertView.getTag();
        }
        if (list.size() > 0) {
            GlideUtils.load(list.get(position).getIco(), holder.circleImageView);
            holder.textView.setText(list.get(position).getTitle());
        }
        return convertView;
    }

    class DianBoHolder {
        CircleImageView circleImageView;
        TextView textView;
    }
}
