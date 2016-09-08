package com.ihandy.a2014011425;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by max on 16-9-7.
 */
public class FavouriteAdapter extends BaseAdapter {
    public ArrayList<NewsContent> mContentItems;
    public ArrayList<String> correspondingTabs;
    public NewsTab tablist;
    private Context context;

    public static FavouriteAdapter getNewInstance(Context _context, ArrayList<NewsContent> list, ArrayList<String> tabs, NewsTab newsTab){
        FavouriteAdapter adp = new FavouriteAdapter();
        adp.context = _context;
        adp.mContentItems = list;
        adp.correspondingTabs = tabs;
        adp.tablist = newsTab;
        return adp;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mContentItems.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mContentItems.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return mContentItems.get(position).newsid;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card_favourite, null);
        }
        final NewsContent currentContent = mContentItems.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.favourite_item_title);
        textView.setText(currentContent.title);
        TextView category = (TextView) convertView.findViewById(R.id.favourite_item_category);
        category.setText(currentContent.category);
        TextView origin = (TextView) convertView.findViewById(R.id.favourite_item_origin);
        origin.setText(currentContent.origin);
        ImageView img = (ImageView) convertView.findViewById(R.id.favourite_item_pic);
        if(currentContent.pic!=null){
            img.setImageBitmap(currentContent.pic);
        }
        final ImageButton favourite = (ImageButton)convertView.findViewById(R.id.favourite_item_favouritebtn);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton btn = (ImageButton)v;
                switch (currentContent.favourite){
                    case NewsContent.FAVOURITE:
                        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_favorite));
                        currentContent.favourite = NewsContent.NOT_FAVOURITE;
                        btn.invalidate();
                        break;
                    case NewsContent.NOT_FAVOURITE:
                        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.red_heart));
                        currentContent.favourite = NewsContent.FAVOURITE;
                        btn.invalidate();
                        break;
                }
            }
        });
        CardView cardView = (CardView)convertView.findViewById(R.id.favourite_item);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentContent.urlstr!=null){
                    Intent intent = new Intent();
                    intent.putExtra("content", currentContent);
                    int order = tablist.getCodedTitle().indexOf(correspondingTabs.get(position));
                    intent.putExtra("order", order);
                    intent.setClass(context, NewsViewActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
