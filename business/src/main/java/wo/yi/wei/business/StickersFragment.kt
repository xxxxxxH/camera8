package wo.yi.wei.business

import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lcw.library.stickerview.Sticker
import hui.shou.tao.base.*
import me.lwb.bindingadapter.BindingAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wo.yi.wei.business.databinding.FragmentStickersBinding
import wo.yi.wei.business.databinding.ListItemBinding
import xEvent


class StickersFragment : BaseFragment<FragmentStickersBinding>(FragmentStickersBinding::inflate) {
    override fun initialization() {
        EventBus.getDefault().register(this)
        (requireActivity() as BaseActivity<*>).displayNativeAd {
            fragmentBinding.adView.removeAllViews()
            fragmentBinding.adView.addView(it)
        }
        (requireActivity() as AppCompatActivity).stickersDataCollection {
            val adapter = BindingAdapter(ListItemBinding::inflate, it) { _, item ->
                Glide.with(this@StickersFragment).load(item).into(binding.itemImage)
                binding.root.setOnClickListener {
                    if (TextUtils.isEmpty(imageUrl)){
                        (requireActivity() as AppCompatActivity).showSelectDialog()
                    }else{
                        val b = BitmapFactory.decodeResource(this@StickersFragment.resources, item)
                        fragmentBinding.sticker.addSticker(Sticker(requireActivity(), b))
                    }
                }
            }
            fragmentBinding.recycler.layoutManager = GridLayoutManager(requireActivity(), 4)
            fragmentBinding.recycler.adapter = adapter
        }
        fragmentBinding.save.setOnClickListener {
            if (TextUtils.isEmpty(imageUrl)){
                (requireActivity() as AppCompatActivity).showSelectDialog()
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