package com.lcsd.luxi.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.view.photoview.PhotoView;

import butterknife.BindView;

/**
 * Created by jie on 2018/5/16.
 */
public class ImagePageActivity extends BaseActivity {
    @BindView(R.id.img_viewpager)
    ViewPager vp;
    @BindView(R.id.img_tv)
    TextView tv;
    private String[] img;
    private int index;
    private Context mContext;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_image_page;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()//底部栏不透明
                .init();
    }

    @Override
    protected void initData() {
        mContext = this;
        if (getIntent() != null) {
            img = getIntent().getStringArrayExtra("images");
            index = getIntent().getIntExtra("index", 0);
        }
        tv.setText((index + 1) + "/" + img.length);
        vp.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv.setText(position + 1 + "/" + img.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_photoview, container, false);
            final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
            final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);
            imageView.enable();
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            pb.setVisibility(View.VISIBLE);

            RequestOptions options = new RequestOptions();
            options.dontAnimate();
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(mContext).load(img[position]).apply(options).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    pb.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    pb.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageView);
            container.addView(view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePageActivity.this.finish();
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.animator.sacle_small);
    }
}
