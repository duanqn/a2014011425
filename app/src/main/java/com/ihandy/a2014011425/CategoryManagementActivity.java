package com.ihandy.a2014011425;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

public class CategoryManagementActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);
        ListView list = (ListView)findViewById(R.id.category_management_list);
        CategoryManagementAdapter mAdapter = CategoryManagementAdapter.getNewInstance(this, (NewsApp)getApplication());
        list.setAdapter(mAdapter);
    }



}
