package com.ihandy.a2014011425;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView port;
    Handler handler;
    final int NEWS_TAB_UPDATE = 1;
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
