package com.example.fightwithcovid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class does extends AppCompatActivity {


    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_does);
        webView=(WebView)findViewById(R.id.does_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://visme.co/blog/coronavirus-prevention/");
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
