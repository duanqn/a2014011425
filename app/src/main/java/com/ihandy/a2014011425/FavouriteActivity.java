package com.ihandy.a2014011425;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends Activity {
    FavouriteAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ListView list = (ListView)findViewById(R.id.favourite_list);

        NewsApp app = (NewsApp)getApplication();
        ArrayList<NewsContent> favouriteList = new ArrayList<>();
        synchronized (app.database){
            Cursor cursor = app.database.query("favourite_news", new String[]{"news_id", "title", "source_url", "image_url", "origin", "category"},
                     null, null, null, null, "news_id");
            cursor.moveToFirst();
            NewsContent current;
            for(int i = 0; i < cursor.getCount(); ++i){
                current = new NewsContent();
                current.newsid = cursor.getLong(0);
                current.title = cursor.getString(1);
                current.urlstr = cursor.getString(2);
                current.imageurl = cursor.getString(3);
                current.origin = cursor.getString(4);
                current.category = cursor.getString(5);
                current.favourite = NewsContent.FAVOURITE;
                favouriteList.add(current);
            }
        }
        mAdapter = FavouriteAdapter.getNewInstance(this, favouriteList);
        list.setAdapter(mAdapter);
    }

}
