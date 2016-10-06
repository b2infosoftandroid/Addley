package com.b2infosoft.addley.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.global.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */

public class Dashboard extends Fragment {

    ProgressBar progressBar;
    ScrollView personal_detail_form;

    Button sale,commission,downline;
    TextView user_name,how_to_work;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_dashboard,container,false);
        user_name= (TextView)view.findViewById(R.id.user_dashboard_user_name);
        how_to_work= (TextView)view.findViewById(R.id.user_dashboard_how_to_work);
        sale= (Button)view.findViewById(R.id.user_dashboard_total_sale);
        commission= (Button)view.findViewById(R.id.user_dashboard_total_commission);
        downline= (Button)view.findViewById(R.id.user_dashboard_total_downline_member);

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
               // Log.d("Response", jsonObject.toString());
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
                Login.setKey(getContext(),Tag.USER_NAME,name_1);
                user_name.setText(name_1);
            }if(object.has(Tag.USER_DASHBOARD_SALE)){
                String name_1=object.getString(Tag.USER_DASHBOARD_SALE);
                String data = name_1.concat("\n\nTotal\nSale");
                sale.setText(data);
            }if(object.has(Tag.USER_DASHBOARD_COMMISSION)){
                String name_1=object.getString(Tag.USER_DASHBOARD_COMMISSION);
                String data = name_1.concat("\n\nTotal\nCommission");
                commission.setText(data);
            }if(object.has(Tag.USER_DASHBOARD_COMMISSION)){
                String name_1=object.getString(Tag.USER_DASHBOARD_DOWNLINE);
                String data = name_1.concat("\n\nDownline\nMember");
                downline.setText(data);
            }if(object.has(Tag.USER_EMAIL)){
                String name_1=object.getString(Tag.USER_EMAIL);
                Login.setKey(getContext(),Tag.USER_EMAIL,name_1);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}
