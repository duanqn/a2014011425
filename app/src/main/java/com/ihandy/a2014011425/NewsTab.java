package com.ihandy.a2014011425;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import com.ihandy.a2014011425.fragment.RecyclerViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by max on 16-8-28.
 */
public class NewsTab{
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> titleListVisible = new ArrayList<>();
    public HashMap<String, RecyclerViewFragment> map = new HashMap<>();
    private ArrayList<String> codedTitleList = new ArrayList<>();
    private JSONObject obj;
    public boolean tabReady;
    private NewsApp app;
    public void parseTab(){
        synchronized (this) {
            String key;
            String value;
            if (obj == null)
                return;
            try {
                obj = obj.getJSONObject("data");
                obj = obj.getJSONObject("categories");
                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    key = iter.next();
                    value = obj.getString(key);
                    codedTitleList.add(key);
                    titleList.add(value);
                    titleListVisible.add(value);
                }
                tabReady = true;
            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }
    NewsTab(){
        tabReady = false;
    }
    public ArrayList<String> getTitle(){
        return titleList;
    }
    public ArrayList<String> getVisibleTitle(){
        return titleListVisible;
    }
    public ArrayList<String> getCodedTitle(){
        return codedTitleList;
    }

    public String titleAt(int pos){
        String res = null;
        try{
            res = titleListVisible.get(pos);
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return res;
    }
    public String codedTitleAt(int pos){
        String res = null;
        try{
            res = codedTitleList.get(pos);
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     *
     * @return number of VISIBLE title
     */
    public int getTitleNum(){
        return titleListVisible.size();
    }
    public String getResponse(){
        long mills = System.currentTimeMillis();
        String url = "http://assignment.crazz.cn/news/en/category" + "?timestamp=" + mills;
        System.out.println("Getting response from " + url);
        String res = "";
        InputStreamReader adp = null;
        BufferedReader in = null;
        URLConnection urlConn = null;
        try{
            URL tar = null;
            tar = new URL(url);
            if(tar != null){
                urlConn = tar.openConnection();
                urlConn.setConnectTimeout(2000);
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
        catch(SocketTimeoutException e){
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
        if(res!=null) {
            try {
                obj = new JSONObject(res);
            } catch (JSONException j) {
                System.out.println(j);
            }
        }
        return res;
    }
    public void registerPage(String title, RecyclerViewFragment f){
        map.put(title, f);
    }
    public void setApp(NewsApp _app){
        app = _app;
    }
    public NewsApp getApp(){
        return app;
    }
    public int getTabOrderFromVisibleOrder(int visibleOrder){
        String title = titleAt(visibleOrder);
        for(int i = 0; i < titleList.size(); ++i){
            if(titleList.get(i).equals(title))
                return i;
        }
        return -1;
    }
    public boolean makeTabInvisible(int position){
        if(position >= 0 && position < titleList.size()) {
            titleListVisible.remove(position);
            return true;
        }
        else
            return false;
    }
    public boolean makeTabVisible(int position_in_invisible){
        int invisible_count = 0;
        int visible_ptr = 0;
        for(int i = 0; i < titleList.size(); ++i){
            if((visible_ptr >= titleListVisible.size()) || !titleList.get(i).equals(titleListVisible.get(visible_ptr))){
                // title is invisble
                ++invisible_count;
                if(invisible_count == position_in_invisible+1){
                    titleListVisible.add(visible_ptr, titleList.get(i));
                    return true;
                }
            }
            else
                ++visible_ptr;
        }
        return false;
    }
    public boolean getTab(){
        getResponse();
        if(obj != null){
            parseTab();
            Cursor cursor;
            String s;
            synchronized (app.database) {
                cursor = app.database.query("tabs", new String[]{"tab_order", "codedTitle", "title", "watched"}, null, null, null, null, "tab_order");
                if(cursor.moveToFirst()){
                    int index;
                    for(int i = 0; i < cursor.getCount(); ++i){
                        s = cursor.getString(2);
                        index = titleList.indexOf(s);
                        if(index == -1){
                            app.database.execSQL("delete from tabs where title=?", new Object[]{s});
                        }
                        else{
                            if(cursor.getInt(3)==0){
                                titleListVisible.remove(s); //Avoid accessing by index
                            }
                        }
                        cursor.move(1);
                    }
                    for(int i = 0; i < codedTitleList.size(); ++i){
                        cursor = app.database.query("tabs", new String[]{"tab_order", "codedTitle", "title", "watched"}, "codedTitle=?", new String[]{codedTitleList.get(i)}, null, null, "tab_order");
                        if(cursor.getCount()==0){
                            //tab not recorded in database
                            ContentValues value = new ContentValues();
                            value.put("title", titleList.get(i));
                            value.put("codedTitle", codedTitleList.get(i));
                            value.put("tab_order", i);
                            value.put("watched", 1);
                            app.database.insert("tabs", null, value);
                        }
                    }
                }
                else{
                    app.database.execSQL("drop table tabs");
                    app.database.execSQL("create table tabs(" +
                            "tab_order integer primary key, " +
                            "codedTitle text, " +
                            "title text, " +
                            "watched integer)");
                    for(int i = 0; i < titleList.size(); ++i){
                        ContentValues value = new ContentValues();
                        value.put("title", titleList.get(i));
                        value.put("codedTitle", codedTitleList.get(i));
                        value.put("tab_order", i);
                        value.put("watched", titleListVisible.contains(titleList.get(i))?1:0);
                        app.database.insert("tabs", null, value);
                    }
                }
                for(int i = 0; i < titleList.size(); ++i){
                    app.database.execSQL("create table if not exists " + codedTitleAt(i)
                            + " (news_id text primary key, " +
                            "title text, " +
                            "source_url text, " +
                            "image_url text, " +
                            "origin text, " +
                            "category text, " +
                            "image_store blob, "+
                            "favourite integer)");    //store news_id as double to ensure precision //7 columns
                }
            }
            return true;
        }
        else{
            return getTabFromDatabase();
        }
    }
    public boolean getTabFromDatabase(){
        Cursor cursor;
        synchronized (app.database) {
            cursor = app.database.query("tabs", new String[]{"tab_order", "codedTitle", "title", "watched"}, null, null, null, null, "tab_order");
            String s;
            int t;
            if(cursor.moveToFirst()){
                for(int i = 0; i < cursor.getCount(); ++i){
                    s = cursor.getString(1);    //0 for tab_order
                    codedTitleList.add(s);
                    s = cursor.getString(2);
                    titleList.add(s);
                    t = cursor.getInt(3);
                    if(t == 1)
                        titleListVisible.add(s);
                    cursor.move(1);
                }
                cursor.close();
                return tabReady = true;
            }
            else
                return false;
        }
    }
    public String lookupCodedTitle(String title){
        int index = titleList.indexOf(title);
        return codedTitleAt(index);
    }
}
