package com.example.theindexor

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
val AUTHORIZED_EXT : List<String> = listOf(".js",
    ".png",".jpg",".jpeg",".raw",".svg",".tiff",".gif",".bmp",".eps",
    ".pcm",".wav",".m4a",".mp3",".aiff",".flac",
    ".mp4",".ogg",".avi",".MTS",".M2TS",".TS",".mov",".amv",".mpg",".mpeg",".svi",".3gp",".3g2",".m4v",".flv"
)


class Webview : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)


        var landing_url = intent.getStringExtra("url").toString()
        val should_click_on_element = intent.getStringExtra("should_click_on_element").toString()
        val website_root = landing_url.split("/")[2]

        var webView = findViewById<WebView>(R.id.webView)

        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptCanOpenWindowsAutomatically = false
        webSettings.setSupportMultipleWindows(false)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                val req_url : String = request.url.toString();
                // block all redirections outside website
                if (req_url.contains(website_root)){
                    return false
                }

                // still allow loading external resources
                for (ext in AUTHORIZED_EXT) {
                    if (req_url.endsWith(ext)){
                        return false
                    }
                }

                //println("refused : $req_url")

                return true

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                // check if we are on the landing url
                if (url == landing_url) {

                    // perform should_click_on_element while it is still here
                    webView.evaluateJavascript("document.getELementsByClassName(\"$should_click_on_element\")[0].click()",fun(s :String){})
                    println("CLICKED ON ELEMENT : $should_click_on_element")
                    // check if we can extract a webview and its stream url to offer a better experience


                    //iframe tag search
                    webView.evaluateJavascript("document.getElementsByTagName(\"iframe\")[0].src",
                        fun(s: String) {
                            if (s != "null") {
                                webView.loadUrl(s)
                            }
                        })

                    //video tag search

                }

            }


        }





     webView.loadUrl(landing_url)

    }


}