package com.lcsd.luxi.ui_third_party.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.activity.BaseActivity;
import com.lcsd.luxi.ui_third_party.adapter.LsAdapter;
import com.lcsd.luxi.ui_third_party.entity.Ls1;
import com.lcsd.luxi.view.MultipleStatusView;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.tsy.sdk.myokhttp.util.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lv_ls)
    ListView mlv;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.history_top_view)
    View mView;

    private Context mContext;
    private List<Ls1.TResult> list;
    private LsAdapter adapter;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_ls;
    }

    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(mView).init();
        ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
    }

    @Override
    protected void initData() {
        mContext = this;
        //显示加载中视图
        multipleStatusView.showLoading();
        request_data();

        //设置重试视图点击事件
        multipleStatusView.setOnRetryClickListener(mRetryClickListener);

        findViewById(R.id.ll_back).setOnClickListener(this);
        tv_title.setText("历史上的今天");
        list = new ArrayList<>();
        adapter = new LsAdapter(mContext, list);
        mlv.setAdapter(adapter);
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mContext, LsContnetActivity.class).putExtra("e_id", list.get(i).getE_id()));
            }
        });
    }

    private void request_data() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        final Map<String, String> map = new HashMap<>();
        map.put("key", AppConfig.Lskey);
        map.put("date", month + "/" + day);
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Lssdjt1, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    LogUtils.d("历史上的今天：", response);
                    Ls1 ls = JSON.parseObject(response, Ls1.class);
                    if (ls.getError_code() == 0) {
                        if (ls.getResult() != null && ls.getResult().size() > 0) {
                            if (list.size() > 0) {
                                list.clear();
                            }
                            list.addAll(ls.getResult());
                            adapter.notifyDataSetChanged();
                            multipleStatusView.showContent();
                        } else {
                            multipleStatusView.showEmpty();
                        }
                    } else {
                        Toast.makeText(mContext, ls.getReason(), Toast.LENGTH_SHORT).show();
                        multipleStatusView.showError();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
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
            request_data();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
