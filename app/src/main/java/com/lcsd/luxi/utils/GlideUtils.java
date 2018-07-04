package com.lcsd.luxi.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lcsd.luxi.R;
import com.lcsd.luxi.http.MyApplication;

/**
 * Created by jie on 2018/5/16.
 */
public class GlideUtils {
    public static void load(Context context,
                            String url,
                            ImageView imageView,
                            RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    //使用默认options===普通news默认图
    public static void loadbig(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_moren)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.centerCrop();
        options.dontAnimate();

        Glide.with(MyApplication.getInstance())
                .load(url)

                .apply(options)
                .into(imageView);
    }  //使用默认options===普通news默认图
    public static void load(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_moren)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.centerCrop();
        options.dontAnimate();

        Glide.with(MyApplication.getInstance())
                .load(url)

                .apply(options)
                .into(imageView);
    }
    //使用默认options===普通news默认图

    public static void loadnormoal(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_moren)
                .override(200,200)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.centerCrop();
        options.dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    //使用特殊的默认图
    public static void loadspecial(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_zhibo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.centerCrop();
        options.dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
