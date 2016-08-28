package com.ihandy.a2014011425;

/**
 * Created by max on 16-8-27.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private NewsTab tabs;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        tabs = new NewsTab();
        tabs.parseTab();
    }

    //获取显示页的Fragment
    @Override
    public Fragment getItem(int position) {
        return new TabPage();
    }

    // page个数设置
    @Override
    public int getCount() {
        return tabs.getTitleNum();
    }

    //设置pageTitle， 我们只需重载此方法即可
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.titleAt(position);
    }
}
