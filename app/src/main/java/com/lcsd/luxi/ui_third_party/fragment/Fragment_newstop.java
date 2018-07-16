package com.lcsd.luxi.ui_third_party.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lcsd.luxi.R;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.WebviewActivity;
import com.lcsd.luxi.ui.fragment.BaseV4Fragment;
import com.lcsd.luxi.ui_third_party.adapter.NewsTopAdapter;
import com.lcsd.luxi.ui_third_party.entity.NewsTop;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/3/7.
 */
public class Fragment_newstop extends BaseV4Fragment {
    private String identifier, title;
    @BindView(R.id.ptr_newstop)
    PtrClassicFrameLayout ptr;
    @BindView(R.id.lv_newstop)
    ListView lv;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private List<NewsTop.TData> list;
    private NewsTopAdapter adapter;

    public static Fragment getInstance(Bundle bundle) {
        Fragment_newstop fragment = new Fragment_newstop();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_newstop;
    }

    @Override
    protected void initData() {
        //显示加载中视图
        multipleStatusView.showLoading();
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        identifier = getArguments().getString("identifier");
        title = getArguments().getString("title");
        list = new ArrayList<>();
        adapter = new NewsTopAdapter(getContext(), list);
        lv.setAdapter(adapter);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request_data(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getContext(), WebviewActivity.class)
                        .putExtra("url", list.get(i).getUrl())
                        .putExtra("title", title));
            }
        });
        request_data(false);
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_data(false);
        }
    };

    private void request_data(final boolean isresh) {
        Map<String, String> map = new HashMap<>();
        map.put("type", identifier);
        map.put("key", AppConfig.NewsTopKey);
        MyApplication.getInstance().getmMyOkHttp().post(getContext(), AppConfig.NewsTop, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    //L.d("新闻头条：",response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("error_code") == 0) {
                            NewsTop newsTop = JSON.parseObject(object.getString("result"), NewsTop.class);
                            if (newsTop != null && newsTop.getData() != null && newsTop.getData().size() > 0) {
                                list = newsTop.getData();
                                adapter.setList(list);
                                multipleStatusView.showContent();
                                if (isresh) {
                                    ptr.refreshComplete();
                                }
                            } else {
                                multipleStatusView.showEmpty();
                            }
                        } else {
                            multipleStatusView.showError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                ptr.refreshComplete();
                try {
                    multipleStatusView.showNoNetwork();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
