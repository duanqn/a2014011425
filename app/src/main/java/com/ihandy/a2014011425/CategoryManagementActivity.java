package com.ihandy.a2014011425;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class CategoryManagementActivity extends Activity {
    ListView listup, listdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);


        listup = (ListView)findViewById(R.id.category_management_list_watched);
        listdown = (ListView)findViewById(R.id.category_management_list_unwatched);
        WatchedCategoryManagementAdapter mAdapter1 = WatchedCategoryManagementAdapter.getNewInstance(this, (NewsApp)getApplication());
        listup.setAdapter(mAdapter1);
        UnwatchedCategoryManagementAdapter mAdapter2 = UnwatchedCategoryManagementAdapter.getNewInstance(this, (NewsApp)getApplication());
        mAdapter2.bind(mAdapter1);
        listdown.setAdapter(mAdapter2);
    }



}
