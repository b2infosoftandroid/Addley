package com.b2infosoft.addley.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.dialog.CustomDialog;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */

public class MissingSale extends Fragment {
    private ProgressDialog progressDialog;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    EditText mCompanyName,mProductName,mTransactionId,mAmount,mDate;
    Button submit;

    public MissingSale() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missing_sale, container, false);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        mCompanyName = (EditText)view.findViewById(R.id.missing_sale_company);
        mProductName = (EditText)view.findViewById(R.id.missing_sale_product);
        mTransactionId = (EditText)view.findViewById(R.id.missing_sale_transaction_id);
        mAmount = (EditText)view.findViewById(R.id.missing_sale_amount);
        mDate = (EditText)view.findViewById(R.id.missing_sale_date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate.setError(null);
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(),myDateListener,year,month,day);
                pickerDialog.show();
            }
        });
        submit = (Button)view.findViewById(R.id.missing_sale_action_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c_name= mCompanyName.getText().toString();
                String p_name = mProductName.getText().toString();
                String t_id = mTransactionId.getText().toString();
                String amount = mAmount.getText().toString();
                String date = mDate.getText().toString();
                if(isFormValid()){
                    MissingSaleTask missingSaleTask = new MissingSaleTask(c_name,p_name,t_id,amount,date);
                    missingSaleTask.execute((Void)null);
                }
            }
        });
        //view = inflater.inflate(R.layout.fragment_3,container,false);
        return view;
    }
    private boolean isFormValid(){
        mCompanyName.setError(null);
        mProductName.setError(null);
        mTransactionId.setError(null);
        mAmount.setError(null);
        mDate.setError(null);
        String c_name= mCompanyName.getText().toString().trim();
        String p_name = mProductName.getText().toString().trim();
        String t_id = mTransactionId.getText().toString().trim();
        String amount = mAmount.getText().toString().trim();
        String date = mDate.getText().toString().trim();
        if(c_name.length()==0){
            mCompanyName.setError("Please Enter Company Name");
            mCompanyName.requestFocus();
            return false;
        }
        if(p_name.length()==0){
            mProductName.setError("Please Enter Product Name");
            mProductName.requestFocus();
            return false;
        }if(t_id.length()==0){
            mTransactionId.setError("Please Enter Transaction Id");
            mTransactionId.requestFocus();
            return false;
        }if(amount.length()==0){
            mAmount.setError("Please Enter Amount");
            mAmount.requestFocus();
            return false;
        }if(date.length()==0){
            mDate.setError("Please Choose Date");
            mDate.requestFocus();
            return false;
        }
        return true;
    }
    private void setDate(int mm,int dd,int yyyy){
        mDate.setText(mm+"/"+dd+"/"+yyyy);
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            setDate(month + 1, day, year);
        }
    };
    public class MissingSaleTask extends AsyncTask<Void,Void,Boolean> {
        private String company_name;
        private String product_name;
        private String transaction_id;
        private String amount;
        private String date;
        private int status;
        public MissingSaleTask(String company_name, String product_name, String transaction_id, String amount, String date) {
            this.company_name = company_name;
            this.product_name = product_name;
            this.transaction_id = transaction_id;
            this.amount = amount;
            this.date = date;
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
                data.put(Tag.USER_ACTION,Tag.MISSING_SALE);
                data.put(Tag.MISSING_COMPANY_NAME,company_name);
                data.put(Tag.MISSING_PRODUCT_NAME,product_name);
                data.put(Tag.MISSING_TRANSACTION_ID,transaction_id);
                data.put(Tag.MISSING_AMOUNT,amount);
                data.put(Tag.MISSING_DATE,date);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("RESPONSE", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        if(jsonObject.has(Tag.MISSING_SALE)){
                            status  =  jsonObject.getInt(Tag.MISSING_SALE);
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
            Fragment fragment=null;
            if(aBoolean) {
                String message="";
                switch (status){
                    case 0:
                        message= "Something wrong!";
                        break;
                    case 1:
                        message= "Success! Your Query regarding Missing Sale has be Successfully submitted.";
                        fragment = new  Dashboard();
                        break;
                }
                if(fragment!=null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_content, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    CustomDialog customDialog = new CustomDialog(getActivity());
                    customDialog.setMessage(message);
                    customDialog.show();
                }
            }
        }
    }
}
