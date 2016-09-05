package com.ihandy.a2014011425;

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
    public void parseTab(){
        String key;
        String value;
        try{
            obj = obj.getJSONObject("data");
            obj = obj.getJSONObject("categories");
            Iterator<String> iter = obj.keys();
            while(iter.hasNext()){
                key = iter.next();
                value = obj.getString(key);
                codedTitleList.add(key);
                titleList.add(value);
                titleListVisible.add(value);
            }
            tabReady = true;
        }catch(JSONException e){
            System.out.println(e);
        }
    }
    NewsTab(){
        tabReady = false;
    }
    public ArrayList<String> getTitle(){
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
        try{
            System.out.println("Assigning values! "+res);
            obj = new JSONObject(res);
        }
        catch(JSONException j){
            System.out.println(j);
        }
        return res;
    }
    public void registerPage(String title, RecyclerViewFragment f){
        map.put(title, f);
    }
    public boolean makeTabInvisible(int position){
        for(int i = 0; i < getTitleNum(); ++i){
            System.out.println(titleAt(i));
        }
        if(position >= 0 && position < titleList.size()) {
            titleListVisible.remove(position);
            for(int i = 0; i < getTitleNum(); ++i){
                System.out.println(titleAt(i));
            }
            return true;
        }
        else
            return false;
    }
}
