package com.ihandy.a2014011425;

/**
 * Created by max on 16-8-27.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> titleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        titleList.add("Top Focus");
        titleList.add("Global");
        titleList.add("Sport");
        titleList.add("Social");
        titleList.add("Entertainment");
    }

    //获取显示页的Fragment
    @Override
    public Fragment getItem(int position) {
        return new TabPage();
    }

    // page个数设置
    @Override
    public int getCount() {
        return 5;
    }

    //设置pageTitle， 我们只需重载此方法即可
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
