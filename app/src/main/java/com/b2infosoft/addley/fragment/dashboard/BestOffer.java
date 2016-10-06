package com.b2infosoft.addley.fragment.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.database.DBHelper;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.model.OfferItem;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.adapter.OfferRecycleViewAdapter;
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

public class BestOffer extends Fragment {
    List<OfferItem> offerItems;
    RecyclerView recyclerView;
    OfferRecycleViewAdapter offerRecycleViewAdapter;
    ProgressBar progressBar;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_best_offer, container, false);
        // Initialize recycler view
        dbHelper = new DBHelper(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.best_offer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.best_offer_progress_bar);

        new AsyncHttpTask().execute();
        return view;
    }

    private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            List<OfferItem> list = dbHelper.getOfferAll();
            if (list.size() > 0) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                offerRecycleViewAdapter = new OfferRecycleViewAdapter(getContext(), list);
                recyclerView.setAdapter(offerRecycleViewAdapter);
            } else {
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
//            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getBestOffers());
                HashMap<String, String> data = new HashMap<>();
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
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
            progressBar.setVisibility(View.GONE);
            if (response == 1) {
                offerRecycleViewAdapter = new OfferRecycleViewAdapter(getContext(), offerItems);
                recyclerView.setAdapter(offerRecycleViewAdapter);
            } else {
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONObject object) {
        dbHelper.deleteOfferAll();
        offerItems = new ArrayList<>();
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
                    offerItems.add(offer);
                    dbHelper.insertOfferAll(offer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer", e.toString());
        }
    }
}
