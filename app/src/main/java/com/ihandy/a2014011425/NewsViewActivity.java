package com.ihandy.a2014011425;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class NewsViewActivity extends Activity {
    public String newsurl;
    //Handler newsFetcher;
    WebView webView;
    public NewsViewActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_news_view);
        win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.red_heart);
        Intent intent = getIntent();
        newsurl = intent.getStringExtra("targetURL");


        webView = (WebView)findViewById(R.id.news_view_webView);
        webView.loadUrl(newsurl);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        TextView content = (TextView)findViewById(R.id.news_view_text);
        content.setText(newsurl);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                TextView content = (TextView)findViewById(R.id.news_view_text);
                if (newProgress == 100) {
                    // 网页加载完成
                    content.setVisibility(View.INVISIBLE);
                } else {
                    // 加载中
                    content.setText(String.format(Locale.getDefault(), "Loading... %d%%", newProgress));
                }

            }
        });
        /*
        newsFetcher = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case 0:
                        TextView content = (TextView)findViewById(R.id.news_view_text);
                        content.setText((String)msg.obj);
                        break;
                }
            }
        };
        Thread thread = new Thread(){
            public void run(){
                Message msg = new Message();
                msg.what = 0;
                msg.obj = getResponse();
                newsFetcher.sendMessage(msg);
            }
        };
        thread.start();
        */
        //content.setText(getResponse());
    }


    public String getResponse(){
        System.out.println("Getting news from " + newsurl);
        InputStreamReader adp = null;
        BufferedReader in = null;
        URLConnection urlConn = null;
        String res = "";
        try{
            URL tar = new URL(newsurl);
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
        return res;
    }
}
