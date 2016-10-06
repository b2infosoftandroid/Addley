package com.b2infosoft.addley.fragment.user;

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

public class BankAccountDetail extends Fragment {

    EditText pan_number, bank_name, bank_ac_number, ifsc_code, branch_name;
    Button update;
    ProgressBar progressBar;
    ScrollView personal_detail_form;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_bank_account_detail, container, false);
        pan_number = (EditText) view.findViewById(R.id.bank_account_pan_number);
        bank_name = (EditText) view.findViewById(R.id.bank_account_bank_name);
        bank_ac_number = (EditText) view.findViewById(R.id.bank_account_bank_account_number);
        ifsc_code = (EditText) view.findViewById(R.id.bank_account_ifsc_code);
        branch_name = (EditText) view.findViewById(R.id.bank_account_bank_branch);
        update = (Button) view.findViewById(R.id.bank_account_action_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.BANK_PAN_NUMBER, pan_number.getText().toString());
                data.put(Tag.BANK_NAME, bank_name.getText().toString());
                data.put(Tag.BANK_AC_NUMBER, bank_ac_number.getText().toString());
                data.put(Tag.BANK_IFSC_CODE, ifsc_code.getText().toString());
                data.put(Tag.BANK_BRANCH_NAME, branch_name.getText().toString());
                new BankDetailUpdateTask(data).execute();
            }
        });
        progressBar = (ProgressBar) view.findViewById(R.id.bank_account_progress);
        personal_detail_form = (ScrollView) view.findViewById(R.id.bank_account_form);
        new BankAccountTask().execute();
        return view;
    }

    private class BankAccountTask extends AsyncTask<String, Void, Integer> {
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
                Server server = new Server(Urls.getUserAction());
                HashMap<String, String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION, Tag.BANK_DETAIL);
                JsonParser jsonParser = new JsonParser(server.doPost(data));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
                        object = jsonObject.getJSONObject(Tag.BANK_DETAIL);
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
            personal_detail_form.setVisibility(View.VISIBLE);
            if (response == 1) {
                parseResult(object);
            } else {
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BankDetailUpdateTask extends AsyncTask<String, Void, Integer> {
        private JSONObject object;
        private HashMap<String, String> map;

        public BankDetailUpdateTask(HashMap<String, String> map) {
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
                Server server = new Server(Urls.getUserAction());
                map.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                map.put(Tag.USER_ACTION, Tag.BANK_DETAIL_UPDATE);
                JsonParser jsonParser = new JsonParser(server.doPost(map));
                JSONObject jsonObject = jsonParser.getJsonObject();
                //Log.d("Response",jsonObject.toString());
                if (jsonObject.has(Response.JSON_SUCCESS)) {
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if (success == 1) {
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
            personal_detail_form.setVisibility(View.VISIBLE);
            if (response == 1) {
                //parseResult(object);
                new BankAccountTask().execute();
            } else {
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONObject object) {
        try {
            if (object.has(Tag.BANK_PAN_NUMBER)) {
                String name_1 = object.getString(Tag.BANK_PAN_NUMBER);
                pan_number.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
                if (name_1.equalsIgnoreCase("null")) {
                    pan_number.setEnabled(true);
                } else {
                    pan_number.setEnabled(false);
                }
            }
            if (object.has(Tag.BANK_NAME)) {
                String name_1 = object.getString(Tag.BANK_NAME);
                bank_name.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
                if (name_1.equalsIgnoreCase("null")) {
                    bank_name.setEnabled(true);
                } else if (name_1.equalsIgnoreCase("")) {
                    bank_name.setEnabled(true);
                } else {
                    bank_name.setEnabled(false);
                }
            }
            if (object.has(Tag.BANK_AC_NUMBER)) {
                String name_1 = object.getString(Tag.BANK_AC_NUMBER);
                bank_ac_number.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
                if (name_1.equalsIgnoreCase("null")) {
                    bank_ac_number.setEnabled(true);
                } else if (name_1.equalsIgnoreCase("")) {
                    bank_ac_number.setEnabled(true);
                } else {
                    bank_ac_number.setEnabled(false);
                }
            }
            if (object.has(Tag.BANK_IFSC_CODE)) {
                String name_1 = object.getString(Tag.BANK_IFSC_CODE);
                ifsc_code.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
                if (name_1.equalsIgnoreCase("null")) {
                    ifsc_code.setEnabled(true);
                } else if (name_1.equalsIgnoreCase("")) {
                    ifsc_code.setEnabled(true);
                } else {
                    ifsc_code.setEnabled(false);
                }
            }
            if (object.has(Tag.BANK_BRANCH_NAME)) {
                String name_1 = object.getString(Tag.BANK_BRANCH_NAME);
                branch_name.setText(name_1.equalsIgnoreCase("null") ? "" : name_1);
                if (name_1.equalsIgnoreCase("null")) {
                    branch_name.setEnabled(true);
                } else if (name_1.equalsIgnoreCase("")) {
                    branch_name.setEnabled(true);
                } else {
                    branch_name.setEnabled(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error BestOffer_1", e.toString());
        }
    }
}
