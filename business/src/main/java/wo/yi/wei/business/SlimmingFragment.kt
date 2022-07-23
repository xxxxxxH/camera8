package wo.yi.wei.business

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import hui.shou.tao.base.*
import me.lwb.bindingadapter.BindingAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wo.yi.wei.business.databinding.FragmentSlimmingBinding
import wo.yi.wei.business.databinding.ListItemBinding
import xEvent


class SlimmingFragment : BaseFragment<FragmentSlimmingBinding>(FragmentSlimmingBinding::inflate) {
    override fun initialization() {
        EventBus.getDefault().register(this)
        (requireActivity() as BaseActivity<*>).displayNativeAd {
            fragmentBinding.adView.removeAllViews()
            fragmentBinding.adView.addView(it)
        }
        (requireActivity() as AppCompatActivity).slimmingDataCollection {
            val adapter = BindingAdapter(ListItemBinding::inflate, it) { _, item ->
                binding.itemImage.setBackgroundResource(item)
                binding.root.setOnClickListener {
                    if (TextUtils.isEmpty(imageUrl)){
                        (requireActivity() as AppCompatActivity).showSelectDialog()
                    }
                }
            }
            fragmentBinding.recycler.layoutManager = GridLayoutManager(requireActivity(), 5)
            fragmentBinding.recycler.adapter = adapter
        }
        fragmentBinding.save.setOnClickListener {
            if (TextUtils.isEmpty(imageUrl)){
                (requireActivity() as AppCompatActivity).showSelectDialog()
            }else{
                (requireActivity() as AppCompatActivity).showSaveDialog()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: xEvent) {
        if (e.getMessage()[0] == "showImage") {
            Glide.with(this).load(imageUrl).into(fragmentBinding.imageView)
        }
    }
}