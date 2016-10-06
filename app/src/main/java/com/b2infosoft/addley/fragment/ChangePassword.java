package com.b2infosoft.addley.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.dialog.CustomDialog;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */
public class ChangePassword extends Fragment {
    private ProgressDialog progressDialog;
    private ChangePasswordTask mChangePasswordTask= null;
    // UI Components
    Button change_password_save,change_password_cancel;
    EditText change_password_old,change_password_new,change_password_confirm;
    public ChangePassword() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=     inflater.inflate(R.layout.fragment_change_password, container, false);
        change_password_old = (EditText)view.findViewById(R.id.change_password_old);
        change_password_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                change_password_old.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        change_password_new = (EditText)view.findViewById(R.id.change_password_new);
        change_password_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                change_password_new.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        change_password_confirm = (EditText)view.findViewById(R.id.change_password_confirm);
        change_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                change_password_confirm.setError(null);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        change_password_save = (Button)view.findViewById(R.id.change_password_save);
        change_password_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attemptChangePassword()){
                    String pass_old= change_password_old.getText().toString().trim();
                    String pass_new = change_password_new.getText().toString().trim();
                    mChangePasswordTask = new ChangePasswordTask(pass_old, pass_new);
                    mChangePasswordTask.execute((Void) null);
                }
            }
        });
        change_password_cancel = (Button)view.findViewById(R.id.change_password_cancel);
        return view;
    }
    private boolean attemptChangePassword(){
        String pass_old= change_password_old.getText().toString().trim();
        String pass_new = change_password_new.getText().toString().trim();
        String pass_confirm= change_password_confirm.getText().toString().trim();
        change_password_old.setError(null);
        change_password_new.setError(null);
        change_password_confirm.setError(null);
        if(pass_old.length()==0){
            change_password_old.setError("Please Enter Old Password");
            change_password_old.requestFocus();
            Log.d("CP","1");
            return false;
        }if(pass_new.length()==0){
            change_password_new.setError("Please Enter New Password");
            change_password_new.requestFocus();
            Log.d("CP", "2");
            return false;
        }if(pass_new.length()<=5){
            change_password_new.setError("New Password Minimum 6 Character");
            change_password_new.requestFocus();
            Log.d("CP", "3");
            return false;
        }if(pass_confirm.length()==0){
            change_password_confirm.setError("Please Enter Confirm Password");
            change_password_confirm.requestFocus();
            Log.d("CP", "4");
            return false;
        }
        if(!pass_confirm.equals(pass_new)){
            change_password_confirm.setError("Confirm Password not match");
            change_password_confirm.requestFocus();
            Log.d("CP", "5");
            return false;
        }
        Log.d("CP","6");
        return true;
    }

    public class ChangePasswordTask extends AsyncTask<Void,Void,Boolean> {
        private String old_pass;
        private String new_pass;
        private int status;
        public ChangePasswordTask(String old_pass, String new_pass) {
            this.old_pass = old_pass;
            this.new_pass = new_pass;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_PASS,old_pass);
                data.put(Tag.USER_PASS_NEW,new_pass);
                data.put(Tag.USER_ACTION,Tag.CHANGE_PASSWORD);
                //String response =server.doPost(data);

                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("RESPONSE", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        if(jsonObject.has(Tag.PASSWORD_CHANGED)){
                            status  =  jsonObject.getInt(Tag.PASSWORD_CHANGED);
                        }
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if(aBoolean) {
                String message="";
                switch (status){
                    case 0:
                        message= "Something went wrong!";
                        break;
                    case 1:
                        message= "Success! Your Password Changed Successfully";
                        break;
                    case 2:
                        message = "Current password didn't match";
                        break;
                }
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog.setMessage(message);
                customDialog.show();
            }
        }
    }
}
