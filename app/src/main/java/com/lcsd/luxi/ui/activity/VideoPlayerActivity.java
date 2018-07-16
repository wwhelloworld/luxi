package com.lcsd.luxi.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.ui.fragment.Fragment3;
import com.lcsd.luxi.utils.GlideUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;

public class VideoPlayerActivity extends BaseActivity {
    private Context mContext;
    private String url, title, img;
    private String path = "http://www.jxnn.cn/res/oldvideo/xinwnen/2017news/1.4/孙广东参加部分乡镇代表团讨论.mp4";
    private String path1 = "http://www.jxnn.cn/res/oldvideo/xinwnen/2017news/1.4/%E5%AD%99%E5%B9%BF%E4%B8%9C%E5%8F%82%E5%8A%A0%E9%83%A8%E5%88%86%E4%B9%A1%E9%95%87%E4%BB%A3%E8%A1%A8%E5%9B%A2%E8%AE%A8%E8%AE%BA.mp4";
    @BindView(R.id.video_top_view)
    View mView;
    @BindView(R.id.ll_video_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_video_title)
    TextView tv_title;
    @BindView(R.id.video_player)
    IjkPlayerView playerView;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_video_player;
    }

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
            img = getIntent().getStringExtra("img");
        }
        if (title != null) {
            tv_title.setText(title);
        }
        if (url != null) {
            if (url.contains("m3u8")) {
                playerView.setVideoPath(encodeUrl(url));
                playerView.setTitle(title);
                playerView.hideSet();
                playerView.hideSelect();
                GlideUtils.loadspecial(mContext, img, playerView.mPlayerThumb);
                playerView.init(true, true);
            } else {
                playerView.setVideoPath(encodeUrl(url));
                playerView.setTitle(title);
                playerView.hideSet();
                playerView.hideSelect();
                GlideUtils.loadspecial(mContext, img, playerView.mPlayerThumb);
                playerView.init(false, true);
            }

        }
    }

    /**
     * 正则表达式（还有另外两种方法）
     */
    public static String encodeUrl(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        playerView.configurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.ll_video_title).setVisibility(View.GONE);
            findViewById(R.id.video_top_view).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ll_video_title).setVisibility(View.VISIBLE);
            findViewById(R.id.video_top_view).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (playerView != null && playerView.isFullscreen()) {
            //防止音量键导致返回竖屏
            if (playerView.handleVolumeKey(keyCode)) {
                return true;
            } else {
                playerView.onBackPressed();
            }
            return false;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerView != null) {
            playerView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerView != null) {
            playerView.onDestroy();
        }
    }
}
