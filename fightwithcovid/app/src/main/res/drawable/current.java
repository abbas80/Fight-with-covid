package com.example.covid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class current extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        webView=(WebView)findViewById(R.id.current_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.worldometers.info/coronavirus/country/india/");
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
