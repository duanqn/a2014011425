package com.ihandy.a2014011425;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by max on 16-8-28.
 */
public class NewsTab {
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> codedTitleList = new ArrayList<>();
    private JSONObject obj;
    public synchronized void parseTab(){
        String key, value;
        try{
            obj = obj.getJSONObject("data");
            obj = obj.getJSONObject("categories");
            Iterator keylist = obj.keys();
            while(keylist.hasNext()){
                key = (String)keylist.next();
                value = obj.getString(key);
                titleList.add(value);
                codedTitleList.add(key);
            }
        }catch(JSONException e){
            System.out.println(e);
        }
    }
    NewsTab(){
    }
    public ArrayList<String> getTitle(){
        return titleList;
    }
    public ArrayList<String> getCodedTitle(){
        return codedTitleList;
    }
    public String titleAt(int pos){
        String res = null;
        try{
            res = titleList.get(pos);
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return res;
    }
    public int getTitleNum(){
        return titleList.size();
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
            URL tar = new URL(url);
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
            obj = new JSONObject(res);
        }
        catch(JSONException j){
            System.out.println(j);
        }
        return res;
    }
}
