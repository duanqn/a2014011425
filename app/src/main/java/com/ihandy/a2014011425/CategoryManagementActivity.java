package com.ihandy.a2014011425;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class CategoryManagementActivity extends Activity {
    ListView listup, listdown;
    NewsApp mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);
        mApp = (NewsApp) getApplication();
        listup = (ListView)findViewById(R.id.category_management_list_watched);
        listdown = (ListView)findViewById(R.id.category_management_list_unwatched);
        WatchedCategoryManagementAdapter mAdapter1 = WatchedCategoryManagementAdapter.getNewInstance(this, (NewsApp)getApplication());
        listup.setAdapter(mAdapter1);
        UnwatchedCategoryManagementAdapter mAdapter2 = UnwatchedCategoryManagementAdapter.getNewInstance(this, (NewsApp)getApplication());
        mAdapter2.bind(mAdapter1);
        listdown.setAdapter(mAdapter2);
        ImageButton button_back = (ImageButton)findViewById(R.id.actionbar_category_management_backbtn);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        NewsTab tabs = mApp.share_tabs;
        String s;
        synchronized (mApp.database){
            for(int i = 0; i < tabs.getTitle().size(); ++i){
                s = tabs.getTitle().get(i);
                int watched = tabs.getVisibleTitle().contains(s)?1:0;
                mApp.database.execSQL("update tabs set watched=? where title=?", new Object[]{new Integer(watched), s});
            }
        }
        super.onDestroy();
    }
}
