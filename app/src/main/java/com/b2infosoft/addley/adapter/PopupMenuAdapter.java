package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.b2infosoft.addley.R;


/**
 * Created by rajesh on 4/19/2016.
 */
public class PopupMenuAdapter extends BaseAdapter {

    private Context context;
    private String []itemMenus;

    public PopupMenuAdapter(Context context, String[] itemMenus) {
        this.context = context;
        this.itemMenus = itemMenus;
    }

    @Override
    public int getCount() {
        return itemMenus.length;
    }

    @Override
    public Object getItem(int position) {
        return itemMenus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.dialog_box_commission_generate_add_field_list_view_layout,null);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.generate_commission_add_column_name);
        checkBox.setTextColor(Color.WHITE);
        checkBox.setText(itemMenus[position]);
        return view;
    }
}
