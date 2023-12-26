package com.example.kursovoiwebapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    var upload: ValueCallback<Array<Uri>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById<WebView>(R.id.webView)
        setUpMap()
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpMap() {

        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true

        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.loadUrl(
            "http://10.0.2.2:8080/"
        )

        webView.webChromeClient = object: WebChromeClient(){
            override fun onJsAlert(view: WebView?, url: String?, message: String?,result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                var intent = fileChooserParams.createIntent()
                upload = filePathCallback
                startActivityForResult(intent,101)
                return true
            }
        }

        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading( view: WebView,request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101){
            if(upload==null){
                return
            }

            upload!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data))
            upload=null
        }
    }
}
