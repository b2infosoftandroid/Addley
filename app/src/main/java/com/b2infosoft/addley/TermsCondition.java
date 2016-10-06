package com.b2infosoft.addley;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.b2infosoft.addley.global.Urls;

public class TermsCondition extends AppCompatActivity {
    private WebView terms_condition_web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        terms_condition_web_view = (WebView)findViewById(R.id.terms_condition_web_view);
        terms_condition_web_view.setWebViewClient(new MyBrowser());
        terms_condition_web_view.getSettings().setLoadsImagesAutomatically(true);
        terms_condition_web_view.getSettings().setJavaScriptEnabled(true);
        terms_condition_web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        terms_condition_web_view.loadUrl(Urls.getTermsCondition());
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
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
