package wo.yi.wei.wanglexiangnian

import xEvent
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import hui.shou.tao.base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wo.yi.wei.wanglexiangnian.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)

    override fun initialization() {
        getConfig {
            if (login) {
                displayOpen()
                return@getConfig
            }
            if (config["l"] == 1) {
                activityBinding.loginBtn.visibility = View.VISIBLE
                return@getConfig
            }
            displayOpen()
        }
        EventBus.getDefault().register(this)
        activityBinding.loginBtn.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java))
        }
    }

    private fun displayOpen() {
        if (displayOpenAd(activityBinding.niDie, isForce = true)) {
            isDisplayOpen = true
            return
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onInterstitialAdHidden() {
        super.onInterstitialAdHidden()
        if (config["i"] == 1) {
            if (isDisplayOpen) {
                isDisplayOpen = !isDisplayOpen
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onSplashAdLoaded() {
        super.onSplashAdLoaded()
        if (isDisplayOpen) {
            isDisplayOpen = !isDisplayOpen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: xEvent) {
        if (e.getMessage()[0] == "end") {
            finish()
        }
    }
}