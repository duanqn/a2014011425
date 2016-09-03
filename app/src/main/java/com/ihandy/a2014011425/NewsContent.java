package com.ihandy.a2014011425;

import android.support.annotation.Nullable;

/**
 * Created by max on 16-9-2.
 */
public class NewsContent {
    public String title;
    public String urlstr;
    @Nullable
    public String imageurl = null;
    public long newsid;
    public String category;
    public String origin;
    public NewsContent(){
        title = "一切反动派都是纸老虎";
        urlstr = "";
    }
    public NewsContent(String _title){
        title = _title;
    }
}
