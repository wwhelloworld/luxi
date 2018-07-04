package com.lcsd.luxi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Search_Info;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.ui.adapter.Search_adapter;
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

public class SearchActivity extends BaseActivity {
    private Context mContext;
    private List<Search_Info.TContent.TRslist> list;
    private Search_adapter adapter;
    private int total;
    private int pageid = 1;
    private int psize = 10;
    private String keywords;

    @BindView(R.id.search_top_view)
    View mView;
    @BindView(R.id.ll_search_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.statu_search)
    MultipleStatusView statusView;
    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.listview_search)
    ListView listView;
    @BindView(R.id.refresh_search)
    PtrClassicFrameLayout refresh;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
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
        showSoftInputFromWindow(this, edit_search);
        list = new ArrayList<>();
        //填充数据
        adapter = new Search_adapter(mContext, list);
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
                    requestSearch(2);
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestSearch(1);

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (list.size() <= 0 || keywords == null || keywords.equals("")) {
                    return false;

                } else {
                    return super.checkCanDoRefresh(frame, content, header);

                }
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (list.size() <= 0 || keywords == null || keywords.equals("")) {
                    return false;

                } else {
                    return super.checkCanDoLoadMore(frame, content, footer);
                }
            }
        });


        //点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getVideo() != null && !list.get(position).getVideo().equals("")) {
                    startActivity(new Intent(mContext, VideoPlayerActivity.class)
                            .putExtra("url", list.get(position).getVideo())
                            .putExtra("title", list.get(position).getTitle())
                            .putExtra("img", list.get(position).getThumb()));
                } else {
                    startActivity(new Intent(mContext, NewsDetialActivity.class)
                            .putExtra("url", list.get(position).getUrl())
                            .putExtra("title", list.get(position).getTitle()));
                }
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
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_search.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, "请输入搜索的关键字！", Toast.LENGTH_SHORT).show();
                } else {
                    keywords = edit_search.getText().toString().trim();
                    statusView.showLoading();
                    requestSearch(1);
                    hideKeyboard(v.getWindowToken());


                }

            }
        });

        /**调用键盘的搜索按钮*/
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edit_search.getText().toString().trim().equals("")) {
                        Toast.makeText(mContext, "请输入搜索的关键字！", Toast.LENGTH_SHORT).show();
                    } else {
                        keywords = edit_search.getText().toString().trim();
                        statusView.showLoading();
                        requestSearch(1);
                        hideKeyboard(v.getWindowToken());

                    }
                    return true;

                }

                return false;

            }

        });


    }

    private void requestSearch(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "search");
        map.put("keywords", keywords);
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
                    try {
                    LogUtil.d(response);
                    if (i == 1) {
                        list.clear();
                        pageid = 1;

                    }
                    Search_Info info = JSON.parseObject(response, Search_Info.class);
                    if (info.getContent().getRslist() != null && info.getContent().getRslist().size() > 0) {
                        list.addAll(info.getContent().getRslist());
                    }
                    if (info.getContent().getTotal_page() != null) {
                        total = info.getContent().getTotal_page();
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list.size() > 0) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
