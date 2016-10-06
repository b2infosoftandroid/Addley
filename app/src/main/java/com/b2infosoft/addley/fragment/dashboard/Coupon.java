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
import com.b2infosoft.addley.adapter.CouponGridViewAdapter;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.CouponItem;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.SearchCoupon;
import com.b2infosoft.addley.global.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */

public class Coupon extends Fragment {
    GridView gridView;
    List<CouponItem> items;
    CouponGridViewAdapter gridViewAdapter;
    ProgressBar progressBar;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_coupon, container, false);
        dbHelper = new DBHelper(getContext());
        progressBar = (ProgressBar) view.findViewById(R.id.top_story_progress_bar);
        gridView = (GridView) view.findViewById(R.id.top_story_grid_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponItem item = items.get(position);
                Intent intent = new Intent(getActivity(), SearchCoupon.class);
                intent.putExtra(Tag.COUPON_COMPANY_ID, item.getCompanyId());
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

    private class TopStoryTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
         /*
            List<TopStoryItem>  list= dbHelper.getTopStory();
            if(list.size()>0){
                if (progressBar.getVisibility()==View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                gridViewAdapter = new GridViewAdapter(getActivity(),list);
                gridView.setAdapter(gridViewAdapter);
            }else{
                if (progressBar.getVisibility()==View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
            */
            //gridView.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getCouponUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ACTION, Tag.COUPON_COMPANY);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //    Log.d("COUPON",jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        parseResult(jsonObject.getJSONArray(Tag.COUPON_COMPANY));
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
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            if (response == 1) {
                gridViewAdapter = new CouponGridViewAdapter(getActivity(), items);
                gridView.setAdapter(gridViewAdapter);
            } else {
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONArray array) {
        dbHelper.deleteTopStory();
        items = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object_1 = array.getJSONObject(i);
                CouponItem offer = new CouponItem();
                if (object_1.has(Tag.COUPON_COMPANY_ID)) {
                    offer.setCompanyId(object_1.getString(Tag.COUPON_COMPANY_ID));
                }
                if (object_1.has(Tag.COUPON_COMPANY_NAME)) {
                    offer.setCompanyName(object_1.getString(Tag.COUPON_COMPANY_NAME));
                }
                if (object_1.has(Tag.COUPON_COMPANY_COUNT)) {
                    offer.setCompanyCount(object_1.getInt(Tag.COUPON_COMPANY_COUNT));
                }
                if (object_1.has(Tag.COUPON_COMPANY_IMAGE)) {
                    offer.setCompanyImage(object_1.getString(Tag.COUPON_COMPANY_IMAGE));
                }
                if (object_1.has(Tag.COUPON_COMPANY_ADDEDON)) {
                    offer.setAddedOn(object_1.getString(Tag.COUPON_COMPANY_ADDEDON));
                }
                if (object_1.has(Tag.COUPON_STATUS)) {
                    offer.setvStatus(object_1.getInt(Tag.COUPON_STATUS));
                }
                if (object_1.has(Tag.COUPON_VSTATUS)) {
                    offer.setvStatus(object_1.getInt(Tag.COUPON_VSTATUS));
                }
                items.add(offer);
                //dbHelper.insertTopStory(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
    }
}
