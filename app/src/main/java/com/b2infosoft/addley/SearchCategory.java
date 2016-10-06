package com.b2infosoft.addley;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.OfferItem;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.adapter.OfferRecycleViewAdapter;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchCategory extends AppCompatActivity implements SearchView.OnQueryTextListener{
    List<OfferItem> offerItems;
    RecyclerView recyclerView;
    OfferRecycleViewAdapter offerRecycleViewAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(Login.getValue(this, Tag.CATEGORIES_NAME)!=null){
            getSupportActionBar().setTitle(Login.getValue(this, Tag.CATEGORIES_NAME));
        }
        // Initialize recycler view
        recyclerView = (RecyclerView)findViewById(R.id.best_offer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.best_offer_progress_bar);
        new AsyncHttpTask("").execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        new AsyncHttpTask(query).execute();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        new AsyncHttpTask(newText).execute();
        return false;
    }
    private class AsyncHttpTask  extends AsyncTask<String, Void,Integer> {
        String data;
        public AsyncHttpTask(String data) {
            this.data = data;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data_ = new HashMap<>();
                data_.put(Tag.USER_ACTION,Tag.SEARCH_OFFER_CATEGORIES);
                data_.put(Tag.CATEGORIES_ID, Login.getValue(getBaseContext(), Tag.CATEGORIES_ID));
                data_.put(Tag.SEARCH_OFFER_COMPANY, data);
                String dsdd= server.doPost(data_);
                JsonParser jsonParser=  new JsonParser(dsdd);
                JSONObject jsonObject=jsonParser.getJsonObject();
                Log.d("RES WORKING",jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        parseResult(jsonObject);
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
            recyclerView.setVisibility(View.VISIBLE);
            if(response==1){
                offerRecycleViewAdapter = new OfferRecycleViewAdapter(SearchCategory.this, offerItems);
                recyclerView.setAdapter(offerRecycleViewAdapter);
            }else{
                //Toast.makeText(getApplicationContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONObject object){
        offerItems = new ArrayList<>();
        try {
            if (object.has(Tag.OFFERS)) {
                JSONArray array =  object.getJSONArray(Tag.OFFERS);
                for(int i=0;i<array.length();i++){
                    JSONObject object_1 = array.getJSONObject(i);
                    OfferItem offer=new OfferItem();
                    if(object_1.has(Tag.OFFER_NAME)) {
                        offer.setName(object_1.getString(Tag.OFFER_NAME));
                    }if(object_1.has(Tag.OFFER_LOGO)) {
                        offer.setLogo(object_1.getString(Tag.OFFER_LOGO));
                    }if(object_1.has(Tag.OFFER_DISCOUNT)) {
                        offer.setDiscount(object_1.getString(Tag.OFFER_DISCOUNT));
                    }if(object_1.has(Tag.OFFER_LINK)) {
                        offer.setOfferLink(object_1.getString(Tag.OFFER_LINK));
                    }
                    offerItems.add(offer);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer",e.toString());
        }
    }
}
