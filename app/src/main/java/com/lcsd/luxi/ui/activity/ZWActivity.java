package com.lcsd.luxi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.ZW_Info;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.ZW_adapter;
import com.lcsd.luxi.utils.LogUtil;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ZWActivity extends BaseActivity {
    private Context mContext;
    private String url, title, mark;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    private List<ZW_Info.TContent.TRslist> list;
    private ZW_adapter adapter;

    @BindView(R.id.listview_zw)
    ListView listView;
    @BindView(R.id.zw_top_view)
    View mView;
    @BindView(R.id.tv_zw_title)
    TextView tv_title;
    @BindView(R.id.ll_zw_back)
    LinearLayout ll_back;
    @BindView(R.id.refresh_zw)
    PtrClassicFrameLayout refresh;
    @BindView(R.id.statu_zw)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_zw;
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
        statusView.showLoading();
        requestZW(0);
        //填充数据
        list = new ArrayList<>();
        adapter = new ZW_adapter(mContext, list);
        listView.setAdapter(adapter);
        //刷新
        refresh.setLastUpdateTimeRelateObject(this);
        refresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (pageid >= total) {
                    refresh.refreshComplete();
                } else {
                    pageid++;
                    requestZW(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestZW(1);
            }
        });
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
                requestZW(0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class)
                        .putExtra("url", list.get(position).getVideo())
                        .putExtra("title", list.get(position).getTitle())
                        .putExtra("img", list.get(position).getThumb()));
            }
        });
    }

    private void requestZW(final int i) {
        Map<String, String> map = new HashMap<>();
        if (i == 1 || i == 0) {
            map.put("pageid", 1 + "");
        } else {
            map.put("pageid", pageid + "");
        }
        map.put("psize", psize + "");
        MyApplication.getInstance().getmMyOkHttp().post(mContext, url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {


                        LogUtil.d(response);
                        if (i == 1) {
                            list.clear();
                            pageid = 1;
                        }
                        ZW_Info info = JSON.parseObject(response, ZW_Info.class);
                        if (info.getContent().getRslist() != null && info.getContent().getRslist().size() > 0) {
                            list.addAll(info.getContent().getRslist());
                        }
                        if (info.getContent().getTotal() != null) {
                            total = info.getContent().getTotal();
                        }
                        adapter.notifyDataSetChanged();
                        if (i == 1 || i == 2) {
                            refresh.refreshComplete();
                        }
                        if (list.size() > 0) {
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
