package com.ihandy.a2014011425;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihandy.a2014011425.R;

/**
 * Created by max on 16-9-2.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {
    public TextView title = null, detail = null;
    public ImageView img = null;
    public CardView card = null;
    public CardViewHolder(View v){
        super(v);
        title = (TextView)v.findViewById(R.id.card_view_item_title);
        detail = (TextView)v.findViewById(R.id.card_view_item_detail);
        img = (ImageView)v.findViewById(R.id.card_view_item_pic);
        card = (CardView)v.findViewById(R.id.card_view);
        //card.setListener...
    }
}
