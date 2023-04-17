package com.cradle.common_screen

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityWelcomeUserVendorBinding
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.vendor.ui.activity.VendorLoginActivty

import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory


class WelcomeUserVendorActivity:BaseActivity() {
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityWelcomeUserVendorBinding
    var userType:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initUI()
    }

    private fun initUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityWelcomeUserVendorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onOtpClick,this)

        onClick()
    }

    private fun onClick(){
        mBinding.rlLoginUser.setOnClickListener {
            onClickLoginUser()
        }
        mBinding.loginVendor.setOnClickListener {
            onClickVendorUser()
        }
        mBinding.ivNext.setOnClickListener {
            userLogin()
        }
    }

    private fun onClickLoginUser(){
        SharaGoPref.getInstance(this).setUserType(R.string.user.toString())
        userType=getString(R.string.user)
        mBinding.ivLoginUser.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        mBinding.rlLoginUser.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))

        mBinding.ivLoginVendor.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP)
        mBinding.loginVendor.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

    }
    private fun onClickVendorUser(){
        userType=getString(R.string.vendor_new)
        SharaGoPref.getInstance(this).setUserType(R.string.home.toString())
        mBinding.ivLoginVendor.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        mBinding.loginVendor.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))

        mBinding.ivLoginUser.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP)
        mBinding.rlLoginUser.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }
    private fun userLogin(){

        if(userType!!.isNotEmpty()){
            if(userType!!.equals(getString(R.string.user)))
            {
                startActivity(Intent(this, UserLoginActivity::class.java))
                /*  SharaGoPref.getInstance(this).setWhichFrag("Home")
                  startActivity(Intent(this@SplashScreenActivity, UserMainActivity::class.java))
                  finish()*/
            } else{
                startActivity(Intent(this, VendorLoginActivty::class.java))

            }
        }else showToast(getString(R.string.please_select_user_type))

    }


}