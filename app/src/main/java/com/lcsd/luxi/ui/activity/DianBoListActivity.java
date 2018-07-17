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
import com.lcsd.luxi.entity.DianBoList_Info;
import com.lcsd.luxi.entity.ZW_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.DianBoList_adapter;
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


public class DianBoListActivity extends BaseActivity {
    private Context mContext;
    private String title, mark, id;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    private List<DianBoList_Info.TContent.TRslist> list;
    private DianBoList_adapter adapter;

    @BindView(R.id.listview_dianbolist)
    ListView listView;
    @BindView(R.id.dianbolist_top_view)
    View mView;
    @BindView(R.id.tv_dianbolist_title)
    TextView tv_title;
    @BindView(R.id.ll_dianbolist_back)
    LinearLayout ll_back;
    @BindView(R.id.refresh_dianbolist)
    PtrClassicFrameLayout refresh;
    @BindView(R.id.statu_dianbolist)
    MultipleStatusView statusView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_dian_bo_list;
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
            title = getIntent().getStringExtra("title");
            mark = getIntent().getStringExtra("mark");
            id = getIntent().getStringExtra("id");
        }
        if (title != null) {
            tv_title.setText(title);
        }
        statusView.showLoading();
        requestZW(0);
        //填充数据
        list = new ArrayList<>();
        adapter = new DianBoList_adapter(mContext, list);
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
                requestZW(1);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getVideo() != null && !list.get(position).getVideo().equals("")) {
                    mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class)
                            .putExtra("url", list.get(position).getVideo())
                            .putExtra("title", list.get(position).getTitle())
                            .putExtra("img", list.get(position).getThumb()));
                } else {
                    mContext.startActivity(new Intent(mContext, NewsDetialActivity.class)
                            .putExtra("url", list.get(position).getUrl())
                            .putExtra("title", list.get(position).getTitle()));
                }
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
        map.put("id", id);
        map.put("cate", mark);
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        LogUtil.d(response);
                        if (i == 1) {
                            list.clear();
                            pageid = 1;
                        }
                        DianBoList_Info info = JSON.parseObject(response, DianBoList_Info.class);
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
