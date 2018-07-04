package com.lcsd.luxi.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lcsd.luxi.entity.XWZX_Head_Info;
import com.lcsd.luxi.ui.fragment.Fragment_xwzx;

import java.util.List;


public class XWZX_Head_adapter extends FragmentPagerAdapter {

    private List<XWZX_Head_Info.TContent> list;
    private String mark;

    public XWZX_Head_adapter(FragmentManager fragmentManager, List<XWZX_Head_Info.TContent> list, String mark) {
        super(fragmentManager);
        this.list = list;
        this.mark = mark;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("key", list.get(position).getIdentifier());
        bundle.putString("mark", mark);
        return Fragment_xwzx.getInstance(bundle);

    }
}
