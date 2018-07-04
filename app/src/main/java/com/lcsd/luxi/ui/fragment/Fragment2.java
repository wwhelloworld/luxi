package com.lcsd.luxi.ui.fragment;


import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.News_Head_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.News_Head_adapter;
import com.lcsd.luxi.utils.LogUtil;
import com.lcsd.luxi.view.MultipleStatusView;
import com.lcsd.luxi.view.PagerSlidingTabStrip;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jie on 2018/5/15.
 */
public class Fragment2 extends BaseFragment {

    private List<News_Head_Info.TContent> list;
    private News_Head_adapter adapter;

    @BindView(R.id.psts_tab_news)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.news_view_pager)
    ViewPager viewPager;
    @BindView(R.id.statu_news)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment02;
    }

    @Override
    protected void initData() {
        super.initData();
        list = new ArrayList<>();
        statusView.showLoading();
        requestNewsHead();
        viewPager.setOffscreenPageLimit(5);
    }

    @Override
    protected void setListener() {
        super.setListener();
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestNewsHead();
            }
        });
    }

    private void requestNewsHead() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "newsClass");
        MyApplication.getInstance().getmMyOkHttp().post(mActivity, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {

                        LogUtil.d(response);
                        News_Head_Info info = JSON.parseObject(response, News_Head_Info.class);
                        if (info.getContent() != null && info.getContent().size() > 0) {
                            list.addAll(info.getContent());
                        }
                        if (list.size() > 0) {
                            adapter = new News_Head_adapter(getFragmentManager(), list);
                            viewPager.setAdapter(adapter);
                            tabs.setViewPager(viewPager);
                            viewPager.setCurrentItem(0);
                            setTabsValue();
                            statusView.showContent();
                        } else {
                            statusView.showEmpty();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        statusView.showError();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (error_msg != null) {
                    try {
                        statusView.showNoNetwork();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(false);
        //设置底部指示器和文字宽度一样
        // tabs.setIndicatorWrap(true);

        // 设置Tab的分割线的颜色
        tabs.setDividerColor(getResources().getColor(R.color.transparent));
        // 设置分割线的上下的间距,传入的是dp
        tabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        tabs.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        tabs.setUnderlineColor(getResources().getColor(R.color.gray2));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        tabs.setIndicatorHeight(2);//滑动线的厚度
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.orange));

        // 设置Tab标题文字的大小,传入的是dp
        tabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        tabs.setSelectedTextColor(getResources().getColor(R.color.orange));
        //设置正常Tab文字的颜色
        tabs.setTextColor(getResources().getColor(R.color.gray));

        //  设置点击Tab时的背景色
        tabs.setTabBackground(R.drawable.background_tab);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        tabs.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.3倍
        tabs.setZoomMax(0.05F);//增大倍数
        //设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(11);
    }

}
