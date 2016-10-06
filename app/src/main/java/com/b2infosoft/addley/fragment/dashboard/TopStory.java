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
import android.widget.GridView;
import android.widget.ProgressBar;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.adapter.GridViewAdapter;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.TopStoryItem;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */

public class TopStory extends Fragment {
    GridView gridView;
    List<TopStoryItem> items;
   // RecyclerView recyclerView;
   // CouponGridViewAdapter couponGridViewAdapter;
    GridViewAdapter gridViewAdapter;
    ProgressBar progressBar;
    private DBHelper dbHelper;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_top_story,container,false);
        dbHelper = new DBHelper(getContext());
        progressBar = (ProgressBar)view.findViewById(R.id.top_story_progress_bar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView)view.findViewById(R.id.top_story_grid_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopStoryItem item=    items.get(position);
                Intent intent = new Intent(getActivity(), Search.class);
                intent.putExtra(Tag.SEARCH_OFFER_COMPANY,item.getName());
                startActivity(intent);
                //Toast.makeText(getContext(),"CLICK ME : "+item.getName()+" : "+position,Toast.LENGTH_SHORT).show();
            }
        });
        /*
        for(int i=15;i<40;i++) {
            TopStoryItem item = new TopStoryItem();
            item.setImg("http://b2rakesh.com/rajesh.png");
            item.setOfferCount(i);
            items.add(item);
        }
        */

        //items = new ArrayList<>();
        //gridViewAdapter = new GridViewAdapter(getActivity(),items);
        //gridView.setAdapter(gridViewAdapter);

        new TopStoryTask().execute();
        return view;
    }
    private class TopStoryTask  extends AsyncTask<String, Void,Integer> {
        @Override
        protected void onPreExecute() {
            List<TopStoryItem>  list= dbHelper.getTopStory();
            if(list.size()>0){
                if (progressBar.getVisibility()==View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                gridViewAdapter = new GridViewAdapter(getActivity(),list);
             //   gridView.setAdapter(gridViewAdapter);
            }else{
                if (progressBar.getVisibility()==View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
            //gridView.setVisibility(View.GONE);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ACTION,Tag.TOP_STORY);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
               //Log.d("RESPONSE",jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        parseResult(jsonObject.getJSONArray(Tag.TOP_STORY));
                        return 1;
                    }else{
                        return 0;
                    }
                }else{
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
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
           if(response==1){
                gridViewAdapter = new GridViewAdapter(getActivity(),items);
               gridView.setAdapter(gridViewAdapter);
           }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONArray array){
        dbHelper.deleteTopStory();
        items = new ArrayList<>();
        try {
                for(int i=0;i<array.length();i++) {
                    JSONObject object_1 = array.getJSONObject(i);
                    TopStoryItem offer = new TopStoryItem();
                    if (object_1.has(Tag.TOP_STORY_NAME)) {
                        offer.setName(object_1.getString(Tag.TOP_STORY_NAME));
                    }if (object_1.has(Tag.TOP_STORY_COUNT)) {
                        offer.setOfferCount(object_1.getInt(Tag.TOP_STORY_COUNT));
                    }if (object_1.has(Tag.TOP_STORY_IMG)) {
                        offer.setImg(object_1.getString(Tag.TOP_STORY_IMG));
                    }
                    items.add(offer);
                    dbHelper.insertTopStory(offer);
                }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}
