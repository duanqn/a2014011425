package com.ihandy.a2014011425;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by max on 16-8-29.
 */
public class TitleBar extends RelativeLayout {
    public TitleBar(Context context, AttributeSet attr){
        super(context, attr);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Return button clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
