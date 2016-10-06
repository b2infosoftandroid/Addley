package com.b2infosoft.addley.fragment.generatecommission;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */

public class Refer extends Fragment {
    private View mProgressView;
    private View mGenerateCommissionView;

    TextView success, pending, paid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_commission_refer_amount_layout, container, false);
        mProgressView = view.findViewById(R.id.generate_commission_progress);
        mGenerateCommissionView = view.findViewById(R.id.generate_commission_main_scroll_view);

        success = (TextView) view.findViewById(R.id.refer_success);
        pending = (TextView) view.findViewById(R.id.refer_pending);
        paid = (TextView) view.findViewById(R.id.refer_paid);

        new AsyncHttpTask().execute();
        return view;
    }

    private class AsyncHttpTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
            mGenerateCommissionView.setVisibility(View.GONE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION, Tag.REFER_AMOUNT);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            mProgressView.setVisibility(View.GONE);
            mGenerateCommissionView.setVisibility(View.VISIBLE);
            try {
                if (object != null) {
                    if (object.has(Tag.REFER_AMOUNT_SUCCESS)) {
                        String amount = object.getString(Tag.REFER_AMOUNT_SUCCESS);
                        success.setText("Rs. " + amount);
                    }
                    if (object.has(Tag.REFER_AMOUNT_PENDING)) {
                        String amount = object.getString(Tag.REFER_AMOUNT_PENDING);
                        pending.setText("Rs. " + amount);
                    }
                    if (object.has(Tag.REFER_AMOUNT_PAID)) {
                        String amount = object.getString(Tag.REFER_AMOUNT_PAID);
                        paid.setText("Rs. " + amount);
                    }
                }
            } catch (JSONException e) {

            }
        }
    }

}
