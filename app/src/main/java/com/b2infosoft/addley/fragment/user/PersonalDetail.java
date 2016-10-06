package com.b2infosoft.addley.fragment.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.b2infosoft.addley.ChangePassword;
import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Response;
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

public class PersonalDetail extends Fragment {

    ProgressBar progressBar;
    ScrollView personal_detail_form;
    EditText code,name,address,pin_code,email,mobile,referral_code;
    Button update,change_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_personal_detail,container,false);
        code = (EditText)view.findViewById(R.id.personal_detail_code);
        name = (EditText)view.findViewById(R.id.personal_detail_name);
        address = (EditText)view.findViewById(R.id.personal_detail_address);
        pin_code = (EditText)view.findViewById(R.id.personal_detail_pin_code);
        email = (EditText)view.findViewById(R.id.personal_detail_email);
        mobile = (EditText)view.findViewById(R.id.personal_detail_mobile);
        referral_code = (EditText)view.findViewById(R.id.personal_detail_referral_code);
        update = (Button)view.findViewById(R.id.personal_detail_action_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.PERSONAL_NAME,name.getText().toString());
                data.put(Tag.PERSONAL_ADDRESS,address.getText().toString());
                data.put(Tag.PERSONAL_PINNO,pin_code.getText().toString());
                new PersonalDetailUpdateTask(data).execute();
            }
        });
        change_password = (Button)view.findViewById(R.id.personal_detail_action_change_password);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePassword.class));
            }
        });
        progressBar = (ProgressBar) view.findViewById(R.id.personal_detail_progress);
        personal_detail_form = (ScrollView)view.findViewById(R.id.personal_detail_form);
        new PersonalDetailTask().execute();
        return view;
        /*
            View view = inflater.inflate(R.layout.fragment_dashboard_best_offer,container,false);
            // Initialize recycler view
            recyclerView = (RecyclerView) view.findViewById(R.id.best_offer_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return view;
        */
    }
    private class PersonalDetailTask  extends AsyncTask<String, Void,Integer> {
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
                data.put(Tag.USER_ACTION,Tag.PERSONAL_DETAIL);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("Response",jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        //parseResult(jsonObject.getJSONObject(Tag.PERSONAL_DETAIL));
                        object = jsonObject.getJSONObject(Tag.PERSONAL_DETAIL);
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
               // Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PersonalDetailUpdateTask  extends AsyncTask<String, Void,Integer> {
        private JSONObject object;
        private HashMap<String,String> map;
        public PersonalDetailUpdateTask(HashMap<String, String> map) {
            this.map = map;
        }
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
                map.put(Tag.USER_ID, Login.getValue(getContext(),Tag.USER_ID));
                map.put(Tag.USER_ACTION, Tag.PERSONAL_DETAIL_UPDATE);
                JsonParser jsonParser=  new JsonParser(server.doPost(map));
                JSONObject jsonObject=jsonParser.getJsonObject();
               // Log.d("Response",jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        //parseResult(jsonObject.getJSONObject(Tag.PERSONAL_DETAIL));
                        //object = jsonObject.getJSONObject(Tag.PERSONAL_DETAIL);
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
                //parseResult(object);
                new PersonalDetailTask().execute();
            }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONObject object){
        try {
            if(object.has(Tag.PERSONAL_CODE)){
                code.setText(object.getString(Tag.PERSONAL_CODE));
            }if(object.has(Tag.PERSONAL_NAME)){
                String name_1 =object.getString(Tag.PERSONAL_NAME);
                name.setText(name_1.equalsIgnoreCase("null")?"":name_1);
            }if(object.has(Tag.PERSONAL_ADDRESS)){
                String name_1 =object.getString(Tag.PERSONAL_ADDRESS);
                address.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
            }if(object.has(Tag.PERSONAL_PINNO)){
                String name_1 =object.getString(Tag.PERSONAL_PINNO);
                pin_code.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
            }if(object.has(Tag.PERSONAL_EMAIL)){
                String eeee= object.getString(Tag.PERSONAL_EMAIL);
                Login.setKey(getContext(),Tag.USER_EMAIL,eeee);
                email.setText(eeee);
            }if(object.has(Tag.PERSONAL_MOBILE)){
                mobile.setText(object.getString(Tag.PERSONAL_MOBILE));
            }if(object.has(Tag.PERSONAL_SPONSORCODE)) {
                referral_code.setText(object.getString(Tag.PERSONAL_SPONSORCODE));
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}
