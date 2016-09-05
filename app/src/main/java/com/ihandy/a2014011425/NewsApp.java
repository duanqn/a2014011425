package com.ihandy.a2014011425;

import android.app.Application;

/**
 * Created by max on 16-8-27.
 */
public class NewsApp extends Application{
    public NewsTab share_tabs;
    public ViewPagerAdapter viewPagerAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
        share_tabs = new NewsTab();
    }
    public void setGlobalViewPagerAdapter(ViewPagerAdapter v){
        viewPagerAdapter = v;
    }
}
