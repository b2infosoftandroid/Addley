package com.b2infosoft.addley.fragment;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.global.Response;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.global.Urls;
import com.b2infosoft.addley.server.JsonParser;
import com.b2infosoft.addley.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rajesh on 4/19/2016.
 */
public class GenerateCommission extends Fragment {
    public GenerateCommission() {

    }
    TableLayout tableLayout;
    TableRow.LayoutParams tableRowParams;
    String[] fields = {"Code","Name","Amount","Self Commission","Level1 Commission","Level2 Commission","TDS","Net Amount"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_commission_layout,container,false);

        tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.height=50;
        tableRowParams.weight = 1;

        tableLayout = (TableLayout)view.findViewById(R.id.generate_commission_table_layout);
        tableLayout.removeAllViews();
        TableRow tableRow =new TableRow(getActivity());
        tableRow.setBackgroundResource(R.color.row_even);
        tableRow.setLayoutParams(tableRowParams);
        tableRow.setBackgroundResource(R.color.colorAccent);

        for(String s:fields){
            TextView textView =new TextView(getActivity());
            textView.setText(s);
            textView.setPadding(2,2,2,2);
            textView.setTextColor(Color.WHITE);
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);
        /*
            View view_1 = inflater.inflate(R.layout.dialog_box_commission_generate_add_field_layout,container,false);
            ListView listView = (ListView)view_1.findViewById(R.id.dialog_box_list_view);
            listView.setAdapter(new PopupMenuAdapter(getActivity().getBaseContext(),fields));
            ImageButton imageButton=(ImageButton)view.findViewById(R.id.generate_commission_add_column);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //displayPopupWindow(v);
                    android.support.v7.widget.PopupMenu popupMenu = new android.support.v7.widget.PopupMenu(getActivity(),v);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.show();
                }
            });
        */
        new GenerateCommissionTask().execute();
        return view;
    }

    private void displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_box_commission_generate_add_field_layout, null);
        popup.setContentView(layout);

        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);

    }
    private class GenerateCommissionTask  extends AsyncTask<String, Void,Integer> {
        private JSONArray object;
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION,Tag.GENERATE_COMMISSION);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
                //Log.d("Response", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        object = jsonObject.getJSONArray(Tag.GENERATE_COMMISSION);
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
            if(response==1){
                parseResult(object);
            }else{
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(JSONArray array){
        try {
            int j=1;
            for (int i=0;i<array.length();i++){
                TableRow tableRow =new TableRow(getActivity());
                if(j%2==0){
                    tableRow.setBackgroundResource(R.color.row_even);
                }else{
                    tableRow.setBackgroundResource(R.color.row_odd);
                }
                j++;
                tableRow.setLayoutParams(tableRowParams);
                JSONObject object1 = array.getJSONObject(i);
                TextView textView = new TextView(getActivity());
                textView.setText(object1.getString(Tag.GENERATE_COMMISSION_CODE));
                TextView textView1 = new TextView(getActivity());
                textView1.setText(object1.getString(Tag.GENERATE_COMMISSION_CUSTOMER_NAME));
                TextView textView2 = new TextView(getActivity());
                textView2.setText(object1.getString(Tag.GENERATE_COMMISSION_AMOUNT));
                TextView textView3 = new TextView(getActivity());
                textView3.setText(object1.getString(Tag.GENERATE_COMMISSION_SELF_COMMISSION));
                TextView textView4 = new TextView(getActivity());
                textView4.setText(object1.getString(Tag.GENERATE_COMMISSION_LEVEL1_COMMISSION));
                TextView textView5 = new TextView(getActivity());
                textView5.setText(object1.getString(Tag.GENERATE_COMMISSION_LEVEL2_COMMISSION));
                TextView textView6 = new TextView(getActivity());
                textView6.setText(object1.getString(Tag.GENERATE_COMMISSION_TDS));
                TextView textView7 = new TextView(getActivity());
                textView7.setText(object1.getString(Tag.GENERATE_COMMISSION_NET_AMOUNT));
                textView.setPadding(2, 2, 2, 2);
                textView1.setPadding(2,2,2,2);
                textView2.setPadding(2,2,2,2);
                textView3.setPadding(2, 2, 2, 2);
                textView4.setPadding(2, 2, 2, 2);
                textView5.setPadding(2, 2, 2, 2);
                textView6.setPadding(2, 2, 2, 2);
                textView7.setPadding(2, 2, 2, 2);

                tableRow.addView(textView);
                tableRow.addView(textView1);
                tableRow.addView(textView2);
                tableRow.addView(textView3);
                tableRow.addView(textView4);
                tableRow.addView(textView5);
                tableRow.addView(textView6);
                tableRow.addView(textView7);
                tableLayout.addView(tableRow);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}
