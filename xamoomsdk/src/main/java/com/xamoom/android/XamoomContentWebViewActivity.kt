package com.xamoom.android

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xamoom.android.xamoomsdk.Helpers.ColorHelper
import com.xamoom.android.xamoomsdk.R

class XamoomContentWebViewActivity: AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        toolbar = findViewById(R.id.webview_toolbar)
        webView = findViewById(R.id.webView)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)

        val urlString = intent.getStringExtra("url") ?: "https://xamoom.net"
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = ""

            val upArrow = resources.getDrawable(R.drawable.ic_arrow_back)
            upArrow.setColorFilter(ColorHelper.getInstance().barFontColor, PorterDuff.Mode.SRC_ATOP)
            actionBar.setHomeAsUpIndicator(upArrow)
        }

        val secureString = urlString.replace("http://", "https://", true)
        openWebViewWithUrl(secureString)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openWebViewWithUrl(url: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                Log.e("url", url)
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

    }
}