package com.ihandy.a2014011425.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */

//TODO: Manually overwrite
public class RecyclerViewFragment extends Fragment {

    static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 5;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int tab_order;
    private NewsTab tabInfo;
    private List<NewsContent> mContentItems = new ArrayList<>();
    private Handler mhandler;

    public static RecyclerViewFragment newInstance(NewsTab tabs, int pos) {
        RecyclerViewFragment f = new RecyclerViewFragment();
        f.tab_order = pos;
        f.tabInfo = tabs;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        mAdapter = new TestRecyclerViewAdapter(mContentItems);

        //mAdapter = new RecyclerViewMaterialAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //TODO: He added empty objects. We need to change this!
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                // no need to switch msg.what
                mAdapter.notifyDataSetChanged();
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run(){
                while(!tabInfo.tabReady){
                    try{
                        sleep(50);
                    }catch(InterruptedException e){}
                }
                JSONObject obj = getResponse();
                JSONArray newsList;
                JSONObject news;
                NewsContent content;
                try{
                    if(obj == null){
                        System.out.println("No object returned!");
                    }
                    else {
                        obj = obj.getJSONObject("data");
                        newsList = obj.getJSONArray("news");
                        for (int i = 0; i < newsList.length(); ++i) {
                            news = newsList.getJSONObject(i);
                            content = new NewsContent();
                            content.title = news.getString("title");
                            content.category = news.getString("category");
                            content.newsid = news.getLong("news_id");
                            if(news.getJSONArray("imgs").length() > 0){
                                content.imageurl = news.getJSONArray("imgs").getJSONObject(0).getString("url");
                            }
                            content.origin = news.getString("origin");
                            content.urlstr = news.getJSONObject("source").getString("url");
                            mContentItems.add(content);
                        }
                        mhandler.sendEmptyMessage(0);
                    }
                }catch(JSONException e){
                    System.out.println(e);
                }
            }
            public JSONObject getResponse(){
                String tab_code = tabInfo.codedTitleAt(tab_order);
                String url = "http://assignment.crazz.cn/news/query?locale=en&category=" + tab_code;
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
        };
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
