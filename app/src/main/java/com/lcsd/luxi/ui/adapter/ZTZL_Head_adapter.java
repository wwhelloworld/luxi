package com.lcsd.luxi.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lcsd.luxi.entity.ZTZL_Head_Info;
import com.lcsd.luxi.ui.fragment.Fragmen_ztzl;

import java.util.List;


public class ZTZL_Head_adapter extends FragmentPagerAdapter {

    private List<ZTZL_Head_Info.TContent> list;
    private String mark;

    public ZTZL_Head_adapter(FragmentManager fragmentManager, List<ZTZL_Head_Info.TContent> list, String mark) {
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
        return Fragmen_ztzl.getInstance(bundle);

    }
}
