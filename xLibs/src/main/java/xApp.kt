package hui.shou.tao.base

import android.app.Activity
import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.anythink.core.api.ATSDK
import com.anythink.core.api.NetTrafficeCallback
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkSettings
import com.bumptech.glide.Glide
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import com.tencent.mmkv.MMKV
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.common.ImageLoader
import wo.yi.wei.xlibs.BuildConfig
import wo.yi.wei.xlibs.R

class xApp : Application() {
    companion object {
        lateinit var instance: xApp
    }

    private val LOVINSDK by lazy {
        AppLovinSdk.getInstance(
            this.getString(R.string.lovin_app_key).reversed(),
            AppLovinSdkSettings(this),
            this
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this)
        DialogX.init(this)
        DialogX.globalTheme = DialogX.THEME.LIGHT
        DialogX.globalStyle = IOSStyle()
        init()
        ISNav.getInstance().init(ImageLoader { context, path, imageView ->
            Glide.with(context).load(path).into(imageView)
        })
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            if (instance.packageName != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }

        ATSDK.checkIsEuTraffic(instance, object : NetTrafficeCallback {
            override fun onResultCallback(isEU: Boolean) {
                if (isEU && ATSDK.getGDPRDataLevel(instance) == ATSDK.UNKNOWN) {
                    ATSDK.showGdprAuth(instance)
                }
            }

            override fun onErrorCallback(errorMsg: String) {
            }
        })

        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)
        ATSDK.integrationChecking(instance)
        ATSDK.init(
            instance,
            getString(R.string.top_on_app_id),
            getString(R.string.top_on_app_key)
        )
    }

    fun lovinInterstitialAd(ac: Activity): MaxInterstitialAd {
        return MaxInterstitialAd(getString(R.string.lovin_insert_ad_id), LOVINSDK, ac)
    }

    fun lovinNative(): MaxNativeAdLoader {
        return MaxNativeAdLoader(this.getString(R.string.lovin_native_ad_id), LOVINSDK, this)
    }

    fun lovinBanner(): MaxAdView {
        return MaxAdView(this.getString(R.string.lovin_banner_ad_id), LOVINSDK, this)
    }

    fun openAd(listener: ATSplashAdListener?): ATSplashAd {
        return ATSplashAd(this, this.getString(R.string.top_on_open_ad_id), listener)
    }
}