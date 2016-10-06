package com.b2infosoft.addley;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.b2infosoft.addley.adapter.CouponListAdapter;
import com.b2infosoft.addley.adapter.CouponRecycleViewAdapter;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.Coupons;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.global.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchCoupon extends AppCompatActivity{
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ListViewCompat listViewCompat;
    CouponListAdapter couponListAdapter;
    List<Coupons> offerItems;
    CouponRecycleViewAdapter offerRecycleViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_coupon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initialize recycler view
        //recyclerView = (RecyclerView)findViewById(R.id.best_offer_recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listViewCompat = (ListViewCompat)findViewById(R.id.coupon_listViewCompat);
        progressBar = (ProgressBar)findViewById(R.id.best_offer_progress_bar);

        if(getIntent().hasExtra(Tag.COUPON_COMPANY_ID)){
            String company = getIntent().getExtras().getString(Tag.COUPON_COMPANY_ID);
            //Toast.makeText(this,company,Toast.LENGTH_SHORT).show();
            new AsyncHttpTask(company).execute();
        }
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
    private class AsyncHttpTask  extends AsyncTask<String, Void,Integer> {
        String data;
        public AsyncHttpTask(String data) {
            this.data = data;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            //setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getCouponUserAction());
                HashMap<String,String> data_ = new HashMap<>();
                data_.put(Tag.USER_ACTION,Tag.COUPONS);
                data_.put(Tag.COUPON_COMPANY_ID, data);
                String dsdd= server.doPost(data_);
                JsonParser jsonParser=  new JsonParser(dsdd);
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("COUPONS ",jsonObject.toString());
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
            if(response==1){
                //offerRecycleViewAdapter = new CouponRecycleViewAdapter(SearchCoupon.this, offerItems);
                //recyclerView.setAdapter(offerRecycleViewAdapter);
                couponListAdapter = new CouponListAdapter(SearchCoupon.this,offerItems);
                listViewCompat.setAdapter(couponListAdapter);
            }else{
                //Toast.makeText(getApplicationContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONObject object){
        offerItems = new ArrayList<>();
        try {
            if (object.has(Tag.COUPONS)) {
                JSONArray array =  object.getJSONArray(Tag.COUPONS);
                for(int i=0;i<array.length();i++){
                    JSONObject object_1 = array.getJSONObject(i);
                    Coupons offer=new Coupons();
                    if(object_1.has(Tag.COUPONS_ID)) {
                        offer.setCouponID(object_1.getString(Tag.COUPONS_ID));
                    }if(object_1.has(Tag.COUPONS_NAME)) {
                        offer.setCouponName(object_1.getString(Tag.COUPONS_NAME));
                    }if(object_1.has(Tag.COUPONS_IMAGE)) {
                        offer.setCouponImage(object_1.getString(Tag.COUPONS_IMAGE));
                    }if(object_1.has(Tag.COUPONS_OFFER_ID)) {
                        offer.setCouponOfferId(object_1.getString(Tag.COUPONS_OFFER_ID));
                    }if(object_1.has(Tag.COUPONS_OFFER_CODE)) {
                        offer.setCouponOfferCode(object_1.getString(Tag.COUPONS_OFFER_CODE));
                    }if(object_1.has(Tag.COUPONS_OFFER_URL)) {
                        offer.setCouponOfferUrl(object_1.getString(Tag.COUPONS_OFFER_URL));
                    }if(object_1.has(Tag.COUPONS_OFFER_TITLE)) {
                        offer.setCouponOfferTitle(object_1.getString(Tag.COUPONS_OFFER_TITLE));
                    }if(object_1.has(Tag.COUPONS_OFFER_END_DATE)) {
                        offer.setCouponOfferEndDate(object_1.getString(Tag.COUPONS_OFFER_END_DATE));
                    }if(object_1.has(Tag.COUPONS_OFFER_CAT_ID)) {
                        offer.setCouponOfferCatDate(object_1.getString(Tag.COUPONS_OFFER_CAT_ID));
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
