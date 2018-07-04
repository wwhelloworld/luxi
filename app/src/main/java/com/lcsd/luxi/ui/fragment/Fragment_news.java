package com.lcsd.luxi.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.News_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.refresh.OnItemClickListener;
import com.lcsd.luxi.refresh.RollPagerView;
import com.lcsd.luxi.ui.activity.NewsDetialActivity;
import com.lcsd.luxi.ui.adapter.Banner_news_adapter;
import com.lcsd.luxi.ui.adapter.News_adapter;
import com.lcsd.luxi.view.MultipleStatusView;
import com.lcsd.luxi.view.ScrollViewWithListView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class Fragment_news extends BaseFragment {

    private Context mContext;
    private String key, mark;
    private List<News_Info.TContent.THd_list> list1;
    private List<News_Info.TContent.TRslist> list2;
    private News_adapter adapter;
    private Banner_news_adapter banner;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    @BindView(R.id.lv_news)
    ScrollViewWithListView listView;
    @BindView(R.id.banner_news)
    RollPagerView rollPagerView;
    @BindView(R.id.scrollview_news)
    ScrollView scrollView;
    @BindView(R.id.statu_news_list)
    MultipleStatusView statusView;
    @BindView(R.id.refresh_news)
    PtrClassicFrameLayout refresh;


    public static Fragment getInstance(Bundle bundle) {
        Fragment_news fragment = new Fragment_news();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initData() {
        mContext = getActivity();
        key = getArguments().getString("key");
        mark = getArguments().getString("mark");
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        //不显示顶部
        scrollView.smoothScrollTo(0, 20);

        statusView.showLoading();
        requestnews(0);
        //轮播
        banner = new Banner_news_adapter(list1, rollPagerView);
        rollPagerView.setparentTouch(refresh);
        rollPagerView.setAdapter(banner);
        rollPagerView.setAnimationDurtion(2000);
        rollPagerView.setHintPadding(0, 0, 0, 20);
        //列表
        adapter = new News_adapter(mContext, list2);
        listView.setAdapter(adapter);
        listView.setFocusable(false);//去除焦点


    }

    @Override
    protected void setListener() {
        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(mContext, NewsDetialActivity.class)
                        .putExtra("url", list1.get(position).getUrl())
                        .putExtra("title", list1.get(position).getTitle()));

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, NewsDetialActivity.class)
                        .putExtra("url", list2.get(position).getUrl())
                        .putExtra("title", list2.get(position).getTitle()));

            }
        });
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.disableWhenHorizontalMove(true);
        refresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid < total) {
                    pageid++;
                    requestnews(2);
                } else {
                    requestnews(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestnews(1);

            }
        });
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestnews(0);
            }
        });

    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {
//            refresh.refreshComplete();
//        }
//    }

    private void requestnews(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "news");
        map.put("cate", key);
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
                    try {
                        if (i == 1) {
                            list1.clear();
                            list2.clear();
                            pageid = 1;
                        }
                        if (i == 0 || i == 1) {
                            News_Info info = JSON.parseObject(response, News_Info.class);
                            if (info.getContent().getHd_list().size() > 0) {
                                list1.addAll(info.getContent().getHd_list());
                            }
                            if (info.getContent().getRslist().size() > 0) {
                                list2.addAll(info.getContent().getRslist());
                            }
                            if (info.getContent().getTotal() != null) {
                                total = info.getContent().getTotal();
                            }
                            rollPagerView.notifydata(banner);
                            adapter.notifyDataSetChanged();
                        }

                        if (list1.size() > 0 || list2.size() > 0) {
                            statusView.showContent();
                        } else {
                            statusView.showEmpty();
                        }
                        if (i == 1 || i == 2) {
                            refresh.refreshComplete();
                        }

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
                        if (i == 1 || i == 2) {
                            refresh.refreshComplete();
                        }
                        statusView.showNoNetwork();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


}
