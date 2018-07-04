package com.lcsd.luxi.refresh;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class DisallowParentTouchViewPager extends ViewPager {

    private ViewGroup parent;

    /**
     * 不能用getparent不然无法找到外层刷新的控件父控件
     */
    public void setParent(ViewGroup viewGroup) {
        this.parent = viewGroup;
    }

    public DisallowParentTouchViewPager(Context context) {
        super(context);

    }


    public DisallowParentTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (parent != null) {
                    //禁止上一层的View不处理该事件,屏蔽父组件的事件
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (parent != null) {
                    //拦截
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}