package com.b2infosoft.addley.fragment.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Common;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.Paginate;
import com.b2infosoft.addley.adapter.RecyclerCouponAdapter;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.CouponItem;
import com.b2infosoft.addley.server.JsonParser;
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

public class Coupon2 extends Fragment implements Paginate.Callbacks, Common {
    /*
    RecyclerView recyclerView;
    */

    RecyclerCouponAdapter offerAdapter = null;
    ProgressBar progressBar;
    private DBHelper dbHelper;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private boolean loading = false;
    private Handler handler;
    private Paginate paginate;
    private int page = 0;
    private int last_position = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_coupon, container, false);
        dbHelper = new DBHelper(getContext());
        progressBar = (ProgressBar) view.findViewById(R.id.top_story_progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.best_offer_recycler_view);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_coupon);refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                last_position = 0;
                new AsyncHttpTask(1).execute();
            }
        });
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);
        handler = new Handler();
        setupPagination();
        List<CouponItem> couponItems= dbHelper.getCoupons();
        if(couponItems.size()>0){
            last_position = dbHelper.getLastPositionCoupon();
            setMyAdapter(couponItems);
        }else {
            new AsyncHttpTask(0).execute();
        }
        return view;
    }

    protected void setupPagination() {
        // If RecyclerView was recently bound, unbind
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        //adapter = new RecyclerPersonAdapter(DataProvider.getRandomData(20));
        loading = false;
        page = 0;
        int layoutOrientation = OrientationHelper.VERTICAL;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        layoutManager = new GridLayoutManager(getContext(), 2, layoutOrientation, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setMyAdapter(List<CouponItem> items) {
        offerAdapter = new RecyclerCouponAdapter(getContext(), items);
        recyclerView.setAdapter(offerAdapter);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .build();
    }

    private void addData(final List<CouponItem> items) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                offerAdapter.add(items);
            }
        });
    }

    @Override
    public synchronized void onLoadMore() {
        loading = true;
        // Fake asynchronous loading that will generate page of random data after some delay
        handler.postDelayed(fakeCallback, networkDelay);
    }

    @Override
    public synchronized boolean isLoading() {
        return loading; // Return boolean weather data is already loading or not
    }

    @Override
    public boolean hasLoadedAllItems() {
        //return page == totalPages; // If all pages are loaded return true
        return false;
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            new LoadTask().execute();
            loading = false;
        }
    };

    private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            if (i == 0) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else if(i==1){
                refreshLayout.setRefreshing(true);
            }
        }

        int i;

        public AsyncHttpTask(int i) {
            this.i = i;
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getCouponUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ACTION, Tag.COUPON_COMPANY);
                data.put(Tag.LAST_POSITION, String.valueOf(last_position));
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        if (jsonObject.has(Tag.LAST_POSITION)) {
                            last_position = jsonObject.getInt(Tag.LAST_POSITION);
                        }
                        parseResult(jsonObject.getJSONArray(Tag.COUPON_COMPANY), last_position);
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
            if (i == 0) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }else if(i==1){
                refreshLayout.setRefreshing(false);
            }
        }
    }

    private class LoadTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
          //  refreshLayout.setRefreshing(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getCouponUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.LAST_POSITION, String.valueOf(last_position));
                data.put(Tag.USER_ACTION, Tag.COUPON_COMPANY);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        if (jsonObject.has(Tag.LAST_POSITION)) {
                            last_position = jsonObject.getInt(Tag.LAST_POSITION);
                        }
                        parseResultMore(jsonObject.getJSONArray(Tag.COUPON_COMPANY), last_position);
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

        }
    }

    private void parseResult(JSONArray array, int last) {
        dbHelper.deleteTopStory();
        final List<CouponItem> items_1 = new ArrayList<>();
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
                offer.setLastPosition(last);
                items_1.add(offer);
                dbHelper.insertTopStory(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setMyAdapter(items_1);
            }
        });
    }

    private void parseResultMore(JSONArray array, int last) {
        if (dbHelper == null)
            dbHelper = new DBHelper(getContext());
        final List<CouponItem> items_1 = new ArrayList<>();
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
                offer.setLastPosition(last);
                items_1.add(offer);
                dbHelper.insertTopStory(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
        if (items_1.size() > 0) {
            addData(items_1);
        }
    }
}
