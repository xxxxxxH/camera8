package wo.yi.wei.wanglexiangnian

import android.content.Intent
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.hjq.permissions.XXPermissions
import hui.shou.tao.base.*
import org.greenrobot.eventbus.EventBus
import wo.yi.wei.business.AgeFragment
import wo.yi.wei.business.CartoonFragment
import wo.yi.wei.business.SlimmingFragment
import wo.yi.wei.business.StickersFragment
import wo.yi.wei.wanglexiangnian.databinding.ActivityMainBinding
import xEvent


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initialization() {
        XXPermissions.with(this).permission(permissions).request { _, all ->
            if (all) {
                if (TextUtils.isEmpty(imageUrl)){
                    showSelectDialog()
                }
                val fragmentCollections = ArrayList<Fragment>()
                fragmentCollections.add(StickersFragment())
                fragmentCollections.add(SlimmingFragment())
                fragmentCollections.add(CartoonFragment())
                fragmentCollections.add(AgeFragment())
                activityBinding.tabLayout.setViewPager(
                    activityBinding.viewpager,
                    bottomTitle,
                    this,
                    fragmentCollections
                )
                activityBinding.viewpager.offscreenPageLimit = 4
                activityBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        displayInsertAd()
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 10086) {
                data?.let {
                    val list = it.getStringArrayListExtra("result")
                    list?.let {
                        imageUrl = it[0]
                        EventBus.getDefault().post(xEvent("showImage"))
                    }
                }
            } else if (requestCode == 996) {
                data?.let {
                    imageUrl = it.getStringExtra("result").toString()
                    EventBus.getDefault().post(xEvent("showImage"))
                }
            }
            if (TextUtils.isEmpty(imageUrl)){
                showSelectDialog()
            }
        }
    }

    override fun onBackPressed() {
        showExitDialog()
    }
}