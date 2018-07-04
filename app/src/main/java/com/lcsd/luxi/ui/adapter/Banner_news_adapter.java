package com.lcsd.luxi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.News_Info;
import com.lcsd.luxi.refresh.LoopPagerAdapter;
import com.lcsd.luxi.refresh.RollPagerView;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

/**
 * 上方广告适配器
 */
public class Banner_news_adapter extends LoopPagerAdapter {
    private List<News_Info.TContent.THd_list> list;

    public Banner_news_adapter(List<News_Info.TContent.THd_list> list, RollPagerView viewPager) {
        super(viewPager);
        this.list = list;

    }

    @Override
    public View getView(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_view, null);
        ImageView imageView = view.findViewById(R.id.iv_item_lunbo);
        TextView textView = view.findViewById(R.id.tv_item_lunbo);
        GlideUtils.load(list.get(position).getThumb(), imageView);
        textView.setText(list.get(position).getTitle());
        return view;
    }


    @Override
    public int getRealCount() {
        return list.size();
    }
}