package com.lcsd.luxi.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dl7.player.media.IjkPlayerView;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.DianBo_Info;
import com.lcsd.luxi.entity.ZB_Info;
import com.lcsd.luxi.entity.ZhiBo_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.DianBoListActivity;
import com.lcsd.luxi.ui.activity.XCZBActivity;
import com.lcsd.luxi.ui.adapter.DianBo_adapter;
import com.lcsd.luxi.ui.adapter.ZhiBo_adapter;
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
    private ZhiBo_adapter zb_adapter;
    private DianBo_adapter db_adapter;
    private List<ZhiBo_Info.TContent> zblist;
    private List<DianBo_Info.TContent> dblist;
    public static IjkPlayerView zb_player;
    @BindView(R.id.gv_zhibo)
    GridView gridView_zb;
    @BindView(R.id.gv_dianbo)
    GridView gridView_db;
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
        zb_player.hideSet();
        zb_player.init(true, true);
        //直播广播
        requestZBGB(0);
        //点播请求
        requestDianBo(0);


        //填充适配器
        zblist = new ArrayList<>();
        dblist = new ArrayList<>();
        //直播
        zb_adapter = new ZhiBo_adapter(mContext, zblist);
        gridView_zb.setAdapter(zb_adapter);
        //点播
        db_adapter = new DianBo_adapter(mContext, dblist);
        gridView_db.setAdapter(db_adapter);
        //刷新
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestZBGB(1);
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
                requestZBGB(0);
                requestDianBo(0);
            }
        });
        gridView_zb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (zblist.get(position).getZblinker() != null && !zblist.get(position).getZblinker().equals("")) {
                    if (zblist.get(position).getZblinker().contains("m3u8")) {
                        if (zblist.get(position).getZblinker().contains("fm")) {
                            zb_player.switchVideoPath(true, zblist.get(position).getZblinker());
                            zb_player.setTitle(zblist.get(position).getTitle());
                            GlideUtils.loadspecial(mContext, zblist.get(position).getThumb(), zb_player.mPlayerThumb);
                            zb_player.isFM(true);
                            zb_player.start();
                        } else {
                            zb_player.switchVideoPath(false, zblist.get(position).getZblinker());
                            zb_player.setTitle(zblist.get(position).getTitle());
                            GlideUtils.loadspecial(mContext, zblist.get(position).getThumb(), zb_player.mPlayerThumb);
                            zb_player.isFM(false);
                            zb_player.start();
                        }

                    } else {
                        startActivity(new Intent(mContext, XCZBActivity.class)
                                .putExtra("url", zblist.get(position).getZblinker())
                                .putExtra("title", zblist.get(position).getTitle()));

                    }
                } else {
                    Toast.makeText(mContext, "后台维护中，请稍后重试！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        gridView_db.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                mContext.startActivity(new Intent(mContext, DianBoListActivity.class)
                        .putExtra("id", "demand")
                        .putExtra("mark", dblist.get(i).getIdentifier())
                        .putExtra("title", dblist.get(i).getTitle()));
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
     * 直播类请求
     */
    private void requestZBGB(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "data");
        map.put("data", "zhiboLink");
        MyApplication.getInstance().getmMyOkHttp().post(mActivity, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {

                        LogUtil.d(response);
                        if (i == 1) {
                            zblist.clear();
                        }
                        ZhiBo_Info info = JSON.parseObject(response, ZhiBo_Info.class);
                        if (info.getContent() != null && info.getContent().size() > 0) {
                            zblist.addAll(info.getContent());
                        }
                        zb_adapter.notifyDataSetChanged();
                        if (i == 0) {
                            //初始加载直播流
                            zb_player.setVideoPath(info.getContent().get(0).getZblinker());
                            zb_player.setTitle(info.getContent().get(0).getTitle());
                            GlideUtils.load(info.getContent().get(0).getThumb(), zb_player.mPlayerThumb);
                        }
                        if (i == 1) {
                            refresh.refreshComplete();

                        }
                        if (zblist.size() > 0) {
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
                            dblist.clear();
                        }
                        DianBo_Info info = JSON.parseObject(response, DianBo_Info.class);
                        if (info.getContent() != null && info.getContent().size() > 0) {
                            dblist.addAll(info.getContent());
                        }
                        db_adapter.notifyDataSetChanged();
                        if (i == 1) {
                            refresh.refreshComplete();

                        }
                        if (dblist.size() > 0) {
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
