package com.lcsd.luxi.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lcsd.luxi.entity.AboutUs_Head_Info;
import com.lcsd.luxi.ui.fragment.Fragment_aboutus;

import java.util.List;




public class AboutUs_Head_adapter extends FragmentPagerAdapter {

    private List<AboutUs_Head_Info.TContent> list;
    private String mark;

    public AboutUs_Head_adapter(FragmentManager fragmentManager, List<AboutUs_Head_Info.TContent> list, String mark) {
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
        bundle.putString("linkurl", list.get(position).getLinkurl());
        bundle.putString("mark", mark);
        return Fragment_aboutus.getInstance(bundle);

    }
}
