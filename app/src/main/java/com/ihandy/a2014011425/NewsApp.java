package com.ihandy.a2014011425;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;

/**
 * Created by max on 16-8-27.
 */
public class NewsApp extends Application{
    public NewsTab share_tabs;
    public ViewPagerAdapter viewPagerAdapter;
    public SQLiteDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        share_tabs = new NewsTab();
        share_tabs.setApp(this);
        File path = getApplicationContext().getDatabasePath("news_master_database.db");
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
        synchronized (database) {
            //add table "tabs"
            database.execSQL("create table if not exists tabs(" +
                    "tab_order integer primary key, " +
                    "codedTitle text, " +
                    "title text, " +
                    "watched integer)");
            database.execSQL("create table if not exists favourite_news(" +
                    "news_id integer primary key, " +
                    "codedTab text, " +
                    "title text, " +
                    "source_url text, " +
                    "image_url text, " +
                    "origin text, " +
                    "category text, " +
                    "content text)");
        }
    }
    public void setGlobalViewPagerAdapter(ViewPagerAdapter v){
        viewPagerAdapter = v;
    }

    public boolean isNetworkConnected() {
        Context context = getApplicationContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * Downloads the target page and store in database
     * @param newsid
     * @param url
     */
    public void downloadPage(long newsid, String url){
        synchronized (database){

        }
    }

    /**
     * Downloads the target image and store in database
     * @param codedTab
     * @param newsid
     * @param url
     */
    public void downloadPic(String codedTab, long newsid, String url){

    }
}
