package com.b2infosoft.addley.fragment.profile;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.RedeemCashback;
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Login;
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

public class Summary extends Fragment {

    private View mProgress;
    private View mMainView;

    TextView current_balance,total_commission,downline_member,total_sale,self_commission,commission_level_1,commission_level_2,tds;
    Button redeem_cashback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_summary_layout,container,false);
        mProgress = view.findViewById(R.id.summary_progress);
        mMainView = view.findViewById(R.id.summary_mView);
        redeem_cashback = (Button)view.findViewById(R.id.profile_summary_redeem_cashback);
        redeem_cashback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RedeemCashback.class));
            }
        });
        current_balance = (TextView)view.findViewById(R.id.profile_summary_current_balance_default);
        downline_member = (TextView)view.findViewById(R.id.profile_summary_total_downline_default);
        total_sale = (TextView)view.findViewById(R.id.profile_summary_total_sale_default);
        total_commission = (TextView)view.findViewById(R.id.profile_summary_total_commission_default);
        self_commission = (TextView)view.findViewById(R.id.profile_summary_self_sale_commission_default);
        commission_level_1 = (TextView)view.findViewById(R.id.profile_summary_level_1_commission_default);
        commission_level_2 = (TextView)view.findViewById(R.id.profile_summary_level_2_commission_default);
        tds = (TextView)view.findViewById(R.id.profile_summary_tds_default);
        new DownLineTask().execute();
        return view;
    }

    private class DownLineTask  extends AsyncTask<String, Void,Integer> {
        private JSONArray object;
        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
            mMainView.setVisibility(View.GONE);
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION,Tag.GENERATE_COMMISSION);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        object = jsonObject.getJSONArray(Tag.GENERATE_COMMISSION);
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
            mProgress.setVisibility(View.GONE);
            mMainView.setVisibility(View.VISIBLE);
            if(response==1){
                parseResult(object);
            }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONArray array){
        try {
            int j=1;
            for (int i=0;i<array.length();i++){
                JSONObject object1 = array.getJSONObject(i);
                if(object1.has(Tag.GENERATE_COMMISSION_CODE)){

                }if(object1.has(Tag.GENERATE_COMMISSION_CUSTOMER_NAME)){

                }if(object1.has(Tag.GENERATE_COMMISSION_TOTAL_SALE)){
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_TOTAL_SALE);
                    total_sale.setText(commission);
                }if(object1.has(Tag.GENERATE_COMMISSION_SELF_COMMISSION)){
                    self_commission.setText(Global.getRupee(getContext())+""+object1.getString(Tag.GENERATE_COMMISSION_SELF_COMMISSION));
                }if(object1.has(Tag.GENERATE_COMMISSION_LEVEL1_COMMISSION)){
                    commission_level_1.setText(Global.getRupee(getContext())+""+object1.getString(Tag.GENERATE_COMMISSION_LEVEL1_COMMISSION));
                }if(object1.has(Tag.GENERATE_COMMISSION_LEVEL2_COMMISSION)){
                    commission_level_2.setText(Global.getRupee(getContext())+""+object1.getString(Tag.GENERATE_COMMISSION_LEVEL2_COMMISSION));
                }if(object1.has(Tag.GENERATE_COMMISSION_TDS)){
                    tds.setText(Global.getRupee(getContext())+""+object1.getString(Tag.GENERATE_COMMISSION_TDS));
                }if(object1.has(Tag.GENERATE_COMMISSION_NET_AMOUNT)){
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_NET_AMOUNT);
                    Login.setWalletAmount(getContext(),Float.parseFloat(commission.trim()));
                    current_balance.setText(Global.getRupee(getContext())+""+commission);
                }if(object1.has(Tag.GENERATE_COMMISSION_AMOUNT)){
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_AMOUNT);
                    total_commission.setText(Global.getRupee(getContext()) + "" + commission);
                }if(object1.has(Tag.GENERATE_COMMISSION_TOTAL_DOWNLINE_MEMBER)){
                    String commission = object1.getString(Tag.GENERATE_COMMISSION_TOTAL_DOWNLINE_MEMBER);
                    downline_member.setText(commission);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}
