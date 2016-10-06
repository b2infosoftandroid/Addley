package com.b2infosoft.addley.fragment.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.SearchCategory;
import com.b2infosoft.addley.adapter.CategoriesListAdapter;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.Category;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */

public class Categories extends Fragment {
//    private SwipeRefreshLayout refreshLayout;
    private ListView listViewCompat;
    private List<Category> categories;
    private View progressBar;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_categorie, container, false);
        dbHelper = new DBHelper(getContext());
        progressBar = view.findViewById(R.id.categories_progress_bar);
        listViewCompat = (ListView) view.findViewById(R.id.categories_listViewCompat);
        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (categories.size() > position) {
                    Login.setKey(getContext(), Tag.CATEGORIES_ID, categories.get(position).getId() + "");
                    Login.setKey(getContext(), Tag.CATEGORIES_NAME, categories.get(position).getName() + "");
                    startActivity(new Intent(getContext(), SearchCategory.class));
                }
            }
        });
        /*
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_category);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CategoriesTask(1).execute();
            }
        });
        */
        boolean inti= true;
        categories = new ArrayList<>();
        List<Category> list = dbHelper.getCategory();

        if (list.size() > 0) {
            setListAdapter(list);
        } else {
            new CategoriesTask(0).execute();
            inti = false;
        }
        if(inti){
            new CategoriesTask(1).execute();
        }
        return view;
    }

    private class CategoriesTask extends AsyncTask<String, Void, Integer> {
        int i;

        public CategoriesTask(int i) {
            this.i = i;
        }

        @Override
        protected void onPreExecute() {
            if (i == 0) {
                progressBar.setVisibility(View.VISIBLE);
                listViewCompat.setVisibility(View.GONE);
            } else if (i == 1) {
               // refreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ACTION, Tag.CATEGORIES);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        parseResult(jsonObject.getJSONArray(Tag.CATEGORIES));
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer response) {
            listViewCompat.setVisibility(View.VISIBLE);
            if (i == 0) {
                progressBar.setVisibility(View.GONE);
            } else if (i == 1) {
               // refreshLayout.setRefreshing(false);
            }
            /*
            if (response == 1) {
                listAdapter = new CategoriesListAdapter(getContext(), categories);
                listViewCompat.setAdapter(listAdapter);
            } else {

            }
            */
        }
    }

    private void parseResult(JSONArray array) {
        dbHelper.deleteCategory();
        final List<Category> categories = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Category category = new Category();
                if (object.has(Tag.CATEGORIES_NAME)) {
                    category.setName(object.getString(Tag.CATEGORIES_NAME));
                }
                if (object.has(Tag.CATEGORIES_ID)) {
                    category.setId(object.getInt(Tag.CATEGORIES_ID));
                }
                if (object.has(Tag.CATEGORIES_COUNT)) {
                    category.setCount(object.getInt(Tag.CATEGORIES_COUNT));
                }
                if (object.has(Tag.CATEGORIES_IMAGE)) {
                    category.setImg(object.getString(Tag.CATEGORIES_IMAGE));
                }
                categories.add(category);
                dbHelper.insertCategory(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter(categories);
            }
        });
    }

    private void setListAdapter(List<Category> categories) {
        this.categories = categories;
        listViewCompat.setAdapter(new CategoriesListAdapter(getContext(), categories));
    }
}
