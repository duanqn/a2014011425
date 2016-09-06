package com.ihandy.a2014011425;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ihandy.a2014011425.materialviewpager.MaterialViewPager;
import com.ihandy.a2014011425.materialviewpager.header.HeaderDesign;


public class MainActivity extends DrawerActivity {
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ViewPagerAdapter mAdapter;
    private NewsTab tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");   //No title

        mViewPager = (MaterialViewPager)findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        final NewsApp mApp = (NewsApp) getApplication();
        mAdapter = ViewPagerAdapter.getNewInstance(mApp, getSupportFragmentManager());
        mApp.setGlobalViewPagerAdapter(mAdapter);
        mViewPager.getViewPager().setAdapter(mAdapter);
        tabs = mAdapter.getTabs();

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    /*
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.green,
                            "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.blue,
                            "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.cyan,
                            "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.red,
                            "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                            */
                    default:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    //TODO: use chrome Momentum pics
                }

                //execute others actions if needed (ex : modify your header logo)

                //return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                DrawerLayout drawer;
                Intent intent;
                switch(id){
                    case R.id.nav_about_me:
                        drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        intent = new Intent();
                        intent.setClass(MainActivity.this, AboutMeActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.nav_category_management:
                        drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        intent = new Intent();
                        intent.setClass(MainActivity.this, CategoryManagementActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.nav_favourite:
                        drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        intent = new Intent();
                        intent.setClass(MainActivity.this, FavouriteActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.nav_clear:
                        //drop all tables!
                        synchronized (mApp.database){
                            Cursor cursor = mApp.database.query("tabs", new String[]{"tab_order", "codedTitle", "title", "watched"}, null, null, null, null, "tab_order");
                            cursor.moveToFirst();
                            for(int i = 0; i < cursor.getCount(); ++i){
                                String label = cursor.getString(1);
                                mApp.database.execSQL("drop table "+label);
                                cursor.move(1);
                            }
                            mApp.database.execSQL("drop table tabs");
                            finish();
                        }
                        break;
                }

                return true;
            }
        });
        /*
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        */
        /*
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case NEWS_TAB_UPDATE:
                        port.setText((String)msg.obj);;
                        break;
                    default:
                }
            }
        };
        port = (TextView)findViewById((R.id.testport));
        Thread thread = new Thread(){
            @Override
            public void run(){
                NewsTab nt = new NewsTab();
                nt.getResponse();
                nt.parseTab();
                int n = nt.getTitleNum();
                String res = "";
                for(int i = 0; i < n; ++i){
                    res += nt.titleAt(i)+"\n";
                }
                Message msg = new Message();
                msg.what = NEWS_TAB_UPDATE;
                msg.obj = res;
                MainActivity.this.handler.sendMessage(msg);
            }
        };
        thread.start();
        */
        /*
        Button use = (Button)findViewById(R.id.next);
        use.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        */
    }



}
