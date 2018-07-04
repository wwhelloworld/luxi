package com.lcsd.luxi.http;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tencent.smtt.sdk.QbSdk;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.umeng.commonsdk.UMConfigure;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by jie on 2018/5/11.
 */
public class MyApplication extends Application {
    public static MyApplication app;
    private MyOkHttp mMyOkHttp;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        app = this;
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5af553bdf29d980860000072", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
        QbSdk.initX5Environment(this, null);

        //持久化存储cookie
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);
    }

    public static MyApplication getInstance() {
        if (app == null)
            return new MyApplication();
        return app;
    }

    public MyOkHttp getmMyOkHttp() {
        return mMyOkHttp;
    }
}
