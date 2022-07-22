package wo.yi.wei.business

import FastBlur
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import hui.shou.tao.base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.lwb.bindingadapter.BindingAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wo.yi.wei.business.databinding.FragmentCartoonBinding
import wo.yi.wei.business.databinding.ListItemBinding
import xEvent


class CartoonFragment : BaseFragment<FragmentCartoonBinding>(FragmentCartoonBinding::inflate) {
    override fun initialization() {
        EventBus.getDefault().register(this)
        (requireActivity() as BaseActivity<*>).displayNativeAd {
            fragmentBinding.adView.removeAllViews()
            fragmentBinding.adView.addView(it)
        }
        (requireActivity() as AppCompatActivity).cartoonDataCollection {
            val adapter = BindingAdapter(ListItemBinding::inflate, it) { position, item ->
                binding.itemImage.setBackgroundResource(item)
                binding.root.setOnClickListener {
                    if (TextUtils.isEmpty(imageUrl)) {
                        (requireActivity() as AppCompatActivity).showSelectDialog()
                    } else {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val b = BitmapFactory.decodeFile(imageUrl)
                            val bb = FastBlur.doBlur(b, position+1, position+1)
                            withContext(Dispatchers.Main) {
                                Glide.with(this@CartoonFragment).load(bb).into(fragmentBinding.imageView)
                            }
                        }
                    }
                }
            }
            fragmentBinding.recycler.layoutManager = GridLayoutManager(requireActivity(), 4)
            fragmentBinding.recycler.adapter = adapter
        }
        fragmentBinding.save.setOnClickListener {
            if (TextUtils.isEmpty(imageUrl)) {
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