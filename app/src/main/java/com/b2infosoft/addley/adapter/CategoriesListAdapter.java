package com.b2infosoft.addley.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.model.Category;
import com.b2infosoft.addley.global.ServerPath;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 5/13/2016.
 */

public class CategoriesListAdapter  extends BaseAdapter{
    private Context context;
    private List<Category> categories;
    public CategoriesListAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }
    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.fragment_dashboard_categorie_layout, null);
        Category category= categories.get(position);
        ImageView image = (ImageView)view.findViewById(R.id.categories_image);
        Picasso.with(context)
                .load(ServerPath.getCategoryIconPath(category.getImg()))
                .placeholder(R.mipmap.ic_terrain_black_24dp)
                .error(R.mipmap.ic_terrain_black_24dp)
                .into(image);
       // Log.d("PATH",ServerPath.getCategoryIconPath(category.getImg()));
        TextView name = (TextView)view.findViewById(R.id.categories_name);
        TextView count = (TextView)view.findViewById(R.id.categories_count);
        name.setText(category.getName());
        count.setText(String.valueOf(category.getCount()));

        return view;
    }
}