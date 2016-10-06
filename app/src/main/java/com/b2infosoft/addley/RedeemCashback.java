package com.b2infosoft.addley;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.b2infosoft.addley.global.*;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RedeemCashback extends AppCompatActivity {
    private View mProgressView;
    private View mGenerateCommissionView;
    TextView your_code_1,your_name_1,total_commission_1,current_balance_1,downline_member_1,total_sale_1,self_commission_1,commission_level_1_1,commission_level_2_1,tds_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_cashback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressView = findViewById(R.id.generate_commission_progress);
        mGenerateCommissionView = findViewById(R.id.generate_commission_main_scroll_view);
        your_code_1 = (TextView)findViewById(R.id.generate_commission_your_code_default_1);
        current_balance_1 = (TextView)findViewById(R.id.generate_commission_current_balance_default_1);
        downline_member_1 = (TextView)findViewById(R.id.generate_commission_total_downline_default_1);
        total_commission_1 = (TextView)findViewById(R.id.generate_commission_total_commission_default_1);
        total_sale_1 = (TextView)findViewById(R.id.generate_commission_total_sale_default_1);
        self_commission_1 = (TextView)findViewById(R.id.generate_commission_self_sale_commission_default_1);
        commission_level_1_1 = (TextView)findViewById(R.id.generate_commission_level_1_commission_default_1);
        commission_level_2_1 = (TextView)findViewById(R.id.generate_commission_level_2_commission_default_1);
        tds_1 = (TextView)findViewById(R.id.generate_commission_tds_default_1);
        new RedeemCommissionTask().execute();
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
    private class RedeemCommissionTask  extends AsyncTask<String, Void,Integer> {
        private JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
            mGenerateCommissionView.setVisibility(View.GONE);
            setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, com.b2infosoft.addley.global.Login.getValue(RedeemCashback.this, Tag.USER_ID));
                data.put(Tag.USER_ACTION,Tag.REDEEM_COMMISSION);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        this.jsonObject=jsonObject;
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
            mProgressView.setVisibility(View.GONE);
            mGenerateCommissionView.setVisibility(View.VISIBLE);
            if(response==1){
                parseResult(jsonObject);
            }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONObject object){
        try {
            if(object.has(Tag.REDEEM_COMMISSION)){
                JSONArray array = object.getJSONArray(Tag.REDEEM_COMMISSION);
                for (int i=0;i<array.length();i++){
                    JSONObject object1 = array.getJSONObject(i);
                    if(object1.has(Tag.REDEEM_COMMISSION_CODE)){
                        your_code_1.setText(object1.getString(Tag.REDEEM_COMMISSION_CODE));
                    }if(object1.has(Tag.REDEEM_COMMISSION_CUSTOMER_NAME)){
                    }if(object1.has(Tag.REDEEM_COMMISSION_TOTAL_SALE)){
                        String commission = object1.getString(Tag.REDEEM_COMMISSION_TOTAL_SALE);
                        total_sale_1.setText(commission);
                    }if(object1.has(Tag.REDEEM_COMMISSION_SELF_COMMISSION)){
                        self_commission_1.setText(Global.getRupee(this)+""+object1.getString(Tag.REDEEM_COMMISSION_SELF_COMMISSION));
                    }if(object1.has(Tag.REDEEM_COMMISSION_LEVEL1_COMMISSION)){
                        commission_level_1_1.setText(Global.getRupee(this)+""+object1.getString(Tag.REDEEM_COMMISSION_LEVEL1_COMMISSION));
                    }if(object1.has(Tag.REDEEM_COMMISSION_LEVEL2_COMMISSION)){
                        commission_level_2_1.setText(Global.getRupee(this)+""+object1.getString(Tag.REDEEM_COMMISSION_LEVEL2_COMMISSION));
                    }if(object1.has(Tag.REDEEM_COMMISSION_TDS)){
                        tds_1.setText(Global.getRupee(this)+""+object1.getString(Tag.REDEEM_COMMISSION_TDS));
                    }if(object1.has(Tag.REDEEM_COMMISSION_NET_AMOUNT)){
                        String commission = object1.getString(Tag.REDEEM_COMMISSION_NET_AMOUNT);
                        //Login.setWalletAmount(getContext(),Float.parseFloat(commission.trim()));
                        current_balance_1.setText(Global.getRupee(this)+""+commission);
                    }if(object1.has(Tag.REDEEM_COMMISSION_AMOUNT)){
                        String commission = object1.getString(Tag.REDEEM_COMMISSION_AMOUNT);
                        total_commission_1.setText(Global.getRupee(this) + "" + commission);
                    }if(object1.has(Tag.REDEEM_COMMISSION_TOTAL_DOWNLINE_MEMBER)){
                        String commission = object1.getString(Tag.REDEEM_COMMISSION_TOTAL_DOWNLINE_MEMBER);
                        downline_member_1.setText(commission);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
    }
}
