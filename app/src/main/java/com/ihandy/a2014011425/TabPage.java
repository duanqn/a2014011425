package com.ihandy.a2014011425;

/**
 * Created by max on 16-8-27.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.view.*;
import android.widget.*;
import android.app.Activity;

import java.net.MalformedURLException;
import java.net.URL;

public class TabPage extends Fragment{
    int[] imageId = new int[] { R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06, R.drawable.img07, R.drawable.img08 }; // 定义并初始化保存图片id的数组
    String[] title = new String[] { "保密设置", "安全", "系统设置", "上网", "我的文档", "GPS导航", "我的音乐", "E-mail" }; // 定义并初始化保存列表项文字的数组

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;
        MyAdapter(Context context){
            myInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        class ViewHolder{
            public ImageView imgView;
            public TextView txtView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = myInflater.inflate(R.layout.items, null);
                holder = new ViewHolder();
                holder.imgView = (ImageView) convertView.findViewById(R.id.image);
                holder.txtView = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imgView.setImageResource(imageId[position]);
            holder.txtView.setText(title[position]);
            return convertView;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        long t = System.currentTimeMillis();
        String addr = "http://assignment.crazz.cn/news/en/category?timestamp=" + t;
        try{
            URL server = new URL(addr);
        }
        catch(MalformedURLException e){
            System.out.println(e);
        }
        //TODO
        View view = inflater.inflate(R.layout.page_tab, container, false);
        ListView listview = (ListView) view.findViewById(R.id.listview1);
        MyAdapter adp = new MyAdapter(getActivity());
        listview.setAdapter(adp);
        return view;
    }
}
