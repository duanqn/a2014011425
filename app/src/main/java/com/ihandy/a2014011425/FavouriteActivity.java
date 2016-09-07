package com.ihandy.a2014011425;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends Activity {
    FavouriteAdapter mAdapter;
    ArrayList<NewsContent> mContentItems;
    ArrayList<String> tabName;
    NewsApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ListView list = (ListView)findViewById(R.id.favourite_list);

        app = (NewsApp)getApplication();
        mContentItems = new ArrayList<>();
        tabName = new ArrayList<>();
        synchronized (app.database){
            Cursor cursor = app.database.query("favourite_news", new String[]{"news_id", "title", "source_url", "image_url", "origin", "category", "codedTab"},
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
                mContentItems.add(current);
                tabName.add(cursor.getString(6));
                cursor.move(1);
            }
        }
        mAdapter = FavouriteAdapter.getNewInstance(this, mContentItems);
        list.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        for(int i = 0; i < mContentItems.size(); ++i){
            if(mContentItems.get(i).favourite == NewsContent.NOT_FAVOURITE){
                synchronized (app.database){
                    app.database.execSQL("delete from favourite_news where news_id="+mContentItems.get(i).newsid);
                    app.database.execSQL("update "+tabName.get(i)+" set favourite=0 where news_id="+mContentItems.get(i).newsid);
                }
            }
        }
        super.onDestroy();
    }
}
