package com.lcsd.luxi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;
import com.lcsd.luxi.refresh.LoopPagerAdapter;
import com.lcsd.luxi.refresh.RollPagerView;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;


/**
 * 上方广告适配器
 */
public class Banner_adapter extends LoopPagerAdapter {
    private Context context;
    private List<Frag01_list.THeadSlideNews> list;

    public Banner_adapter(Context context, List<Frag01_list.THeadSlideNews> list, RollPagerView viewPager) {
        super(viewPager);
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_view, null);
        ImageView imageView = view.findViewById(R.id.iv_item_lunbo);
        TextView textView = view.findViewById(R.id.tv_item_lunbo);
        GlideUtils.loadbig(list.get(position).getThumb(), imageView);
        textView.setText(list.get(position).getTitle());
        return view;
    }


    @Override
    public int getRealCount() {
        return list.size();
    }
}