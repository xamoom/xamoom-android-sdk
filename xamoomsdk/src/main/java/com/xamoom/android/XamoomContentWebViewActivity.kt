package com.xamoom.android

import android.content.ActivityNotFoundException
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xamoom.android.xamoomsdk.Helpers.ColorHelper
import com.xamoom.android.xamoomsdk.R
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil

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
            if (ColorHelper.getInstance() != null) {
                upArrow.setColorFilter(ColorHelper.getInstance().barFontColor, PorterDuff.Mode.SRC_ATOP)
            }
            actionBar.setHomeAsUpIndicator(upArrow)
        }

        val secureString = urlString.replace("http://", "https://", true)
        openWebViewWithUrl(secureString)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openWebViewWithUrl(url: String) {

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if( URLUtil.isNetworkUrl(url) ) {
                    url?.let { view!!.loadUrl(it) }
                    return true
                }

                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                } catch (e: ActivityNotFoundException) {
                    println("Package error $e")
                }

                //return true anyway to prevent wrong url scheme error when opening links other than http(s):, tel:, mailto:
                //clicking on links that are not supported does nothing
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                url?.let { Log.e("url", it) }
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

    }
}