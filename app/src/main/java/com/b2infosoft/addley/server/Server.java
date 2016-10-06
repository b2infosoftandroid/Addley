package com.b2infosoft.addley.server;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by rajesh on 4/15/2016.
 */
public class Server {
    private final String USER_AGENT = "Mozilla/5.0";
    private String url;
    public Server(String url) {
        this.url = url;
    }
    public String doPost(HashMap<String,String> params) throws Exception {
        Log.d("URL",url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setUseCaches(false);
        con.setRequestMethod("POST");
//        con.setRequestProperty("Host", "sandeeprahar.com");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  //      con.setRequestProperty("Connection", "keep-alive");
    //    con.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        //con.setRequestProperty("Accept-Encoding", "identity");
        con.setRequestProperty("Authorization",getBasicAuthenticationEncoding());
        con.setDoOutput(true);
        con.setDoInput(true);

        StringBuilder stringBuilder=new StringBuilder();
        if(params.size()>0){
            Iterator<String> iterator = (params.keySet()).iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(params.get(key),"UTF-8"));
                stringBuilder.append("&");
            }
        }
        // Send post request

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(stringBuilder.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        StringBuffer response = new StringBuffer();
        if(responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        con.disconnect();
        /*if(response.length()==0){
            return "{'success':0}";
        }*/
        return response.toString();
    }
    private String getBasicAuthenticationEncoding() {
        String username="sandeeprahar";
        String password="O~Z5xeE._cbn";
        String userPassword = username + ":" + password;
        return "Basic " + new String (Base64.encode(userPassword.getBytes(),Base64.DEFAULT));
    }
}
