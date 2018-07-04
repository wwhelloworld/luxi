package com.lcsd.luxi.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.dl7.player.media.IjkPlayerView;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.DianBo_Info;
import com.lcsd.luxi.entity.ZB_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.DianBoListActivity;
import com.lcsd.luxi.ui.adapter.DianBo_adapter;
import com.lcsd.luxi.utils.GlideUtils;
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
public class Fragment3 extends BaseFragment {
    private Context mContext;
    private DianBo_adapter adapter;
    private List<DianBo_Info.TContent> list;
    public static IjkPlayerView zb_player;
    @BindView(R.id.gv_dianbo)
    GridView gridView;
    @BindView(R.id.statu_frag03)
    MultipleStatusView statusView;
    @BindView(R.id.refresh_frag03)
    PtrClassicFrameLayout refresh;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment03;
    }

    @Override
    protected void initData() {
        mContext = getActivity();
        statusView.showLoading();
        zb_player = getActivity().findViewById(R.id.ijk_zb_player);
        requestZD();//直播请求
        //点播请求
        requestDianBo(0);
        //填充适配器
        list = new ArrayList<>();
        adapter = new DianBo_adapter(mContext, list);
        gridView.setAdapter(adapter);
        //刷新
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestDianBo(1);
            }
        });

    }

    @Override
    protected void setListener() {
        statusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusView.showLoading();
                requestDianBo(0);
                requestZD();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, DianBoListActivity.class)
                        .putExtra("id","demand")
                        .putExtra("mark", list.get(position).getIdentifier())
                        .putExtra("title", list.get(position).getTitle()));
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        zb_player.configurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().findViewById(R.id.main_bottom_view).setVisibility(View.GONE);
        } else {
            getActivity().findViewById(R.id.main_bottom_view).setVisibility(View.VISIBLE);

        }
    }

    /**
     * 请求直播信息
     */
    private void requestZD() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "zhiboLink");
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    LogUtil.d(response);
                    ZB_Info info = JSON.parseObject(response, ZB_Info.class);
                    zb_player.setVideoPath(info.getContent().getUrl());
                    zb_player.setTitle(info.getContent().getTitle());
                    zb_player.hideSet();
                    GlideUtils.load(info.getContent().getIco(), zb_player.mPlayerThumb);
                    zb_player.init(true, true);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    /**
     * 点播类请求
     */
    private void requestDianBo(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "pnwjxtxyxa");
        MyApplication.getInstance().getmMyOkHttp().post(mActivity, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {

                    LogUtil.d(response);
                    if (i == 1) {
                        list.clear();
                    }
                    DianBo_Info info = JSON.parseObject(response, DianBo_Info.class);
                    if (info.getContent() != null && info.getContent().size() > 0) {
                        list.addAll(info.getContent());
                    }
                    adapter.notifyDataSetChanged();
                    if (i == 1) {
                        refresh.refreshComplete();

                    }
                    if (list.size() > 0) {
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


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (zb_player != null && hidden) {
            zb_player.onPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (zb_player != null) {
            zb_player.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (zb_player != null) {
            zb_player.onDestroy();
        }
    }
}
