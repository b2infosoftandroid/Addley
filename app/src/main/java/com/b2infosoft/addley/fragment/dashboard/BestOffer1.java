package com.b2infosoft.addley.fragment.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.b2infosoft.addley.model.OfferItem;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.Paginate;
import com.b2infosoft.addley.adapter.OfferRecycleViewAdapter;
import com.b2infosoft.addley.adapter.RecyclerOfferAdapter;
import com.b2infosoft.addley.adapter.RecyclerPersonAdapter;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Urls;
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

public class BestOffer1 extends Fragment implements Paginate.Callbacks, Common {
    /*
    RecyclerView recyclerView;
    */
    OfferRecycleViewAdapter offerRecycleViewAdapter;
    RecyclerOfferAdapter offerAdapter = null;
    List<OfferItem> offerItems;
    ProgressBar progressBar;
    private DBHelper dbHelper;

    private static final int GRID_SPAN = 3;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerPersonAdapter adapter;
    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;
    private int last_position = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_best_offer, container, false);
        dbHelper = new DBHelper(getContext());
        progressBar = (ProgressBar) view.findViewById(R.id.best_offer_progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.best_offer_recycler_view);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_offers);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                last_position = 0;
                new AsyncHttpTask(1).execute();
            }
        });
        handler = new Handler();
        setupPagination();
        List<OfferItem> search_data = dbHelper.getOfferAll();
        if (search_data.size() > 0) {
            last_position = dbHelper.getLastPositionOffer();
            setMyAdapter(search_data);
        } else {
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
        RecyclerView.LayoutManager layoutManager = layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setMyAdapter(List<OfferItem> items) {
        offerAdapter = new RecyclerOfferAdapter(getContext(), items);
        recyclerView.setAdapter(offerAdapter);
        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .build();
    }

    private void addData(final List<OfferItem> items) {
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
            page++;
            //adapter.add(DataProvider.getRandomData(itemsPerPage));
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
                Server server = new Server(Urls.getBestOffers());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.LAST_POSITION, String.valueOf(last_position));
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        if (jsonObject.has(Tag.LAST_POSITION)) {
                            last_position = jsonObject.getInt(Tag.LAST_POSITION);
                        }
                        parseResult(jsonObject);
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

        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getBestOffers());
                HashMap<String, String> data = new HashMap<>();
                if (last_position != 0) {
                    last_position++;
                }
                data.put(Tag.LAST_POSITION, String.valueOf(last_position));
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        if (jsonObject.has(Tag.LAST_POSITION)) {
                            last_position = jsonObject.getInt(Tag.LAST_POSITION);
                        }
                        parseResultAdd(jsonObject);
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

    private void parseResult(JSONObject object) {
        dbHelper.deleteOfferAll();
        final List<OfferItem> offers = new ArrayList<>();
        try {
            if (object.has(Tag.OFFERS)) {
                JSONArray array = object.getJSONArray(Tag.OFFERS);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object_1 = array.getJSONObject(i);
                    OfferItem offer = new OfferItem();
                    if (object_1.has(Tag.OFFER_NAME)) {
                        offer.setName(object_1.getString(Tag.OFFER_NAME));
                    }
                    if (object_1.has(Tag.OFFER_LOGO)) {
                        offer.setLogo(object_1.getString(Tag.OFFER_LOGO));
                    }
                    if (object_1.has(Tag.OFFER_DISCOUNT)) {
                        offer.setDiscount(object_1.getString(Tag.OFFER_DISCOUNT));
                    }
                    if (object_1.has(Tag.OFFER_LINK)) {
                        offer.setOfferLink(object_1.getString(Tag.OFFER_LINK));
                    }
                    if (object.has(Tag.LAST_POSITION)) {
                        offer.setLastPosition(object.getString(Tag.LAST_POSITION));
                    }
                    offers.add(offer);
                    dbHelper.insertOfferAll(offer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer", e.toString());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setMyAdapter(offers);
            }
        });
    }

    private void parseResultAdd(JSONObject object) {
        if (dbHelper == null) {
            new DBHelper(getContext());
        }
        List<OfferItem> offers = new ArrayList<>();
        try {
            if (object.has(Tag.OFFERS)) {
                JSONArray array = object.getJSONArray(Tag.OFFERS);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object_1 = array.getJSONObject(i);
                    OfferItem offer = new OfferItem();
                    if (object_1.has(Tag.OFFER_NAME)) {
                        offer.setName(object_1.getString(Tag.OFFER_NAME));
                    }
                    if (object_1.has(Tag.OFFER_LOGO)) {
                        offer.setLogo(object_1.getString(Tag.OFFER_LOGO));
                    }
                    if (object_1.has(Tag.OFFER_DISCOUNT)) {
                        offer.setDiscount(object_1.getString(Tag.OFFER_DISCOUNT));
                    }
                    if (object_1.has(Tag.OFFER_LINK)) {
                        offer.setOfferLink(object_1.getString(Tag.OFFER_LINK));
                    }
                    if (object.has(Tag.LAST_POSITION)) {
                        offer.setLastPosition(object.getString(Tag.LAST_POSITION));
                    }
                    offers.add(offer);
                    dbHelper.insertOfferAll(offer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer", e.toString());
        }
        if (offers.size() > 0) {
            addData(offers);
        }
    }
}
