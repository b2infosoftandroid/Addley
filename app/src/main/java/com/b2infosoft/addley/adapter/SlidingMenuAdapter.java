package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.model.ItemSlideMenu;

import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */
public class SlidingMenuAdapter extends BaseAdapter {

    private Context context;
    private List<ItemSlideMenu> itemSlideMenus;

    public SlidingMenuAdapter(Context context, List<ItemSlideMenu> itemSlideMenus) {
        this.context = context;
        this.itemSlideMenus = itemSlideMenus;
    }

    @Override
    public int getCount() {
        return itemSlideMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return itemSlideMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_sliding_menu,null);
        ImageView imageView =(ImageView) view.findViewById(R.id.item_img);
        TextView textView =(TextView) view.findViewById(R.id.item_title);
        ItemSlideMenu menu = itemSlideMenus.get(position);
        imageView.setImageResource(menu.getImgId());
        textView.setText(menu.getTitle());
        return view;
    }
}
