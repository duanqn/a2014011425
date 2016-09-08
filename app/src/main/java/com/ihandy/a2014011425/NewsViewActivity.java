package com.ihandy.a2014011425;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

public class NewsViewActivity extends Activity {
    NewsContent pageContent;
    //Handler newsFetcher;
    WebView webView;
    ImageButton button_back;
    ImageButton button_favourite;
    ImageButton button_share;
    int net_access_count;
    int tab_order;
    boolean favourite_changed;
    public NewsViewActivity(){
        net_access_count = 0;
        favourite_changed = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        setContentView(R.layout.activity_news_view);
        Intent intent = getIntent();
        pageContent = intent.getParcelableExtra("content");
        tab_order = intent.getIntExtra("order", -1);
        String newsurl = pageContent.urlstr;

        NewsApp app = (NewsApp)getApplication();
        synchronized (app.database){
            if(app.database.query("favourite_news", new String[]{"news_id"}, "news_id=?",
                    new String[]{String.format(Locale.getDefault(), "%d", pageContent.newsid)}, null, null, "news_id").getCount() == 0){
                pageContent.favourite = NewsContent.NOT_FAVOURITE;
            }
            else
                pageContent.favourite = NewsContent.FAVOURITE;
        }

        webView = (WebView)findViewById(R.id.news_view_webView);
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(newsurl);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(net_access_count == 0) {
                    view.loadUrl(url);
                    ++net_access_count;
                    return true;
                }
                else {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    ++net_access_count;
                    return true;
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        final TextView content = (TextView)findViewById(R.id.news_view_text);
        content.setText(newsurl);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                TextView content = (TextView)findViewById(R.id.news_view_text);
                if (newProgress == 100) {
                    // 网页加载完成
                    content.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                } else {
                    // 加载中
                    content.setText(String.format(Locale.getDefault(), "Loading... %d%%", newProgress));
                }

            }
        });
        button_back = (ImageButton)findViewById(R.id.actionbar_news_view_backbtn);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        button_favourite = (ImageButton)findViewById(R.id.actionbar_news_view_favouritebtn);
        switch(pageContent.favourite){
            case NewsContent.FAVOURITE:
                button_favourite.setImageDrawable(getResources().getDrawable(R.drawable.red_heart));
                break;
            case NewsContent.NOT_FAVOURITE:
                button_favourite.setImageDrawable(getResources().getDrawable(R.drawable.blue_favorite));
                break;
        }
        button_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(pageContent.favourite){
                    case NewsContent.FAVOURITE:
                        pageContent.favourite = NewsContent.NOT_FAVOURITE;
                        button_favourite.setImageDrawable(getResources().getDrawable(R.drawable.blue_favorite));
                        button_favourite.invalidate();
                        Toast.makeText(getApplicationContext(), "Removed from favourite list", Toast.LENGTH_SHORT).show();
                        break;
                    case NewsContent.NOT_FAVOURITE:
                        pageContent.favourite = NewsContent.FAVOURITE;
                        button_favourite.setImageDrawable(getResources().getDrawable(R.drawable.red_heart));
                        button_favourite.invalidate();
                        Toast.makeText(getApplicationContext(), "Added to favourite list", Toast.LENGTH_SHORT).show();
                        break;
                }
                favourite_changed = !favourite_changed;
            }
        });
        button_share = (ImageButton)findViewById(R.id.actionbar_news_view_sharebtn);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String []menuList = new String[]{"Share title and URL", "Share picture"};
                AlertDialog alertDialog = new AlertDialog.Builder(NewsViewActivity.this)
                        .setTitle("Choose content to share")
                        .setItems(menuList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent shareIntent = new Intent();
                                switch (which){
                                    case 0:
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, NewsViewActivity.this.pageContent.title+"  from "
                                                +NewsViewActivity.this.pageContent.urlstr);
                                        shareIntent.setType("text/plain");
                                        startActivity(Intent.createChooser(shareIntent, "Share to"));
                                        break;
                                    case 1:
                                        if(NewsViewActivity.this.pageContent.pic==null){
                                            break;
                                        }
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        File imageDir = getApplicationContext().getFilesDir();
                                        if(!imageDir.exists()){
                                            imageDir.mkdirs();
                                        }
                                        File tmpImage = new File(imageDir, "tmp.png");
                                        FileOutputStream os = null;
                                        try{
                                            os = new FileOutputStream(tmpImage);
                                            NewsViewActivity.this.pageContent.pic.compress(Bitmap.CompressFormat.PNG, 100, os);
                                            os.flush();
                                        }catch (IOException e){
                                            Toast.makeText(NewsViewActivity.this, "Unable to share picture", Toast.LENGTH_SHORT).show();
                                        }
                                        finally {
                                            if(os != null){
                                                try{
                                                    os.close();
                                                }catch (IOException e){
                                                    System.out.println(e);
                                                }
                                            }
                                        }
                                        if(tmpImage.exists()) {
                                            Uri imageUri = Uri.fromFile(tmpImage);
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                            shareIntent.setType("image/*");
                                            startActivity(Intent.createChooser(shareIntent, "Share to"));
                                        }
                                        break;
                                }
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }


    public String getResponse(){
        String newsurl = pageContent.urlstr;
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

    @Override
    protected void onDestroy() {
        if(favourite_changed){
            NewsApp app = (NewsApp) getApplication();
            synchronized (app.database){
                String label = app.share_tabs.codedTitleAt(tab_order);
                app.database.execSQL("update "+label+" set favourite="+pageContent.favourite+" where news_id=" + pageContent.newsid);

                //TODO: add to a separate table
                if(pageContent.favourite == NewsContent.FAVOURITE){
                    ContentValues values = new ContentValues();
                    values.put("codedTab", label);
                    values.put("news_id", pageContent.newsid);
                    values.put("title", pageContent.title);
                    values.put("source_url", pageContent.urlstr);
                    values.put("image_url", pageContent.imageurl);
                    values.put("origin", pageContent.origin);
                    values.put("category", pageContent.category);
                    if(pageContent.pic!=null) {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        pageContent.pic.compress(Bitmap.CompressFormat.PNG, 100, os);
                        values.put("image_store", os.toByteArray());
                    }
                    app.database.insert("favourite_news", null, values);
                    app.downloadPage(pageContent.newsid, pageContent.urlstr);
                }
                else{
                    app.database.execSQL("delete from favourite_news where news_id="+pageContent.newsid);
                }
            }
        }
        super.onDestroy();
    }
}
