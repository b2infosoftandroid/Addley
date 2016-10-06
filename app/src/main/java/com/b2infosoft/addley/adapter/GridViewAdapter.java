package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.model.TopStoryItem;
import com.b2infosoft.addley.global.ServerPath;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<TopStoryItem> items;

    public GridViewAdapter(Context context, List<TopStoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.fragment_dashboard_top_story_item_layout,null);
        TopStoryItem item=items.get(position);
        ImageView imageView= (ImageView)view.findViewById(R.id.top_story_offer_image);
        Picasso.with(context)
                .load(ServerPath.getLogoPath(item.getImg()))
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(imageView);
        TextView textView=(TextView)view.findViewById(R.id.top_story_offer_number);
        textView.setText(item.getOfferCount()+"\nOFFERS");
        return view;
    }
}
