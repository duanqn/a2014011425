package com.ihandy.a2014011425;

/**
 * Created by max on 16-8-27.
 */
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihandy.a2014011425.fragment.RecyclerViewFragment;

import java.util.ArrayList;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    final int NEWS_TAB_UPDATE = 1;
    private NewsTab tabs;
    private NewsApp app;
    private Handler mhandler;
    boolean notified;
    int lastnum;

    private ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        notified = false;
        lastnum = 0;
        mhandler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case NEWS_TAB_UPDATE:
                        ViewPagerAdapter.this.notifyDataSetChanged();
                        break;
                }
            }
        };
    }


    public static ViewPagerAdapter getNewInstance(NewsApp appPointer, FragmentManager fm){
        ViewPagerAdapter r = new ViewPagerAdapter(fm);
        r.app = appPointer;
        r.tabs = appPointer.share_tabs;
        return r;
    }

    //获取显示页的Fragment
    @Override
    public Fragment getItem(int position) {
        if(tabs.map.containsKey(tabs.titleAt(position)))
            return tabs.map.get(tabs.titleAt(position));
        else
            return RecyclerViewFragment.newInstance(tabs, position);
    }




    // page个数设置
    @Override
    public int getCount() {
            int res;
            try {
                while (tabs.tabReady == false) {
                    Thread.sleep(40);
                }
            }catch(InterruptedException e){}
            res = tabs.getTitleNum();
            return res;
    }

    //设置pageTitle， 我们只需重载此方法即可
    @Override
    public CharSequence getPageTitle(int position) {
        if(tabs != null)
            return tabs.titleAt(position);
        else
            return "";
    }

    public boolean removeTab(int position){
        boolean r = tabs.makeTabInvisible(position);
        if(r){
            this.notifyDataSetChanged();
        }
        return r;
    }

    //Use for removing tabs
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public NewsTab getTabs(){
        return tabs;
    }

    public boolean addTabBack(int position_in_unwatched){
        boolean r = tabs.makeTabVisible(position_in_unwatched);
        if(r){
            this.notifyDataSetChanged();
        }
        return r;
    }
}
