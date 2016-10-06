package com.b2infosoft.addley.fragment.profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.b2infosoft.addley.Main2;
import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.global.Visit;
import com.b2infosoft.addley.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */

public class Overview extends Fragment {

    ProgressBar progressBar;
    ScrollView personal_detail_form;

    //Button sale,commission,downline;
    TextView sale,commission,downline;

    TextView user_name,how_to_work;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_overview_layout_1,container,false);
        /*
        user_name= (TextView)view.findViewById(R.id.user_dashboard_user_name);
        */
        how_to_work= (TextView)view.findViewById(R.id.user_dashboard_how_to_work);
        how_to_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Main2.class));
                Visit.setActiveSupFragment(getContext(),1);
                getActivity().finish();
            }
        });
        sale= (TextView)view.findViewById(R.id.user_dashboard_total_sale);
        commission= (TextView)view.findViewById(R.id.user_dashboard_total_commission);
        downline= (TextView)view.findViewById(R.id.user_dashboard_total_downline_member);
        progressBar = (ProgressBar) view.findViewById(R.id.bank_account_progress);
        personal_detail_form = (ScrollView)view.findViewById(R.id.bank_account_form);
        new UserDashboardTask().execute();
        return view;
    }
    private class UserDashboardTask  extends AsyncTask<String, Void,Integer> {
        private JSONObject object;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            personal_detail_form.setVisibility(View.GONE);
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION,Tag.USER_DASHBOARD);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        object = jsonObject;
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
            personal_detail_form.setVisibility(View.VISIBLE);
            if(response==1){
                parseResult(object);
            }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONObject object){
        try {
            if(object.has(Tag.USER_DASHBOARD_USER_NAME)){
                String name_1=object.getString(Tag.USER_DASHBOARD_USER_NAME);
                //user_name.setText(name_1);
            }if(object.has(Tag.USER_DASHBOARD_SALE)){
                String name_1=object.getString(Tag.USER_DASHBOARD_SALE);
                String data = name_1.concat("\n\nTotal\nSale");
                //sale.setText(data);
                sale.setText(name_1);
            }if(object.has(Tag.USER_DASHBOARD_COMMISSION)){
                String name_1=object.getString(Tag.USER_DASHBOARD_COMMISSION);
                String data = name_1.concat("\n\nTotal\nCommission");
                //commission.setText(data);
                commission.setText(name_1);

            }if(object.has(Tag.USER_DASHBOARD_COMMISSION)){
                String name_1=object.getString(Tag.USER_DASHBOARD_DOWNLINE);
                String data = name_1.concat("\n\nDownline\nMember");
                //downline.setText(data);
                downline.setText(name_1);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}