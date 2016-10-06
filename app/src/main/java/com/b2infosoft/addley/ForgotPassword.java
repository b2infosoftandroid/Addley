package com.b2infosoft.addley;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class ForgotPassword extends AppCompatActivity {
    private View mProgressView;
    private AutoCompleteTextView mForgotPasswordEmail;
    private Button mForgotPasswordAction;
    private TextView mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressView = findViewById(R.id.forgot_password_progress);
        mForgotPasswordEmail = (AutoCompleteTextView)findViewById(R.id.forgot_password_email);
        mForgotPasswordEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mForgotPasswordEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mForgotPasswordAction= (Button)findViewById(R.id.forgot_password_action_button);
        mForgotPasswordAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionForgotPassword()){
                    String email = mForgotPasswordEmail.getText().toString().trim();
                    new ForgotPasswordTask(email).execute();
                }
            }
        });
        mLogin = (TextView)findViewById(R.id.forgot_password_action_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ForgotPassword.this);
                finish();
            }
        });
    }
    private boolean actionForgotPassword(){
        mForgotPasswordEmail.setError(null);
        String email = mForgotPasswordEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            mForgotPasswordEmail.setError("Please Enter Email Id");
            return false;
        }else if (!Validation.isEmailValid(email)) {
            mForgotPasswordEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class ForgotPasswordTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        ForgotPasswordTask(String email) {
            mEmail = email;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_EMAIL,mEmail);
                data.put(Tag.USER_ACTION,Tag.FORGOT_PASSWORD);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    return success;
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
        protected void onPostExecute(Integer success) {
            CustomDialog dialog = new CustomDialog(ForgotPassword.this);
            switch (success){
                case 1:
                    dialog.setMessage("Login Details Sent Successfully to your Email and Mobile!");
                    break;
                case 2:
                    dialog.setMessage("Sorry no such Account exists");
                    break;
                default:
                    dialog.setMessage("Some Error Accured");
            }
            dialog.show();
        }
        @Override
        protected void onCancelled() {

        }
    }

}
