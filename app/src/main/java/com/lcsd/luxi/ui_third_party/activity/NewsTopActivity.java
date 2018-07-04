package com.lcsd.luxi.ui_third_party.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.ui.activity.BaseActivity;
import com.lcsd.luxi.ui_third_party.entity.NewsTitle;
import com.lcsd.luxi.ui_third_party.fragment.Fragment_newstop;
import com.lcsd.luxi.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsTopActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.top_psts_tab)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.top_view_pager)
    ViewPager pager;
    @BindView(R.id.newstop_top_view)
    View mView;
    private MyPagerAdapter adapter;
    private List<NewsTitle> entities = new ArrayList<>();
    private Context mContext;
    private String[] titles = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    private String[] types = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};



    @Override
    protected int setLayoutId() {
        return R.layout.activity_news_top;
    }
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
        ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
    }
    @Override
    protected void initData() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        tv_title.setText("新闻头条");
        pager.setOffscreenPageLimit(9);
        for (int i = 0; i < titles.length; i++) {
            NewsTitle newsTitle = new NewsTitle();
            newsTitle.setTitle(titles[i]);
            newsTitle.setIdentifier(types[i]);
            entities.add(newsTitle);
        }
        adapter = new MyPagerAdapter(getSupportFragmentManager(), entities);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
        setTabsValue();
        adapter.notifyDataSetChanged();
    }


    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        tabs.setDividerColor(getResources().getColor(R.color.transparent));
        // 设置分割线的上下的间距,传入的是dp
        tabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        tabs.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        tabs.setUnderlineColor(getResources().getColor(R.color.transparent));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        tabs.setIndicatorHeight(2);//滑动线的厚度
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.orange));

        // 设置Tab标题文字的大小,传入的是dp
        tabs.setTextSize(15);
        // 设置选中Tab文字的颜色
        tabs.setSelectedTextColor(getResources().getColor(R.color.orange));
        //设置正常Tab文字的颜色
        tabs.setTextColor(getResources().getColor(R.color.touming_gray));

        //  设置点击Tab时的背景色
        tabs.setTabBackground(R.drawable.background_tab);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        tabs.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.3倍
        tabs.setZoomMax(0.1F);//增大倍数
        //设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(10);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<NewsTitle> list;

        public MyPagerAdapter(FragmentManager fm, List<NewsTitle> list) {
            super(fm);
            this.list = list;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("identifier", list.get(position).getIdentifier());
            bundle.putString("title", list.get(position).getTitle());
            return Fragment_newstop.getInstance(bundle);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
        }
    }
}