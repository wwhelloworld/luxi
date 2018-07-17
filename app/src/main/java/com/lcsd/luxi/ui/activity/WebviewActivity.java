package com.lcsd.luxi.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.GeolocationPermissions;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

import butterknife.BindView;


public class WebviewActivity extends BaseActivity {
    private Context mContext;
    private String title, id, note, img, url;
    private int currentProgress;
    private boolean isAnimStart = false;
    private ArrayList<String> images;

    @BindView(R.id.webview_top_view)
    View mView;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.ll_webview_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_webview_title)
    TextView tv_title;
    @BindView(R.id.progressBar_webview)
    ProgressBar mProgressBar;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_webview;
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
        //获取传来的值
        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra("title");
            url = getIntent().getStringExtra("url");
        }
        images = new ArrayList<>();
        if (title != null) {
            tv_title.setText(title);
            tv_title.setSelected(true);
        }

        //设置屏幕适应
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);//无缓存
        //设置不使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //本地客户端
        webView.setWebViewClient(new WebViewClient());

        if (url != null) {
            webView.loadUrl(url);
        }
        /**
         * 获取网页加载进度
         */
        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
//                //重写此方法，配置权限
//                geolocationPermissionsCallback.invoke(s,true,false);
//                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
//
//            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);

                currentProgress = mProgressBar.getProgress();
                if (i >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    mProgressBar.setProgress(i);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(mProgressBar.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(i);
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
    }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                mProgressBar.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }


    /**
     * 监听back键
     * 在WebView中回退导航
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  // 返回键的KEYCODE
            if (webView.canGoBack()) {
                webView.goBack();
                return true;  // 拦截
            } else {
                return super.onKeyDown(keyCode, event);   //  放行
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            mContext.deleteDatabase("WebView.db");
            mContext.deleteDatabase("WebViewCache.db");
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            getCacheDir().delete();
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
