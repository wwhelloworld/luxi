package com.lcsd.luxi.ui_third_party.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.ui_third_party.entity.Ls2;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6.
 */
public class LsImgAdapter extends BaseAdapter{
    private Context context;
    private List<Ls2.TResult.TPicUrl> list;
    private LayoutInflater layoutInflater;
    public LsImgAdapter(Context context, List<Ls2.TResult.TPicUrl> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
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
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_ls_imglist, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.tv.setText(list.get(i).getPic_title());
        GlideUtils.loadnormoal(context,list.get(i).getUrl(),holder.iv);

        return view;
    }
    class ViewHolder {
        @BindView(R.id.item_ls_imglist_iv)
        ImageView iv;
        @BindView(R.id.item_ls_imglist_tv)
        TextView tv;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}