
package com.lcsd.luxi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.AboutUs_Info;
import com.lcsd.luxi.utils.DateUtils;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

public class AboutUs_adapter extends BaseAdapter {
    private Context mContext;
    private List<AboutUs_Info.TContent.TRslist> list;

    public AboutUs_adapter(Context mContext, List<AboutUs_Info.TContent.TRslist> list) {
        this.mContext = mContext;
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
        XWZXHolder holder = null;
        if (convertView == null) {
            holder = new XWZXHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_common, null);
            holder.imageView = convertView.findViewById(R.id.iv_item_common);
            holder.textView1 = convertView.findViewById(R.id.tv_item_common1);
            holder.textView2 = convertView.findViewById(R.id.tv_item_common2);
            holder.textView3 = convertView.findViewById(R.id.tv_item_common3);
            holder.textView4 = convertView.findViewById(R.id.tv_item_common4);

            convertView.setTag(holder);
        } else {
            holder = (XWZXHolder) convertView.getTag();
        }
        if (list.size() > 0) {
            GlideUtils.loadnormoal(mContext, list.get(position).getThumb(), holder.imageView);
            holder.textView1.setText(list.get(position).getTitle());
            holder.textView2.setText(list.get(position).getSource());
            holder.textView3.setText(list.get(position).getWriter());
            holder.textView4.setText(DateUtils.timeStampDate(list.get(position).getDateline()));
        }
        return convertView;
    }

    class XWZXHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

    }
}
