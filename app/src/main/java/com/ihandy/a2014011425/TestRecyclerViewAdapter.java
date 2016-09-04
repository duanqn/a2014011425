package com.ihandy.a2014011425;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.net.URL;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NewsContent> contents;
    Handler imgLoadNotifier;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    Activity context;

    public TestRecyclerViewAdapter(Activity _context, List<NewsContent> contents) {
        context = _context;
        this.contents = contents;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CardViewHolder cholder = (CardViewHolder)holder;
        final NewsContent c = contents.get(position);
        Thread loadThread;
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                cholder.title.setText(c.title);
                cholder.detail.setText(c.origin);
                if(c.imageurl != null){
                    loadThread = new Thread(){
                        @Override
                        public void run(){

                            String title = c.title;
                            Message msg = new Message();

                            msg.what = 1;
                            msg.obj = title;
                            imgLoadNotifier.sendMessage(msg);
                            Bitmap bitmap = getHttpBitmap(c.imageurl);
                            Message msg2 = new Message();
                            if(bitmap != null){

                                msg2.what = 0;
                                msg2.obj = bitmap;
                                imgLoadNotifier.sendMessage(msg2);
                            }
                        }
                        public Bitmap getHttpBitmap(String url) {
                            URL myFileUrl = null;
                            Bitmap bitmap = null;
                            try {
                                myFileUrl = new URL(url);
                            } catch (MalformedURLException e) {
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
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            return bitmap;
                        }
                    };
                    imgLoadNotifier = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case 0:
                                    cholder.detail.setText(c.origin);
                                    cholder.img.setImageBitmap((Bitmap)msg.obj);
                                    cholder.img.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    cholder.detail.setText((String)msg.obj);
                                    cholder.img.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                    };
                    loadThread.start();
                }
                break;
        }
        cholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("targetURL", c.urlstr);
                intent.setClass(context.getApplicationContext(), NewsViewActivity.class);
                context.startActivity(intent);
            }
        });
    }
}