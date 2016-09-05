package com.ihandy.a2014011425;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by max on 16-9-5.
 */
public class CategoryManagementAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ViewPagerAdapter targetAdapter;
    private NewsApp app;
    private NewsTab tabs;
    private Context context;
    private CategoryManagementAdapter(Context _context){
        context = _context;
        inflater = LayoutInflater.from(context);
    }
    public static CategoryManagementAdapter getNewInstance(Context context, NewsApp appPointer){
        CategoryManagementAdapter adapter = new CategoryManagementAdapter(context);
        adapter.app = appPointer;
        adapter.tabs = appPointer.share_tabs;
        adapter.targetAdapter = appPointer.viewPagerAdapter;
        return adapter;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return tabs.getTitleNum();
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
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
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
            convertView = inflater.inflate(R.layout.list_item_category, null);
        }
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.list_item_category_image);
        TextView textView = (TextView) convertView.findViewById(R.id.list_item_category_text);
        textView.setText(tabs.titleAt(position));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetAdapter.removeTab(position);
                Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
