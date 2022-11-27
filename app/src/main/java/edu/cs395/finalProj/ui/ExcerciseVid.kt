package edu.cs395.finalProj

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.MenuProvider
import edu.cs395.finalProj.databinding.ActivityExcercixeBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer


// XXX Write most of this file
class ExcerciseVid: YouTubeBaseActivity() {

    private var title : String? = ""
    private var api_key =  "AIzaSyDmsthuNYj8U9sIOjVABHiq1Em6tLg19xw"
    private lateinit var binding : ActivityExcercixeBinding
    private lateinit var ytPlayer: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcercixeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        title = intent.getStringExtra("title").toString()

        ytPlayer = binding.webviewPlayerView
        ytPlayer.webViewClient = WebViewClient()
        ytPlayer.webChromeClient = WebChromeClient()
        var webSettings = ytPlayer.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        if (savedInstanceState == null) {
            ytPlayer.loadUrl("https://www.youtube.com/embed/YE7VzlLtp-4")
        }

    }
    private inner class CustomChromeClient : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View?,
            paramCustomViewCallback: CustomViewCallback?
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
            mOriginalOrientation = requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (window.decorView as FrameLayout).addView(mCustomView, FrameLayout.LayoutParams(-1, -1))
            window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ytPlayer.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ytPlayer.restoreState(savedInstanceState)
    }

    private fun finishAct() {


        val returnIntent = Intent().apply {
        }

        setResult(RESULT_OK, returnIntent)
        finish()
        // XXX Write me
    }
}