package com.b2infosoft.addley.fragment.generatecommission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
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

public class Redeemable extends Fragment {
    private View mProgressView;
    private View mGenerateCommissionView;
    TextView self_join, referral_amount, total_amount, self_commission, commission_level_1, commission_level_2, tds, net_amount;
    Button redeem_money;
    float total_net_amount = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_commission_redeemable_amount_layout, container, false);
        mProgressView = view.findViewById(R.id.generate_commission_progress);
        mGenerateCommissionView = view.findViewById(R.id.generate_commission_main_scroll_view);

        self_join = (TextView) view.findViewById(R.id.generate_commission_self_join_default);
        referral_amount = (TextView) view.findViewById(R.id.generate_commission_referral_amount_default);
        self_commission = (TextView) view.findViewById(R.id.generate_commission_self_sale_commission_default);
        commission_level_1 = (TextView) view.findViewById(R.id.generate_commission_level_1_commission_default);
        commission_level_2 = (TextView) view.findViewById(R.id.generate_commission_level_2_commission_default);
        total_amount = (TextView) view.findViewById(R.id.generate_commission_total_amount_default);
        tds = (TextView) view.findViewById(R.id.generate_commission_tds_default);
        net_amount = (TextView) view.findViewById(R.id.generate_commission_current_balance_default);
        redeem_money = (Button) view.findViewById(R.id.generate_commission_redeem_money);
        redeem_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert");
                if (total_net_amount > 300) {
                    builder.setMessage(R.string.redeem_money_confirm);
                    builder.setPositiveButton("REDEEM MONEY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            new RedeemMoneyTask().execute();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    builder.setMessage(R.string.redeem_money_notice);
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                }
                builder.create().show();
            }
        });
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
                data.put(Tag.USER_ACTION, Tag.REDEEM_COMMISSION);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        parseResult(jsonObject.getJSONArray(Tag.REDEEM_COMMISSION));
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

    private class RedeemMoneyTask extends AsyncTask<String, Void, Integer> {
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
                data.put(Tag.USER_ACTION, Tag.REDEEM_MONEY);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    return jsonObject.getInt(Response.JSON_SUCCESS);
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
            if (response == 1) {
                Toast.makeText(getActivity(),"SUCCESS",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),"FAILED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                if (object1.has(Tag.REDEEM_COMMISSION_SELF_JOIN)) {
                    self_join.setText(object1.getString(Tag.REDEEM_COMMISSION_SELF_JOIN));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_REFERRAL_AMOUNT)) {
                    referral_amount.setText(object1.getString(Tag.REDEEM_COMMISSION_REFERRAL_AMOUNT));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_SELF_COMMISSION)) {
                    self_commission.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.REDEEM_COMMISSION_SELF_COMMISSION));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_LEVEL1_COMMISSION)) {
                    commission_level_1.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.REDEEM_COMMISSION_LEVEL1_COMMISSION));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_LEVEL2_COMMISSION)) {
                    commission_level_2.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.REDEEM_COMMISSION_LEVEL2_COMMISSION));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_TOTAL_AMOUNT)) {
                    total_amount.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.REDEEM_COMMISSION_TOTAL_AMOUNT));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_TDS)) {
                    tds.setText(Global.getRupee(getContext()) + "" + object1.getString(Tag.REDEEM_COMMISSION_TDS));
                }
                if (object1.has(Tag.REDEEM_COMMISSION_NET_AMOUNT)) {
                    String commission = object1.getString(Tag.REDEEM_COMMISSION_NET_AMOUNT);
                    net_amount.setText(Global.getRupee(getContext()) + "" + commission);
                    total_net_amount = Float.parseFloat(commission);
                    //total_net_amount = 450;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
    }
}
