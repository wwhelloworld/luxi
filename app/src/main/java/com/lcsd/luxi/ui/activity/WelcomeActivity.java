package com.lcsd.luxi.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gyf.barlibrary.BarHide;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.AD_Info;
import com.lcsd.luxi.entity.Status;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {
    private Context context;
    private AlertDialog.Builder alert;

    @BindView(R.id.iv_welcome)
    ImageView iv_welcome;
    @BindView(R.id.tv_step)
    TextView tv_step;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.hideStatusBar(getWindow());//隐藏状态栏
    }

    @Override
    protected void initData() {
        context = this;

        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(2500);//动画的持续的时间
        //animation.setRepeatCount(2);//动画的重复次数
        iv_welcome.startAnimation(animation);//开始动画
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                requestCanGO();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void setListener() {
        //单独点击跳转
        tv_step.setVisibility(View.INVISIBLE);
        tv_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(4);
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    android.os.Handler handler = new android.os.Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_step.setText("3 跳过");
                    break;
                case 2:
                    tv_step.setText("2 跳过");
                    break;
                case 3:
                    tv_step.setText("1 跳过");
                    break;
                case 4:
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    /**
     * 判断服务器是否开启
     */
    private void requestCanGO() {
        Map<String, String> map = new HashMap<>();
        map.put("f", "status");
        MyApplication.getInstance().getmMyOkHttp().post(context, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("获取网站的开关状态----", response);
                    Status status = JSON.parseObject(response, Status.class);
                    if (status.getStatus().equals("1")) {
                        requestAD();

                    } else {
                        alert = new AlertDialog.Builder(context);
                        alert.setTitle(status.getContent()).setIcon(R.mipmap.ic_launcher).setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setCancelable(false).create().show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                //错误的现象掩饰
                if (error_msg != null) {
                    tv_step.setVisibility(View.VISIBLE);
                    tv_step.setText("3 跳过");
                    handler.sendEmptyMessageDelayed(1, 0);
                    handler.sendEmptyMessageDelayed(2, 1500);
                    handler.sendEmptyMessageDelayed(3, 3000);
                    handler.sendEmptyMessageDelayed(4, 3500);
                }
            }


        });
    }

    /**
     * 广告图
     */
    private void requestAD() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "appad");
        MyApplication.getInstance().getmMyOkHttp().post(context, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("请求到的启动图广告=====", response);
                    AD_Info info = JSON.parseObject(response, AD_Info.class);
                    if (info.getStatus().equals("ok")) {
                        if (info.getContent().getRslist().get(0).getThumbAd().trim() != null && !info.getContent().getRslist().get(0).getThumbAd().equals("")) {
                            Glide.with(context).load(info.getContent().getRslist().get(0).getThumbAd()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    tv_step.setVisibility(View.VISIBLE);
                                    tv_step.setText("3 跳过");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    tv_step.setVisibility(View.VISIBLE);
                                    tv_step.setText("3 跳过");
                                    return false;
                                }
                            }).into(iv_welcome);
                            handler.sendEmptyMessageDelayed(1, 0);
                            handler.sendEmptyMessageDelayed(2, 1500);
                            handler.sendEmptyMessageDelayed(3, 3000);
                            handler.sendEmptyMessageDelayed(4, 3500);
                        } else {
                            tv_step.setVisibility(View.VISIBLE);
                            tv_step.setText("3 跳过");
                            handler.sendEmptyMessageDelayed(1, 0);
                            handler.sendEmptyMessageDelayed(2, 1500);
                            handler.sendEmptyMessageDelayed(3, 3000);
                            handler.sendEmptyMessageDelayed(4, 3500);
                        }
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                if (error_msg != null) {
                    tv_step.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(1, 0);
                    handler.sendEmptyMessageDelayed(2, 1500);
                    handler.sendEmptyMessageDelayed(3, 3000);
                    handler.sendEmptyMessageDelayed(4, 3500);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}
