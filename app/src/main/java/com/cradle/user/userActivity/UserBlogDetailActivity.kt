package com.cradle.user.userActivity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserWriteReviewBinding
import com.cradle.utils.AnalyticsUtils
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_write_review.*

class UserBlogDetailActivity :BaseActivity(),View.OnClickListener{

    private var pref: SharaGoPref? = null
    private lateinit var mBinding: ActivityUserWriteReviewBinding
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserWriteReviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)

        AnalyticsUtils.analyticReport(this, MyConstants.BlogDetailScreen)
        pref = SharaGoPref.getInstance(this)
        rele_notNowRating.setOnClickListener(this)
        rele_submitRating.setOnClickListener(this)
        setCustomToolBar()
        apiResult()
        statusBarColourChange()
    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    private fun apiResult() {

    }

    private fun setCustomToolBar() {

    }

    override fun onClick(p0: View?) {

    }
}