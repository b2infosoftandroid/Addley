package com.b2infosoft.addley;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;
import com.b2infosoft.addley.dialog.CustomDialog;
import com.b2infosoft.addley.global.Response;

import org.json.JSONObject;

import java.util.HashMap;

public class Otp extends AppCompatActivity {
    private OtpConfirmTask mAuthTask = null;
    private String otp_id;
    private String otp_code;
    private Button otp_action;
    private EditText otp_confirm;
    private View mProgressView;
    private View mOtpFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        otp_code    =   getIntent().getExtras().getString(Tag.OTP_CODE);
        otp_id      =   getIntent().getExtras().getString(Tag.OTP_ID);
        otp_confirm = (EditText)findViewById(R.id.otp_confirm);
        otp_action = (Button)findViewById(R.id.otp_action);
        otp_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp_confirm.getText().toString())) {
                    otp_confirm.setError(getString(R.string.error_field_required));
                    otp_confirm.requestFocus();
                    return;
                }
                if(otp_code.equals(otp_confirm.getText().toString().trim())){
                    attemptOtpConfirm();
                }else{
                    CustomDialog dialog = new CustomDialog(Otp.this);
                    dialog.setMessage("Please enter correct OTP");
                    dialog.show();
                }
            }
        });
        mOtpFormView = findViewById(R.id.otp_form);
        mProgressView = findViewById(R.id.otp_progress);
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


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mOtpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mOtpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOtpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mOtpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private void attemptOtpConfirm(){
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        HashMap<String,String> mData= new HashMap<>();
        mData.put(Tag.OTP_ID,otp_id);
        mData.put(Tag.OTP_CODE,otp_code);
        mAuthTask = new OtpConfirmTask(mData);
        mAuthTask.execute((Void) null);
    }
    public class OtpConfirmTask extends AsyncTask<Void, Void, Boolean> {
        private final HashMap<String,String> mData;
        private int status = 0;
        public OtpConfirmTask(HashMap<String, String> mData) {
            this.mData = mData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.OTP_CODE,mData.get(Tag.OTP_CODE));
                data.put(Tag.OTP_ID,mData.get(Tag.OTP_ID));
                data.put(Tag.USER_ACTION, Tag.OTP_CONFIRM);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                //Log.d("RESPONSE", jsonParser.getJsonObject().toString());
                JSONObject jsonObject=jsonParser.getJsonObject();
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        if(jsonObject.has(Tag.OTP_CONFIRM)){
                            status = jsonObject.getInt(Tag.OTP_CONFIRM);
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
                Log.d("Exception",e.toString());
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            otp_confirm.setText(null);
            if (status==1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Otp.this);
                builder.setMessage("Account Created Successfully, you can Login Now");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        NavUtils.navigateUpFromSameTask(Otp.this);
                        finish();
                    }
                });
                builder.show();
            } else {
                CustomDialog dialog = new CustomDialog(Otp.this);
                dialog.setMessage("Something went wrong!");
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
