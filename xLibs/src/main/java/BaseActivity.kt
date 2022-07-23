package hui.shou.tao.base

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.anythink.splashad.api.IATSplashEyeAd
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wo.yi.wei.xlibs.R

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var activityBinding: T
    private val binding get() = activityBinding
    private var isBackground = false
    private lateinit var openAd: ATSplashAd
    private lateinit var lovinInterstitialAd: MaxInterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = getViewBinding()
        setContentView(binding.root)
        openAd = xApp.instance.openAd(OpenAdListener())
        openAd.loadAd()
        openAd.loge("xxxxxxHopenAd")

        lovinInterstitialAd = xApp.instance.lovinInterstitialAd(this)
        lovinInterstitialAd.setListener(LovinInsterListener())
        lovinInterstitialAd.loadAd()
        lovinInterstitialAd.loge(lovinInterstitialAd)
        initialization()

        addBannerAd()
    }

    protected abstract fun getViewBinding(): T

    abstract fun initialization()

    open fun onInterstitialAdHidden() {}
    open fun onInterstitialAdLoaded() {}
    open fun onSplashAdLoaded() {}

    private fun addBannerAd() {
        val content = findViewById<ViewGroup>(android.R.id.content)
        val frameLayout = FrameLayout(this)
        val p = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = p

        val linearLayout = LinearLayout(this)
        val p1 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.layoutParams = p1

        val banner = xApp.instance.lovinBanner()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            banner.loadAd()
            withContext(Dispatchers.Main) {
                val p2 =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp2px(this@BaseActivity, 50f)
                    )
                p2.gravity = Gravity.BOTTOM
                banner.layoutParams = p2
                linearLayout.addView(banner)
                frameLayout.addView(linearLayout)
                content.addView(frameLayout)
            }
        }
    }

    inner class OpenAdListener : ATSplashAdListener {
        override fun onAdLoaded() {
            "xxxxxxH".loge("openAdonAdLoaded")
        }

        override fun onNoAdError(p0: AdError?) {
            "xxxxxxH".loge("openAdonNoAdError $p0")
            lifecycleScope.launch(Dispatchers.IO) {
                delay(3000)
                openAd.onDestory()
                openAd = xApp.instance.openAd(this@OpenAdListener)
                openAd.loadAd()
            }
        }

        override fun onAdShow(p0: ATAdInfo?) {
            "xxxxxxH".loge("openAdonAdShow")
        }

        override fun onAdClick(p0: ATAdInfo?) {
            "xxxxxxH".loge("openAdonAdClick")
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: IATSplashEyeAd?) {
            "xxxxxxH".loge("openAdonAdDismiss")
            onSplashAdLoaded()
            lifecycleScope.launch(Dispatchers.IO) {
                delay(3000)
                openAd.onDestory()
                openAd = xApp.instance.openAd(this@OpenAdListener)
                openAd.loadAd()
            }
        }
    }

    inner class LovinInsterListener : MaxAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            "xxxxxxH".loge("lovinonInsertAdLoaded")
            lovinInterstitialAd.loge(lovinInterstitialAd)
        }

        override fun onAdDisplayed(ad: MaxAd?) {
            "xxxxxxH".loge("xxxxxxHlovinonAdDisplayed")
        }

        override fun onAdHidden(ad: MaxAd?) {
            lastTime = System.currentTimeMillis()
            "xxxxxxH".loge("xxxxxxHlovinonAdHidden")
            lifecycleScope.launch(Dispatchers.IO) {
                lovinInterstitialAd.destroy()
                delay(3000)
                lovinInterstitialAd = xApp.instance.lovinInterstitialAd(this@BaseActivity)
                lovinInterstitialAd.setListener(this@LovinInsterListener)
                lovinInterstitialAd.loadAd()
                lovinInterstitialAd.loge(lovinInterstitialAd)
            }
            onInterstitialAdHidden()
        }

        override fun onAdClicked(ad: MaxAd?) {
            "xxxxxxH".loge("xxxxxxHlovinonAdClicked")
        }

        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            "xxxxxxH".loge("xxxxxxHlovinonAdLoadFailed")
            lifecycleScope.launch(Dispatchers.IO) {
                lovinInterstitialAd.destroy()
                delay(3000)
                lovinInterstitialAd = xApp.instance.lovinInterstitialAd(this@BaseActivity)
                lovinInterstitialAd.setListener(this@LovinInsterListener)
                lovinInterstitialAd.loadAd()
                lovinInterstitialAd.loge(lovinInterstitialAd)
            }
        }

        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
            "xxxxxxH".loge("xxxxxxHonAdDisplayFailed")
            lifecycleScope.launch(Dispatchers.IO) {
                lovinInterstitialAd.destroy()
                delay(3000)
                lovinInterstitialAd = xApp.instance.lovinInterstitialAd(this@BaseActivity)
                lovinInterstitialAd.setListener(this@LovinInsterListener)
                lovinInterstitialAd.loadAd()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isBackground = isInBackground()
    }

    override fun onResume() {
        super.onResume()
        if (isBackground) {
            isBackground = false
            val content = findViewById<ViewGroup>(android.R.id.content)
            (content.getTag(R.id.open_ad_view_id) as? FrameLayout)?.let {
                displayOpenAd(it)
            } ?: kotlin.run {
                FrameLayout(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    content.addView(this)
                    content.setTag(R.id.open_ad_view_id, this)
                    displayOpenAd(this)
                }
            }
        }
    }


    private fun displayOpenAdImpl(viewGroup: ViewGroup, tag: String = ""): Boolean {
        openAd.let {
            if (it.isAdReady) {
                it.show(this, viewGroup)
                return true
            }
        }
        return false
    }


    private fun displayInsertAdImpl(isForce: Boolean = false, tag: String = ""): Boolean {
        lovinInterstitialAd.let {
            if (it.isReady) {
                it.showAd(tag)
                return true
            }
        }
        return false
    }


    fun displayOpenAd(viewGroup: ViewGroup, isForce: Boolean = false): Boolean {
        if (config["i"] == 1) {
            return displayInsertAd(isForce = isForce)
        } else {
            return displayOpenAdImpl(viewGroup)
        }
    }

    fun displayInsertAd(
        showByPercent: Boolean = false,
        isForce: Boolean = false,
        tag: String = ""
    ): Boolean {
        if (isForce) {
            return displayInsertAdImpl(isForce = isForce)
        } else {
            if (config["l"] == 1) {
                if ((showByPercent && isShowInsertAd()) || (!showByPercent)) {
                    if (System.currentTimeMillis() - lastTime > insertAdOffset() * 1000) {
                        var showInsertAd = false
                        if (shownList.getOrNull(shownIndex) == true) {
                            showInsertAd = displayInsertAdImpl()
                        }
                        shownIndex++
                        if (shownIndex >= shownList.size) {
                            shownIndex = 0
                        }
                        return showInsertAd
                    }
                }
            }
            return false
        }
    }

    fun displayNativeAd(show: (MaxNativeAdView?) -> Unit) {
        val lovinLoader = xApp.instance.lovinNative()
        lovinLoader.loadAd()
        lovinLoader.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                "$p0 $p1".loge("xxxxxxHonNativeAdLoaded")
                show(p0)
            }

            override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                super.onNativeAdLoadFailed(p0, p1)
                "$p0 $p1".loge("xxxxxxHonNativeAdLoadFailed")
            }
        })
    }
}