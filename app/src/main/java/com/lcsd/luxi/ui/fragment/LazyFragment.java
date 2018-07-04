package com.lcsd.luxi.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class LazyFragment extends Fragment {
    //是否可见
    protected boolean isVisible;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;

    protected Activity mActivity;
    private Unbinder unbinder;
    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        isPrepared = true;
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        initview();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * 实现Fragment数据的缓加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    /**
     * Sets layout id.
     *
     * @return 绑定布局id
     */
    protected abstract int setLayoutId();

    /**
     * 初始化数据
     */
    protected void initview() {

    }

    /**
     * 不可见时调用
     */
    protected void onInVisible() {
    }

    /**
     * 可见时调用
     */
    protected void onVisible() {
        //加载数据
        loadData();
    }

    protected abstract void loadData();
}