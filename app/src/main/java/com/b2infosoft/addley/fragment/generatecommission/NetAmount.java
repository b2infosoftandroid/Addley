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
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */

public class NetAmount extends Fragment {
    private View mProgressView;
    private View mGenerateCommissionView;
    TextView your_code, your_name, total_commission, current_balance, downline_member, total_sale, self_commission, commission_level_1, commission_level_2, tds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_commission_net_amount_layout, container, false);
        mProgressView = view.findViewById(R.id.generate_commission_progress);
        mGenerateCommissionView = view.findViewById(R.id.generate_commission_main_scroll_view);

        your_code = (TextView) view.findViewById(R.id.generate_commission_your_code_default);
        current_balance = (TextView) view.findViewById(R.id.generate_commission_current_balance_default);
        downline_member = (TextView) view.findViewById(R.id.generate_commission_total_downline_default);
        total_commission = (TextView) view.findViewById(R.id.generate_commission_total_commission_default);
        total_sale = (TextView) view.findViewById(R.id.generate_commission_total_sale_default);
        self_commission = (TextView) view.findViewById(R.id.generate_commission_self_sale_commission_default);
        commission_level_1 = (TextView) view.findViewById(R.id.generate_commission_level_1_commission_default);
        commission_level_2 = (TextView) view.findViewById(R.id.generate_commission_level_2_commission_default);
        tds = (TextView) view.findViewById(R.id.generate_commission_tds_default);

        new GenerateCommissionTask().execute();
        return view;
    }

    private class GenerateCommissionTask extends AsyncTask<String, Void, Integer> {
        private JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
            mGenerateCommissionView.setVisibility(View.GONE);
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server = new Server(Urls.getUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION, Tag.GENERATE_COMMISSION);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        parseResult(jsonObject.getJSONArray(Tag.GENERATE_COMMISSION));
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
            mProgressView.setVisibility(View.GONE);
            mGenerateCommissionView.setVisibility(View.VISIBLE);
        }
    }

    private void parseResult(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                if (object1.has(Tag.GENERATE_COMMISSION_CODE)) {
                    your_code.setText(object1.getString(Tag.GENERATE_COMMISSION_CODE));
                }
                if (object1.has(Tag.GENERATE_COMMISSION_CUSTOMER_NAME)) {

                }
                if (object1.has(Tag.GENERATE_COMMISSION_TOTAL_SALE)) {
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_TOTAL_SALE);
                    total_sale.setText(commission);
                }
                if (object1.has(Tag.GENERATE_COMMISSION_SELF_COMMISSION)) {
                    self_commission.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.GENERATE_COMMISSION_SELF_COMMISSION));
                }
                if (object1.has(Tag.GENERATE_COMMISSION_LEVEL1_COMMISSION)) {
                    commission_level_1.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.GENERATE_COMMISSION_LEVEL1_COMMISSION));
                }
                if (object1.has(Tag.GENERATE_COMMISSION_LEVEL2_COMMISSION)) {
                    commission_level_2.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.GENERATE_COMMISSION_LEVEL2_COMMISSION));
                }
                if (object1.has(Tag.GENERATE_COMMISSION_TDS)) {
                    tds.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.GENERATE_COMMISSION_TDS));
                }
                if (object1.has(Tag.GENERATE_COMMISSION_NET_AMOUNT)) {
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_NET_AMOUNT);
                    Login.setWalletAmount(getContext(), Float.parseFloat(commission.trim()));
                    current_balance.setText(Global.getRupee(getContext()) + "" + commission);
                }
                if (object1.has(Tag.GENERATE_COMMISSION_AMOUNT)) {
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_AMOUNT);
                    total_commission.setText(Global.getRupee(getContext()) + "" + commission);
                }
                if (object1.has(Tag.GENERATE_COMMISSION_TOTAL_DOWNLINE_MEMBER)) {
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_TOTAL_DOWNLINE_MEMBER);
                    downline_member.setText(commission);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
    }
}
