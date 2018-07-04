package com.lcsd.luxi.ui.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.AboutUs_Head_Info;
import com.lcsd.luxi.entity.XWZX_Head_Info;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.AboutUs_Head_adapter;
import com.lcsd.luxi.ui.adapter.XWZX_Head_adapter;
import com.lcsd.luxi.utils.LogUtil;
import com.lcsd.luxi.view.MultipleStatusView;
import com.lcsd.luxi.view.PagerSlidingTabStrip;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {
    private Context mContext;
    private String url, title, mark;
    private List<AboutUs_Head_Info.TContent> list;
    private AboutUs_Head_adapter adapter;
    @BindView(R.id.psts_tab_aboutus)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.aboutus_view_pager)
    ViewPager viewPager;
    @BindView(R.id.aboutus_top_view)
    View mView;
    @BindView(R.id.tv_aboutus_title)
    TextView tv_title;
    @BindView(R.id.ll_aboutus_back)
    LinearLayout ll_back;
    @BindView(R.id.statu_aboutus)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about_us;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
        ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
    }
    @Override
    protected void initData() {
        mContext = this;
        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            mark = getIntent().getStringExtra("mark");
        }
        if (title != null) {
            tv_title.setText(title);
        }
        list = new ArrayList<>();
        viewPager.setOffscreenPageLimit(3);
        statusView.showLoading();
        if (url != null) {
            requestNews();
        }
    }

    @Override
    protected void setListener() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestNews();
            }
        });
    }


    private void requestNews() {
        MyApplication.getInstance().getmMyOkHttp().post(mContext, url, null, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {

                    LogUtil.d(response);
                    AboutUs_Head_Info info = JSON.parseObject(response, AboutUs_Head_Info.class);
                    if (info.getContent() != null && info.getContent().size() > 0) {
                        list.addAll(info.getContent());
                    }
                    if (list.size() > 0) {
                        adapter = new AboutUs_Head_adapter(getSupportFragmentManager(), list, mark);
                        viewPager.setAdapter(adapter);
                        tabs.setViewPager(viewPager);
                        viewPager.setCurrentItem(0);
                        setTabsValue();
                        statusView.showContent();
                    } else {
                        statusView.showEmpty();
                    }
                    adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        e.printStackTrace();
                        statusView.showError();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (error_msg!=null){
                    try {
                        statusView.showNoNetwork();

                    }catch (Exception e){
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
        tabs.setShouldExpand(true);

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
        tabs.setTabPaddingLeftRight(10);
    }
}