package com.ihandy.a2014011425;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        */
        port = (TextView)findViewById((R.id.testport));
        Thread thread = new Thread(){
            public void run(){
                System.out.println(NewsTab.getResponse());
            }
        };
        thread.start();


        Button use = (Button)findViewById(R.id.option);
        use.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }



}
