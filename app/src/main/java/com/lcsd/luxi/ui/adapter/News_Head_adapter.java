package com.lcsd.luxi.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lcsd.luxi.entity.News_Head_Info;
import com.lcsd.luxi.ui.fragment.Fragment_news;
import com.lcsd.luxi.ui.fragment.MyFragmentPagerAdapter;

import java.util.List;



public class News_Head_adapter extends FragmentPagerAdapter {

    private List<News_Head_Info.TContent> list;

    public News_Head_adapter(FragmentManager fragmentManager, List<News_Head_Info.TContent> list) {
        super(fragmentManager);
        this.list = list;

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
        return Fragment_news.getInstance(bundle);

    }
}
