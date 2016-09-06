package com.ihandy.a2014011425;

/**
 * Created by max on 16-8-27.
 */
import android.app.FragmentTransaction;
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
    Handler handler;
    private NewsApp app;
    private static final int MAX_TAB=10;    //Ugly coding and we hope ta won't find this

    private ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case NEWS_TAB_UPDATE:
                        ViewPagerAdapter.this.notifyDataSetChanged();
                        break;
                    default:
                }
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run(){
                tabs.getResponse();
                tabs.parseTab();
                ViewPagerAdapter.this.handler.sendEmptyMessage(NEWS_TAB_UPDATE);
            }
        };
        thread.start();
    }

    public void setApp(NewsApp appPointer){
        app = appPointer;
    }
    public void getGlobalTabs(){
        tabs = app.share_tabs;
    }
    public static ViewPagerAdapter getNewInstance(NewsApp appPointer, FragmentManager fm){
        ViewPagerAdapter r = new ViewPagerAdapter(fm);
        r.setApp(appPointer);
        r.getGlobalTabs();
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
        if(tabs != null)
            return tabs.getTitleNum();
        else
            return 0;
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
