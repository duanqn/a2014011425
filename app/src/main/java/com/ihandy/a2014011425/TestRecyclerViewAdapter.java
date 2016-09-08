package com.ihandy.a2014011425;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.net.URL;
import java.util.Locale;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NewsContent> contents;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private int tab_order;
    Activity context;

    public TestRecyclerViewAdapter(Activity _context, List<NewsContent> contents, int order) {
        context = _context;
        this.contents = contents;
        tab_order = order;
        super.setHasStableIds(true);
    }


    @Override
    //TODO: notice!
    public int getItemViewType(int position) {
        switch (position) {
            /*
            case 0:
                return TYPE_HEADER;
                */
            //We don't use TYPE_HEADER
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new CardViewHolder(view) {
                };

            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_custom, parent, false);
                return new CardViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CardViewHolder cholder = (CardViewHolder)holder;
        final NewsContent c = contents.get(position);
        Thread loadThread;
        NewsApp app = (NewsApp) context.getApplication();
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                cholder.title.setText(c.title);
                cholder.detail.setText(c.origin);
                if(c.pic!=null){
                    //load from memory
                    cholder.detail.setText(c.origin);
                    cholder.img.setImageBitmap(c.pic);
                    cholder.img.setVisibility(View.VISIBLE);
                    cholder.img.invalidate();
                    break;
                }
                //c.pic == null
                synchronized (app.database){
                    String tabLabel = app.share_tabs.codedTitleAt(tab_order);
                    Cursor cursor = app.database.query(tabLabel, new String[]{"news_id", "image_store"}, "news_id=?",
                            new String[]{String.format(Locale.getDefault(), "%d", c.newsid)}, null, null, "news_id");
                    if(cursor.moveToFirst()){
                        byte[] in = cursor.getBlob(1);
                        if(in!=null){
                            c.pic = BitmapFactory.decodeByteArray(in, 0, in.length);
                            cholder.detail.setText(c.origin);
                            cholder.img.setImageBitmap(c.pic);
                            cholder.img.setVisibility(View.VISIBLE);
                            cholder.img.invalidate();
                            break;
                        }
                    }
                }
                //unable to load from database
                if(c.imageurl != null){
                    final Handler imgLoadNotifier = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case 0:
                                    cholder.detail.setText(c.origin);
                                    cholder.img.setImageBitmap((Bitmap)msg.obj);
                                    cholder.img.setVisibility(View.VISIBLE);
                                    cholder.img.invalidate();
                                    break;
                                case 1:
                                    cholder.detail.setText((String)msg.obj);
                                    cholder.img.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                    };
                    loadThread = new Thread(){
                        @Override
                        public void run(){

                            String title = c.title;
                            Message msg = new Message();

                            msg.what = 1;
                            msg.obj = String.format("%d", position);
                            imgLoadNotifier.sendMessage(msg);
                            Bitmap bitmap = getHttpBitmap(c.imageurl);
                            Message msg2 = new Message();
                            if(bitmap != null){

                                msg2.what = 0;
                                msg2.obj = bitmap;
                                imgLoadNotifier.sendMessage(msg2);
                                c.pic = bitmap;
                                NewsApp app = (NewsApp) context.getApplication();
                                String label = app.share_tabs.codedTitleAt(tab_order);
                                synchronized (app.database){
                                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                                    ContentValues values = new ContentValues();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                                    values.put("image_store", os.toByteArray());
                                    app.database.update(label, values, "news_id=?", new String[]{String.format(Locale.getDefault(), "%d", c.newsid)});
                                    if(c.favourite == NewsContent.FAVOURITE){
                                        app.database.update("favourite_news", values, "news_id=?",
                                                new String[]{String.format(Locale.getDefault(), "%d", c.newsid)});
                                    }
                                }
                            }
                        }
                        public Bitmap getHttpBitmap(String url) {
                            URL myFileUrl = null;
                            Bitmap bitmap = null;
                            try {
                                myFileUrl = new URL(url);
                            } catch (MalformedURLException e) {
                                System.out.println(e);
                                e.printStackTrace();
                                return null;
                            }
                            try {
                                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                                conn.setConnectTimeout(1000);
                                conn.setDoInput(true);
                                conn.connect();
                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                                is.close();
                            } catch(SocketTimeoutException e){
                                //TODO: replace it with a red cross
                                return null;
                            }
                            catch (FileNotFoundException e){
                                return null;
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return bitmap;
                        }
                    };
                    loadThread.start();
                }
                break;
        }
        cholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c.urlstr!=null) {
                    Intent intent = new Intent();
                    intent.putExtra("content", c);
                    intent.putExtra("order", tab_order);
                    intent.setClass(context.getApplicationContext(), NewsViewActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return contents.get(position).newsid;
    }
}