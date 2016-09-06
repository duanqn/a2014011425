package com.ihandy.a2014011425.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ihandy.a2014011425.NewsApp;
import com.ihandy.a2014011425.NewsContent;
import com.ihandy.a2014011425.NewsTab;
import com.ihandy.a2014011425.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.ihandy.a2014011425.R;
import com.ihandy.a2014011425.TestRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by florentchampigny on 24/04/15.
 */

//TODO: Manually overwrite
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private int tab_order;
    private NewsApp app;
    private NewsTab tabInfo;
    private List<NewsContent> mContentItems = new ArrayList<>();
    private Handler mhandler;
    private int lastVisibleItem;

    class RefreshThread implements Runnable{
        long max_news_id;
        public static final int NEWEST = -1;
        public RefreshThread (long max_id){
            max_news_id = max_id;
        }
        public void run(){
            while(!tabInfo.tabReady){
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){}
            }
            JSONObject obj = getResponse();
            JSONArray newsList;
            JSONObject news;
            NewsContent content;
            if(obj == null){
                System.out.println("No object returned!");
            }
            else {
                synchronized (mContentItems) {
                    if (!obj.has("data")) {

                    } else {
                        try {
                            obj = obj.getJSONObject("data");
                            newsList = obj.getJSONArray("news");
                            for (int i = 0; i < newsList.length(); ++i) {
                                news = newsList.getJSONObject(i);
                                content = new NewsContent();
                                content.title = news.getString("title");
                                content.category = news.getString("category");
                                content.newsid = news.getLong("news_id");
                                if (news.getJSONArray("imgs").length() > 0) {
                                    content.imageurl = news.getJSONArray("imgs").getJSONObject(0).getString("url");
                                }
                                content.origin = news.getString("origin");
                                if(news.getJSONObject("source").has("url"))
                                    content.urlstr = news.getJSONObject("source").getString("url");
                                boolean crash = false;
                                int j;
                                for (j = 0; j < mContentItems.size(); ++j) {
                                    if (mContentItems.get(j).newsid == content.newsid) {
                                        crash = true;
                                        break;
                                    } else if (mContentItems.get(j).newsid < content.newsid) {
                                        crash = false;
                                        break;
                                    }
                                }
                                if (!crash)
                                    mContentItems.add(j, content);
                            }
                            writeContent();
                            mhandler.sendEmptyMessage(0);
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
        public JSONObject getResponse(){
            String tab_code = tabInfo.codedTitleAt(tab_order);
            String url = "http://assignment.crazz.cn/news/query?locale=en&category=" + tab_code;
            if(max_news_id != NEWEST){
                url+="&max_news_id="+max_news_id;
            }
            System.out.println("Getting response for " + tab_code);
            String res = "";
            InputStreamReader adp = null;
            BufferedReader in = null;
            URLConnection urlConn = null;
            try{
                URL tar = new URL(url);
                if(tar != null){
                    urlConn = tar.openConnection();
                    urlConn.setConnectTimeout(3000);
                    adp = new InputStreamReader(urlConn.getInputStream());
                    in = new BufferedReader(adp);
                    String line = null;
                    while((line = in.readLine())!=null){
                        res += line+"\n";
                    }
                }
            }
            catch(MalformedURLException e){
                System.out.println(e);
            }
            catch(IOException e){
                System.out.println(e);
            }
            finally {
                if(in != null){
                    try{
                        in.close();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            JSONObject json = null;
            try{
                System.out.println("Assigning values!");
                json = new JSONObject(res);
            }
            catch(JSONException j){
                System.out.println(j);
            }
            return json;
        }
        public void writeContent(){
            Cursor cursor;
            NewsContent current;
            final String label = tabInfo.codedTitleAt(tab_order);
            synchronized (app.database){
                for(int i = 0; i < mContentItems.size(); ++i){
                    current = mContentItems.get(i);
                    cursor = app.database.query(label, new String[]{"news_id", "title", "source_url", "image_url", "origin", "category", "favourite"},
                            "news_id=?", new String[]{String.format(Locale.getDefault(), "%d", current.newsid)}, null, null, "news_id");
                    if(cursor.getCount() == 0){
                        ContentValues values = new ContentValues();
                        values.put("news_id", current.newsid);
                        values.put("title", current.title);
                        values.put("source_url", current.urlstr);
                        values.put("image_url", current.imageurl);
                        values.put("origin", current.origin);
                        values.put("category", current.category);
                        values.put("favourite", current.favourite);
                        app.database.insert(label, null, values);
                    }
                    else{
                        ContentValues values = new ContentValues();
                        values.put("title", current.title);
                        values.put("source_url", current.urlstr);
                        values.put("image_url", current.imageurl);
                        values.put("origin", current.origin);
                        values.put("category", current.category);
                        values.put("favourite", current.favourite);
                        app.database.update(label, values, "news_id=?", new String[]{String.format(Locale.getDefault(), "%d", current.newsid)});
                    }
                }
            }
        }
    }


    class NewThread extends RefreshThread{
        public NewThread (){
            super(RefreshThread.NEWEST);
        }
        @Override
        public void run(){
            while(!tabInfo.tabReady){
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){}
            }
            JSONObject obj = getResponse();
            JSONArray newsList;
            JSONObject news;
            NewsContent content;
            if(obj == null){
                // No results from network, try using database
                Cursor cursor;
                synchronized (app.database){
                    final String label = tabInfo.codedTitleAt(tab_order);
                    cursor = app.database.query(label, new String[]{"news_id", "title", "source_url", "image_url", "origin", "category", "favourite"},
                            null, null, null, null, "news_id");
                    if(cursor.moveToFirst()){
                        for(int i = 0; i < cursor.getCount(); ++i){
                            content = new NewsContent();
                            content.newsid = cursor.getLong(0);
                            content.title = cursor.getString(1);
                            content.urlstr = cursor.getString(2);
                            content.imageurl = cursor.getString(3);
                            content.origin = cursor.getString(4);
                            content.category = cursor.getString(5);
                            content.favourite = cursor.getInt(6);
                            boolean crash = false;
                            int j;
                            for (j = 0; j < mContentItems.size(); ++j) {
                                if (mContentItems.get(j).newsid == content.newsid) {
                                    crash = true;
                                    break;
                                } else if (mContentItems.get(j).newsid < content.newsid) {
                                    crash = false;
                                    break;
                                }
                            }
                            if (!crash)
                                mContentItems.add(j, content);
                            cursor.move(1);
                        }
                        writeContent();
                        mhandler.sendEmptyMessage(0);
                    }
                    else{
                        return;
                    }
                }
            }
            else {
                synchronized (mContentItems) {
                    if (!obj.has("data")) {

                    } else {
                        try {
                            obj = obj.getJSONObject("data");
                            newsList = obj.getJSONArray("news");
                            for (int i = 0; i < newsList.length(); ++i) {
                                news = newsList.getJSONObject(i);
                                content = new NewsContent();
                                content.title = news.getString("title");
                                content.category = news.getString("category");
                                content.newsid = news.getLong("news_id");
                                if (news.getJSONArray("imgs").length() > 0) {
                                    content.imageurl = news.getJSONArray("imgs").getJSONObject(0).getString("url");
                                }
                                content.origin = news.getString("origin");
                                if(news.getJSONObject("source").has("url"))
                                    content.urlstr = news.getJSONObject("source").getString("url");
                                boolean crash = false;
                                int j;
                                for (j = 0; j < mContentItems.size(); ++j) {
                                    if (mContentItems.get(j).newsid == content.newsid) {
                                        crash = true;
                                        break;
                                    } else if (mContentItems.get(j).newsid < content.newsid) {
                                        crash = false;
                                        break;
                                    }
                                }
                                if (!crash)
                                    mContentItems.add(j, content);
                            }
                            writeContent();
                            mhandler.sendEmptyMessage(0);
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }


    public static RecyclerViewFragment newInstance(NewsTab tabs, int pos) {
        RecyclerViewFragment f = new RecyclerViewFragment();

        f.tabInfo = tabs;
        f.tab_order = tabs.getTabOrderFromVisibleOrder(pos);
        f.lastVisibleItem = 0;
        tabs.registerPage(tabs.titleAt(pos), f);
        f.app = tabs.getApp();

        System.out.println("New instance with title = "+tabs.titleAt(pos));
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshWidget.setRefreshing(true);
                // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                Thread thread = new Thread(new RefreshThread(RefreshThread.NEWEST));
                thread.start();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        mAdapter = new TestRecyclerViewAdapter(getActivity(), mContentItems, tab_order);

        //mAdapter = new RecyclerViewMaterialAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        System.out.println("Update message received");
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshWidget.setRefreshing(false);
                        break;

                }
            }
        };

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p/>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = RecyclerViewFragment.this.layoutManager.findLastVisibleItemPosition();
            }

            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {@link #SCROLL_STATE_IDLE},
             *                     {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    // 下拉获取更旧的新闻
                    mSwipeRefreshWidget.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    Thread thread = new Thread(new RefreshThread(mAdapter.getItemId(lastVisibleItem)));
                    thread.start();
                }
            }
        });

        //TODO: He added empty objects. We need to change this!

        Thread thread = new Thread(new NewThread());
        thread.start();
        /*
        {
            for (int i = 0; i < ITEM_COUNT; ++i) {
                mContentItems.add(new NewsContent());
            }
            mAdapter.notifyDataSetChanged();
        }
        */
    }
}
