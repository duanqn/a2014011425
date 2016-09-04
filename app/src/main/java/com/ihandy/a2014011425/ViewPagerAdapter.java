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
import android.widget.TextView;

import com.ihandy.a2014011425.fragment.RecyclerViewFragment;

import java.util.ArrayList;
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    final int NEWS_TAB_UPDATE = 1;
    private NewsTab tabs;
    private ArrayList<RecyclerViewFragment> fragments;
    Handler handler;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case NEWS_TAB_UPDATE:
                        tabs = (NewsTab) msg.obj;
                        for(int i = 0; i < tabs.getTitleNum(); ++i){
                            fragments.add(RecyclerViewFragment.newInstance(tabs, i));
                        }
                        ViewPagerAdapter.this.notifyDataSetChanged();
                        break;
                    default:
                }
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run(){
                NewsTab nt = new NewsTab();
                nt.getResponse();
                nt.parseTab();
                Message msg = new Message();
                msg.what = NEWS_TAB_UPDATE;
                msg.obj = nt;
                ViewPagerAdapter.this.handler.sendMessage(msg);
            }
        };
        thread.start();
    }

    //获取显示页的Fragment
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
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
}
