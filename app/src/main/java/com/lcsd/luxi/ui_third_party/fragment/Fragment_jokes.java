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
import com.lcsd.luxi.ui.activity.ImagePageActivity;
import com.lcsd.luxi.ui.fragment.BaseV4Fragment;
import com.lcsd.luxi.ui_third_party.adapter.JokesAdapter;
import com.lcsd.luxi.ui_third_party.entity.Jokes;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.tsy.sdk.myokhttp.util.LogUtils;

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
 * Created by Administrator on 2018/3/14.
 */
public class Fragment_jokes extends BaseV4Fragment {
    @BindView(R.id.lv_jokes)
    ListView lv;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.ptr_jokes)
    PtrClassicFrameLayout ptr;
    private List<Jokes> list;
    private JokesAdapter adapter;
    private String type;

    public static Fragment getInstance(Bundle bundle) {
        Fragment_jokes fragment = new Fragment_jokes();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_jokes;
    }

    protected void initData() {
        //显示加载中视图
        multipleStatusView.showLoading();
        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        type = getArguments().getString("type");
        list = new ArrayList<>();
        adapter = new JokesAdapter(mActivity, list);
        lv.setAdapter(adapter);
        request_data(false);

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
                if (list.get(i).getUrl() != null && list.get(i).getUrl().length() > 0) {
                    startActivity(new Intent(mActivity, ImagePageActivity.class).putExtra("images", new String[]{list.get(i).getUrl()}).putExtra("index", 0));
                }
            }
        });


    }

    private void request_data(final boolean isrefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("key", AppConfig.JokesKey);
        if (type != null) {
            map.put("type", type);
        }
        MyApplication.getInstance().getmMyOkHttp().post(mActivity, AppConfig.Jokes, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    LogUtils.d("笑话大全：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("error_code") == 0) {
                            List<Jokes> jokes = JSON.parseArray(object.getString("result"), Jokes.class);
                            if (jokes != null && jokes.size() > 0) {
                                list.addAll(0, jokes);
                                adapter.notifyDataSetChanged();
                                multipleStatusView.showContent();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        multipleStatusView.showError();
                    }
                }
                if (isrefresh) {
                    ptr.refreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (isrefresh) {
                    ptr.refreshComplete();
                }
                try {
                    multipleStatusView.showNoNetwork();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            multipleStatusView.showLoading();
            request_data(false);
        }
    };
}
