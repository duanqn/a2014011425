package com.ihandy.a2014011425;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
        String t = "{\"data\": {\"categories\": {     \"business\": \"Business\",      \"entertainment\": \"Entertainment\",      \"health\": \"Health\",      \"more top stories\": \"More Top Stories\",      \"national\": \"India\",      \"science\": \"Science\",      \"sports\": \"Sports\",      \"technology\": \"Technology\",      \"top_stories\": \"Top Stories\",      \"world\": \"World\"    },    \"locale\": \"en_in\"  },  \"meta\": {    \"code\": 200  }}";
        JSONTokener token = new JSONTokener(t);
        try{
            obj = (JSONObject)token.nextValue();
        }
        catch(JSONException e){

        }
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
}
