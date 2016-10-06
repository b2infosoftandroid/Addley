package com.b2infosoft.addley.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Tag;

/**
 * Created by rajesh on 4/19/2016.
 */

public class ReferEarn extends Fragment {
    private ProgressDialog progressDialog;
    public ReferEarn() {
        
    }
    private Button share,copy_clipboard;
    private EditText user_code;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refer_earn, container, false);
        share = (Button)view.findViewById(R.id.refer_earn_action_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Login.getValue(getActivity().getApplicationContext(), Tag.REFERRAL_LINK));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });
        copy_clipboard = (Button)view.findViewById(R.id.refer_earn_action_copy_clipboard);
        copy_clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(user_code.getText().toString());
            }
        });
        user_code = (EditText)view.findViewById(R.id.refer_earn_code);
        user_code.setText(Login.getValue(getActivity().getApplicationContext(), Tag.REFERRAL_CODE));
        return view;
    }
    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)getActivity().
                    getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("Your CODE", copyText);
            clipboard.setPrimaryClip(clip);
        }
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Your CODE is copied", Toast.LENGTH_SHORT);
        toast.show();
        //displayAlert("Your OTP is copied");
    }
}
