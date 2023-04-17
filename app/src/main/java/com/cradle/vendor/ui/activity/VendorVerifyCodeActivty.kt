package com.cradle.vendor.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityVendorVerifyCodeBinding
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class VendorVerifyCodeActivty: BaseActivity(){

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityVendorVerifyCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }
    private fun initUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityVendorVerifyCodeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onOtpClick,this)

        onClick()
    }

    private fun onClick() {

    }
}