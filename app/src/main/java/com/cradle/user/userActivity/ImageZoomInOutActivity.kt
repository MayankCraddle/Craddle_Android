package com.cradle.user.userActivity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityImagezoominoutBinding
import com.cradle.user.adapters.ImageViewPagerAdapter
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.*


class ImageZoomInOutActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityImagezoominoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityImagezoominoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        val intent = intent
        try {
            val args = intent.getBundleExtra("BUNDLE")
            val list = args!!.getSerializable("LIST") as ArrayList<String>?
            val imageAndVideo = args.getString("ImageVideo")
            if(imageAndVideo.equals("image")){

            }else{
                list!!.removeAt(0)
            }
            val type = args.getString("type")
            if(type.equals("Maltimedia")){
                mBinding.tvDis.text=SharaGoPref.getInstance(this).getShortDis("")
                mBinding.tvSub.text=SharaGoPref.getInstance(this).getLogDis("")
            }else{
                mBinding.tvDis.visibility=View.GONE
                mBinding.tvSub.visibility=View.GONE
            }

            val adapter = ImageViewPagerAdapter(
                this,
                list,
                imageAndVideo.toString()
            )
            SharaGoPref.getInstance(this).getShortDis("")
            mBinding.photosViewpager.adapter = adapter
        }catch (e:Exception){}

        //mBinding.zoomInOutAdapter= ImageViewPagerAdapter(this,list,imageAndVideo)
        //mBinding.zoomInOutAdapter= ZoomInOutAdapter(this,list,imageAndVideo)

        iv_back.setOnClickListener { finish() }
    }

    override fun onClick(p0: View?) {

    }
}