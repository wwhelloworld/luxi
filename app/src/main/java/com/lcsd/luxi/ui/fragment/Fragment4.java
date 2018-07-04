package com.lcsd.luxi.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Commen_Info;
import com.lcsd.luxi.entity.FuWu_List_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.Frag03_recycle_adapter;
import com.lcsd.luxi.utils.LogUtil;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jie on 2018/5/15.
 */
public class Fragment4 extends BaseFragment {
    private Context mContext;
    private List<FuWu_List_Info.TContent.TRslist> list1;
    private int images[] = {R.drawable.img_topnews, R.drawable.img_weixin_best, R.drawable.img_history_today, R.drawable.img_movie, R.drawable.img_joke};
    private String titles[] = {"新闻头条", "微信精选", "历史的今天", "实时影讯", "轻松一刻"};
    private Frag03_recycle_adapter adapter;
    private List<Commen_Info> list;


    @BindView(R.id.recycle_frag04)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_frag04)
    PtrClassicFrameLayout refresh;
    @BindView(R.id.statu_frag04)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment04;
    }

    @Override
    protected void initData() {
        mContext = getActivity();
        list1 = new ArrayList<>();
        statusView.showLoading();
        requestFuWu(0);
        list = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            Commen_Info commenInfo = new Commen_Info();
            commenInfo.setImage(images[i]);
            commenInfo.setTitle(titles[i]);
            list.add(commenInfo);
        }

        adapter = new Frag03_recycle_adapter(mContext, list1, list);
        //适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        //刷新
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestFuWu(1);
            }
        });


    }

    @Override
    protected void setListener() {
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestFuWu(0);
            }
        });

    }

    private void requestFuWu(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "liveServer");
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        LogUtil.d(response);
                        if (i == 1) {
                            list1.clear();
                        }
                        FuWu_List_Info info = JSON.parseObject(response, FuWu_List_Info.class);
                        if (info.getContent().getRslist() != null && info.getContent().getRslist().size() > 0) {
                            list1.addAll(info.getContent().getRslist());
                        }
                        adapter.notifyDataSetChanged();
                        if (i == 1) {
                            refresh.refreshComplete();
                        }
                        if (list1.size() > 0) {
                            statusView.showContent();
                        } else {
                            statusView.showEmpty();
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
                        statusView.showNoNetwork();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
