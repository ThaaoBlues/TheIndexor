package com.example.theindexor

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class Webview : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        var url = intent.getStringExtra("url").toString()
        val website_root = url.split("/")[2]

        var webView = findViewById<WebView>(R.id.webView)

        val webSettings: WebSettings = webView.getSettings()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                // block all redirections outside website
                if (request.url.toString().contains(website_root) ){
                    view.loadUrl(url)
                }
                return true
            }



        }





        webView.loadUrl(url)


    }


}