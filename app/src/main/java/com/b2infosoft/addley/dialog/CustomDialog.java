package com.b2infosoft.addley.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by rajesh on 4/16/2016.
 */

public class CustomDialog {
    private AlertDialog.Builder builder;
    public CustomDialog(Activity context) {
        builder= new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Alert");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }
    public void setMessage(String message){
        builder.setMessage(message);
    }
    public void show(){
       AlertDialog dialog=  builder.create();
        dialog.show();
    }
}
