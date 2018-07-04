package com.lcsd.luxi.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;

public class NewsDetialActivity extends BaseActivity {
    private Context mContext;
    private String title, id, note, img, url;
    private int currentProgress;
    private boolean isAnimStart = false;
    private ArrayList<String> images;

    @BindView(R.id.newsdetial_top_view)
    View mView;
    @BindView(R.id.webview_news_detial)
    WebView webView;
    @BindView(R.id.ll_news_detial_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_news_detial_title)
    TextView tv_title;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_news_detial;
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
        webView.getSettings().getLoadWithOverviewMode();
        webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);//无缓存
        //设置不使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //通信接口和拦截url
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                addImageClickListner();

            }
        });
        if (url != null) {
            webView.loadUrl(url);
        }

        //载入js
        webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        /**
         * 获取网页加载进度
         */

        webView.setWebChromeClient(new WebChromeClient() {
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
                //findViewById(R.id.view_xian).setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void readImageUrl(String img) {     //把所有图片的url保存在ArrayList<String>中
            images.add(img);
        }

        @android.webkit.JavascriptInterface  //对于targetSdkVersion>=17的，要加这个声明
        public void openImage(String clickimg)//点击图片所调用到的函数
        {
            int index = 0;
            ArrayList<String> list = addImages();
            for (String url : list)
                if (url.equals(clickimg)) index = list.indexOf(clickimg);//获取点击图片在整个页面图片中的位置
            String[] imgs = new String[list.size()];
            for (int i = 0; i < imgs.length; i++) {
                imgs[i] = list.get(i);
            }
            startActivity(new Intent(context, ImagePageActivity.class).putExtra("images", imgs).putExtra("index", index));//启动ViewPagerActivity,用于显示图片
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

    }


    //去重复
    private ArrayList<String> addImages() {
        ArrayList<String> list = new ArrayList<>();
        Set set = new HashSet();
        for (String cd : images) {
            if (set.add(cd)) {
                list.add(cd);
            }
        }
        return list;
    }

    // 注入js函数监听
    private void addImageClickListner() {
        //遍历页面中所有img的节点，因为节点里面的图片的url即objs[i].src，保存所有图片的src.
        //为每个图片设置点击事件，objs[i].onclick
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{" +
                "window.imagelistner.readImageUrl(objs[i].src);  " +
                " objs[i].onclick=function()  " +
                " {  " +
                " window.imagelistner.openImage(this.src);  " +
                "  }  " +
                "}" +
                "})()");
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
            webView.destroy();
        }
    }
}
