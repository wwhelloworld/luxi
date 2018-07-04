package com.lcsd.luxi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.AboutUs_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.NewsDetialActivity;
import com.lcsd.luxi.ui.adapter.AboutUs_adapter;
import com.lcsd.luxi.utils.LogUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class Fragment_aboutus extends BaseV4Fragment {

    private Context mContext;
    private String key, mark, url;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    private List<AboutUs_Info.TContent.TRslist> list;
    private AboutUs_adapter adapter;

    @BindView(R.id.listivew_news_aboutus)
    ListView listView;
    @BindView(R.id.refresh_aboutus)
    PtrClassicFrameLayout refresh;
    @BindView(R.id.aboutus_empty)
    LinearLayout xwzx_empty;
    @BindView(R.id.webview_aboutus)
    WebView webView;


    public static Fragment getInstance(Bundle bundle) {
        Fragment_aboutus fragment = new Fragment_aboutus();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_aboutus;
    }

    @Override
    protected void initData() {
        mContext = getActivity();
        key = getArguments().getString("key");
        mark = getArguments().getString("mark");
        url = getArguments().getString("linkurl");
        Log.d("获取的key===", key);
        //加载数据
        list = new ArrayList<>();
        if (key != null) {
            requestNews(0);
        }
        /**判断进入的时候加载的为哪一个*/
        if (url != null && url.length() > 0) {
            webView.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        } else {
            webView.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);

        }
        /**加载webview*/
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //加载url
        if (url != null) {
            webView.loadUrl(url);
        }
        /**加载列表时候*/
        adapter = new AboutUs_adapter(mContext, list);
        listView.setAdapter(adapter);
        //刷新操作
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid >= total) {
                    refresh.refreshComplete();
                } else {
                    pageid++;
                    requestNews(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestNews(1);
            }
        });
    }

    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, NewsDetialActivity.class)
                        .putExtra("url", list.get(position).getUrl())
                        .putExtra("title", list.get(position).getTitle()));
            }
        });
    }

    /**
     * 新闻资讯请求
     */
    private void requestNews(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mark);
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
                    LogUtil.d(response);
                    if (i == 1) {
                        list.clear();
                        pageid = 1;
                    }
                    AboutUs_Info info = JSON.parseObject(response, AboutUs_Info.class);
                    if (info.getContent().getRslist() != null && info.getContent().getRslist().size() > 0) {
                        list.addAll(info.getContent().getRslist());
                    }
                    adapter.notifyDataSetChanged();
                    if (i == 1 || i == 2) {
                        refresh.refreshComplete();
                    }
                    if (info.getContent().getTotal() != null) {
                        total = info.getContent().getTotal();
                    }
                    if (list.size() > 0) {
                        xwzx_empty.setVisibility(View.GONE);
                        refresh.setVisibility(View.VISIBLE);
                    } else {
                        xwzx_empty.setVisibility(View.VISIBLE);
                        refresh.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }


}
