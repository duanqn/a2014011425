package com.ihandy.a2014011425;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Test");

        mViewPager = (MaterialViewPager)findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        mViewPager.getViewPager().setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

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
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
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
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
