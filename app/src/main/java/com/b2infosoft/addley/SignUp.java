package com.b2infosoft.addley;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.global.Validation;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.dialog.CustomDialog;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.server.Server;

import org.json.JSONObject;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private UserSignUpTask mAuthTask = null;
    // UI References
    private Button sign_up;
    private TextView terms_condition,sign_in;
    private EditText name,mobile,referral,password,password_confirm;
    private CheckBox agree;
    private AutoCompleteTextView email;
    private View mProgressView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText)findViewById(R.id.sign_up_name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                name.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email = (AutoCompleteTextView)findViewById(R.id.sign_up_email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                email.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mobile = (EditText)findViewById(R.id.sign_up_mobile);
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mobile.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        referral = (EditText)findViewById(R.id.sign_up_referral);
        referral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                referral.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password = (EditText)findViewById(R.id.sign_up_password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password_confirm = (EditText)findViewById(R.id.sign_up_password_confirm);
        password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password_confirm.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        agree = (CheckBox)findViewById(R.id.sign_up_action_agree);
        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sign_up.setEnabled(isChecked);
            }
        });
        sign_up = (Button)findViewById(R.id.sign_up_action_sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
        terms_condition= (TextView)findViewById(R.id.sign_up_action_terms_condition);
        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,TermsCondition.class));
            }
        });
        sign_in = (TextView)findViewById(R.id.sign_up_action_sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SignUp.this);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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

    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }
        mobile.setError(null);
        email.setError(null);
        name.setError(null);
        referral.setError(null);
        password.setError(null);
        password_confirm.setError(null);

        String mName = name.getText().toString().trim();
        String mEmail = email.getText().toString().trim();
        String mMobile = mobile.getText().toString().trim();
        String mReferral = referral.getText().toString().trim();
        String mPass= password.getText().toString().trim();
        String mPassCon = password_confirm.getText().toString().trim();

        if(mName.length()==0){
            name.setError("Please Enter Name");
            name.requestFocus();
            return;
        }
        if(mEmail.length()==0){
            email.setError("Please Enter Email");
            email.requestFocus();
            return;
        }if(!Validation.isEmailValid(mEmail)){
            email.setError("Please Enter Valid Email");
            email.requestFocus();
            return;
        }
        if(mMobile.length()==0){
            mobile.setError("Please Enter Mobile Number");
            mobile.requestFocus();
            return;
        }
        if(mMobile.length()!=10){
            mobile.setError("Please Enter Valid Mobile Number");
            mobile.requestFocus();
            return;
        }if(mPass.length()==0){
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }if(mPass.length()<=5){
            password.setError("Please Enter Minimum 6 Character password");
            password.requestFocus();
            return;
        }if(mPassCon.length()==0){
            password_confirm.setError("Please Enter Password Confirm");
            password_confirm.requestFocus();
            return;
        }if(!mPass.equals(mPassCon)){
            password_confirm.setError("Confirm Password Not Match");
            password_confirm.requestFocus();
            return;
        }
        showProgress(true);
        HashMap<String,String> data = new HashMap<>();
        data.put(Tag.SIGN_UP_NAME,name.getText().toString());
        data.put(Tag.SIGN_UP_EMAIL,email.getText().toString());
        data.put(Tag.SIGN_UP_MOBILE,mobile.getText().toString());
        data.put(Tag.SIGN_UP_REFERRAL,referral.getText().toString());
        data.put(Tag.SIGN_UP_PASSWORD,password.getText().toString());
        mAuthTask = new UserSignUpTask(data);
        mAuthTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {
        private final HashMap<String,String> mData;
        private int status = 0;
        private String otp_code;
        private String otp_id;
        public UserSignUpTask(HashMap<String, String> mData) {
            this.mData = mData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.SIGN_UP_NAME,mData.get(Tag.SIGN_UP_NAME));
                data.put(Tag.SIGN_UP_EMAIL,mData.get(Tag.SIGN_UP_EMAIL));
                data.put(Tag.SIGN_UP_MOBILE,mData.get(Tag.SIGN_UP_MOBILE));
                data.put(Tag.SIGN_UP_REFERRAL,mData.get(Tag.SIGN_UP_REFERRAL));
                data.put(Tag.SIGN_UP_PASSWORD,mData.get(Tag.SIGN_UP_PASSWORD));
                data.put(Tag.USER_ACTION, Tag.SIGN_UP);
                String s =server.doPost(data);
                //Log.d("RESPONSE : ","RAJESH : "+s);
                JsonParser jsonParser=  new JsonParser(s);
                //Log.d("RESPONSE", jsonParser.getJsonObject().toString());
                JSONObject jsonObject=jsonParser.getJsonObject();
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        if(jsonObject.has(Tag.SIGN_UP)){
                            status = jsonObject.getInt(Tag.SIGN_UP);
                            if(status==1){
                                if(jsonObject.has(Tag.OTP_CODE)){
                                    otp_code = jsonObject.getString(Tag.OTP_CODE);
                                }
                                if(jsonObject.has(Tag.OTP_ID)){
                                    otp_id = jsonObject.getString(Tag.OTP_ID);
                                }
                            }
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
            CustomDialog dialog = new CustomDialog(SignUp.this);
		/*
			success 		: 	1
			invalid email 	:	2
			exits email		:	3
			invalid mobile	:	4
			exits mobile	:	5
			invalid password:	6
			invalid referral:	7
			invalid name	:	8
		*/
            switch (status){
                case 99:
                    dialog.setMessage(getString(R.string.sign_up_error));
                    dialog.show();
                    break;
                case 1:
                    Intent intent = new Intent(SignUp.this,Otp.class);
                    intent.putExtra(Tag.OTP_ID,otp_id);
                    intent.putExtra(Tag.OTP_CODE,otp_code);
                    startActivity(intent);
                    break;
                case 2:
                    dialog.setMessage(getString(R.string.sign_up_error_email));
                    dialog.show();
                    break;
                case 3:
                    dialog.setMessage(getString(R.string.sign_up_already_exits_email));
                    dialog.show();
                    break;
                case 4:
                    dialog.setMessage(getString(R.string.sign_up_error_mobile));
                    dialog.show();
                    break;
                case 5:
                    dialog.setMessage(getString(R.string.sign_up_already_exits_mobile));
                    dialog.show();
                    break;
                case 6:
                    dialog.setMessage(getString(R.string.sign_up_error_password));
                    dialog.show();
                    break;
                case 7:
                    dialog.setMessage(getString(R.string.sign_up_error_referral));
                    dialog.show();
                    break;
                case 8:
                    dialog.setMessage(getString(R.string.sign_up_error_name));
                    dialog.show();
                    break;
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
