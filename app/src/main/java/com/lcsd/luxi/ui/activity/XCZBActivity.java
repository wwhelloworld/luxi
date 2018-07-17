package com.lcsd.luxi.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.XCZB_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.XCZB_adapter;
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

public class XCZBActivity extends BaseActivity {

    private String url, title;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    private List<XCZB_Info.TContent.TRslist> list;
    private XCZB_adapter adapter;

    @BindView(R.id.xczb_view_top_view)
    View mView;
    @BindView(R.id.ll_xczb_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_xczb_title)
    TextView tv_title;

    @BindView(R.id.listview_xczb)
    ListView listView;
    @BindView(R.id.refresh_xczb)
    PtrClassicFrameLayout refresh;
    @BindView(R.id.statu_xczb)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_xczb;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
        ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
    }

    @Override
    protected void initData() {
        super.initData();
        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
        }
        if (title != null) {
            tv_title.setText(title);
        }
        list = new ArrayList<>();

        if (url != null && url.contains("http")) {
            requestXCZB(0);
        } else {
            url = "http://luxi.5kah.com/" + url;
            requestXCZB(0);

        }
        adapter = new XCZB_adapter(mContext, list);
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
                    requestXCZB(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestXCZB(1);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
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
                requestXCZB(1);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getZbstatus().equals("1")) {

                    startActivity(new Intent(mContext, VideoPlayerActivity.class)
                            .putExtra("url", list.get(position).getZbaddress())
                            .putExtra("img",list.get(position).getThumb())
                            .putExtra("title", list.get(position).getTitle()));

                } else if (list.get(position).getZbstatus().equals("2")) {
                    startActivity(new Intent(mContext, WebviewActivity.class)
                            .putExtra("url", list.get(position).getZbaddress())
                            .putExtra("title", list.get(position).getTitle()));
                } else if (list.get(position).getZbstatus().equals("3")) {
                    if (list.get(position).getVideo()!=null&&!list.get(position).getVideo().equals("")){
                        startActivity(new Intent(mContext, VideoPlayerActivity.class)
                                .putExtra("url", list.get(position).getVideo())
                                .putExtra("img",list.get(position).getThumb())
                                .putExtra("title", list.get(position).getTitle()));
                    }else {
                        startActivity(new Intent(mContext, VideoPlayerActivity.class)
                                .putExtra("url", list.get(position).getZbaddress())
                                .putExtra("img",list.get(position).getThumb())
                                .putExtra("title", list.get(position).getTitle()));
                    }

                } else {
                    Toast.makeText(mContext, "直播筹划中！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestXCZB(final int i) {
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
                        XCZB_Info info = JSON.parseObject(response, XCZB_Info.class);
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
