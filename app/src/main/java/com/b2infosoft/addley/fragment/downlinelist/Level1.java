package com.b2infosoft.addley.fragment.downlinelist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class Level1 extends Fragment {

    TableLayout tableLayout;
    TableRow.LayoutParams tableRowParams;
    TableLayout.LayoutParams layoutParams;
    private TableLayout.LayoutParams layout_params_table_row;

    private View mProgressView;
    private View mMainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_down_line_list_lavel_1_layout,container,false);

        mProgressView = view.findViewById(R.id.gown_line_level_1_progress_bar);
        mMainView = view.findViewById(R.id.down_line_list_level_1_scroll_view_level_1);

        layout_params_table_row=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT, 1.0f);
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.height=30;
        tableRowParams.weight = 1;
        layoutParams =  new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.FILL_PARENT, 1.0f);
/*
        tableRow.setGravity(Gravity.CENTER);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT, 1.0f));
*/
        tableLayout = (TableLayout)view.findViewById(R.id.down_line_list_level_1_table_layout_level_1);
        init();
        new DownLineTask().execute();
        return view;
    }
    private void init(){
        tableLayout.removeAllViews();
        TableRow tableRow =new TableRow(getActivity());
        tableRow.setGravity(Gravity.CENTER);
        tableRow.setBackgroundResource(R.color.row_even);
        tableRow.setLayoutParams(layout_params_table_row);
        tableRow.setPadding(0, 20, 0, 20);
        TextView textView =new TextView(getActivity());
        textView.setText("Date");
        textView.setGravity(Gravity.CENTER);
        tableRow.addView(textView);
        textView =new TextView(getActivity());
        textView.setText("Code");
        textView.setGravity(Gravity.CENTER);
        tableRow.addView(textView);
        textView =new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("Customer Name");
        tableRow.addView(textView);
        tableLayout.addView(tableRow);
    }
    private class DownLineTask  extends AsyncTask<String, Void,Integer> {
        private JSONArray object;
        @Override
        protected void onPreExecute() {
            mProgressView.setVisibility(View.VISIBLE);
            mMainView.setVisibility(View.GONE);
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Integer doInBackground(String... params) {
            try {
                Server server =new Server(Urls.getUserAction());
                HashMap<String,String> data = new HashMap<>();
                data.put(Tag.USER_ID, Login.getValue(getContext(), Tag.USER_ID));
                data.put(Tag.USER_ACTION,Tag.DOWN_LINE_LIST_LEVEL_1);
                JsonParser jsonParser=  new JsonParser(server.doPost(data));
                JSONObject jsonObject=jsonParser.getJsonObject();
//                Log.d("Response", jsonObject.toString());
                if(jsonObject.has(Response.JSON_SUCCESS)){
                    int success = jsonObject.getInt(Response.JSON_SUCCESS);
                    if(success==1){
                        object = jsonObject.getJSONArray(Tag.DOWN_LINE);
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
            mProgressView.setVisibility(View.GONE);
            mMainView.setVisibility(View.VISIBLE);
            if(response==1){
                parseResult(object);
            }else{
                //Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(JSONArray array){
        try {
            init();
            int j=1;
            for (int i=0;i<array.length();i++){
                TableRow tableRow =new TableRow(getActivity());
                tableRow.setLayoutParams(layout_params_table_row);
                tableRow.setPadding(0, 20, 0, 20);
                if(j%2==0){
                    tableRow.setBackgroundResource(R.color.row_even);
                }else{
                    tableRow.setBackgroundResource(R.color.row_odd);
                }
                j++;
                tableRow.setLayoutParams(tableRowParams);
                JSONObject object1 = array.getJSONObject(i);
                TextView textView = new TextView(getActivity());
                textView.setText(object1.getString(Tag.DOWN_LINE_DATE));
                textView.setGravity(Gravity.CENTER);
                TextView textView1 = new TextView(getActivity());
                textView1.setText(object1.getString(Tag.DOWN_LINE_CODE));
                textView1.setGravity(Gravity.CENTER);

                TextView textView2 = new TextView(getActivity());
                String str = object1.getString(Tag.DOWN_LINE_CUSTOMER_NAME);
                if(str.length()>10) {
                    str = str.substring(0, 9);
                    str = str.concat("...");
                }
                textView2.setText(str);
                //textView2.setText(object1.getString(Tag.DOWN_LINE_CUSTOMER_NAME));
                textView2.setGravity(Gravity.CENTER);
                textView.setPadding(0, 0, 5, 0);
                textView.setPadding(5,0,0,0);
                tableRow.addView(textView);
                tableRow.addView(textView1);
                tableRow.addView(textView2);
                tableLayout.addView(tableRow);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e("Error BestOffer_1",e.toString());
        }
    }
}