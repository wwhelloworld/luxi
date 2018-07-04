package com.lcsd.luxi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;
import com.lcsd.luxi.entity.Frag01_news;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.SearchActivity;
import com.lcsd.luxi.ui.adapter.Frag01_recycle_adapter;
import com.lcsd.luxi.view.BetterPtrFrameLayout;
import com.lcsd.luxi.view.CustomVRecyclerView;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jie on 2018/5/15.
 */
public class Fragment1 extends BaseFragment {
    private Context mContext;
    //json
    private List<Frag01_list.THeadSlideNews> bannerlist;
    private List<Frag01_list.TTopNewslist> toplist;
    private List<Frag01_list.TVideoList> recommendlist;
    private List<Frag01_list.TNewxmlbList> lanmulist;
    private List<Frag01_list.TColumnClick> videolist;
    private List<Frag01_news.TContent.TRslist> newslist;
    //刷新加载
    private int total;
    private int pageid = 1;
    private int psize = 10;

    //栏目的列表
    private Frag01_recycle_adapter adapter;


    @BindView(R.id.f1_et_sousuo)
    EditText mEditText;
    @BindView(R.id.recycler_frag01_lanmu)
    CustomVRecyclerView recycler_frag01_lanmu;
    @BindView(R.id.refresh_frag01)
    BetterPtrFrameLayout refresh;
    @BindView(R.id.statu_frag01)
    MultipleStatusView statusView;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment01;
    }

    @Override
    protected void initData() {
        mContext = getActivity();
        mEditText.setFocusable(false);
        //
        lanmulist = new ArrayList<>();
        toplist = new ArrayList<>();
        recommendlist = new ArrayList<>();
        bannerlist = new ArrayList<>();
        videolist = new ArrayList<>();
        newslist = new ArrayList<>();
        //刷新操作
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.disableWhenHorizontalMove(true);
        refresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid == total) {
                    refresh.refreshComplete();
                } else {
                    pageid++;
                    requestNews(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestFragment1(1);
                requestNews(1);

            }
        });
        //设置布局管理器
        recycler_frag01_lanmu.setNestedScrollingEnabled(false);
        recycler_frag01_lanmu.setItemViewCacheSize(10);//设置缓存holder的数量，不然重新服用的时候回卡顿，同时不能过多，不然会造成内存过大
        recycler_frag01_lanmu.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new Frag01_recycle_adapter(mContext, bannerlist,toplist,recommendlist, lanmulist, videolist, newslist);
        recycler_frag01_lanmu.setAdapter(adapter);
        //请求数据
        statusView.showLoading();
        requestFragment1(0);
        requestNews(0);


    }

    @Override
    protected void setListener() {
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestFragment1(0);
                requestNews(0);
            }
        });
/**隐藏了搜索*/
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SearchActivity.class));
            }
        });
    }

    /**
     * 数据请求
     */
    private void requestFragment1(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "index");

        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {

                    if (i == 1) {
                        bannerlist.clear();
                        lanmulist.clear();
                        videolist.clear();
                    }
                    Log.d("返回的数据====", response);
                    Frag01_list info = JSON.parseObject(response, Frag01_list.class);
                    //轮播图
                    if (info.getHeadSlideNews() != null && info.getHeadSlideNews().size() > 0) {
                        bannerlist.addAll(info.getHeadSlideNews());
                    }
                    //热点滚动消息
                    if (info.getTopNewslist().size() > 0) {
                        toplist.addAll(info.getTopNewslist());
                    }

                    //视频推荐两条
                    if (info.getVideoList().size() > 0) {
                        recommendlist.addAll(info.getVideoList());
                    }
                    //栏目数据
                    if (info.getNewxmlbList() != null && info.getNewxmlbList().size() > 0) {
                        lanmulist.addAll(info.getNewxmlbList());
                    }
                    //视频点播栏目
                    if (info.getColumnClick() != null && info.getColumnClick().size() > 0) {
                        videolist.addAll(info.getColumnClick());
                    }


                    adapter.notifyDataSetChanged();
                    if (i == 1) {
                        refresh.refreshComplete();
                    }
                    if (bannerlist.size() > 0 || lanmulist.size() > 0 || videolist.size() > 0) {
                        statusView.showContent();
                    } else {
                        statusView.showEmpty();
                    }

                    }catch (Exception e){
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

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void requestNews(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "news");
        if (i == 1 || i == 0) {
            map.put("pageid", 1 + "");
        } else {
            map.put("pageid", pageid + "");
        }
        map.put("psize", psize + "");
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    if (i == 1) {
                        newslist.clear();

                    }
                    Frag01_news info = JSON.parseObject(response, Frag01_news.class);
                    if (info.getContent().getRslist() != null) {
                        newslist.addAll(info.getContent().getRslist());
                    }
                    adapter.notifyDataSetChanged();
                    if (i == 1 || i == 2) {
                        refresh.refreshComplete();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }


}
