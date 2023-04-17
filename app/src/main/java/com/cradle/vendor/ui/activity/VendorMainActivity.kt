package com.cradle.vendor.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityVendorMainBinding
import com.cradle.vendor.ui.fragment.ReceivedOrderFragment
import com.cradle.vendor.ui.fragment.VendorProfileFragment

import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_main.*
import kotlinx.android.synthetic.main.activity_vendor_main.*
import kotlinx.android.synthetic.main.fragment_vendor_receivedorder.*

class VendorMainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityVendorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityVendorMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        // mBinding.setVariable(BR.onOtpClick,this)

        onClick()
    }

    private fun onClick() {
        ll_profile.setOnClickListener(this)
        ll_delivered.setOnClickListener(this)
        ll_received_order.setOnClickListener(this)
        ll_processing.setOnClickListener(this)

    }

    private fun openReceviedFrag() {
        iv_received_order.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP)
        iv_processing.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_delivered.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_profile.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        tv_received_vendor.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_process_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_delivered_order_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_profile_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))

    }
    fun addFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun openProcessingFrag() {
        iv_received_order.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_processing.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_delivered.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_profile.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        tv_received_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_process_vendor.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_delivered_order_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_profile_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))

    }

    private fun openDeliveredFrag() {
        iv_received_order.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_processing.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_delivered.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_profile.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        tv_received_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_process_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_delivered_order_vendor.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_profile_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))

    }

    private fun openProfileFrag() {
        iv_received_order.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_processing.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_delivered.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_profile.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        tv_received_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_process_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_delivered_order_vendor.setTextColor(ContextCompat.getColor(this, R.color.home_text))
        tv_profile_vendor.setTextColor(ContextCompat.getColor(this, R.color.black))

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_received_order -> {
           openReceviedFrag()
                addFragment(ReceivedOrderFragment())
            }
            R.id.ll_processing -> {
                openProcessingFrag()
              //  addFragment(ReceivedOrderFragment())

            }
            R.id.ll_delivered -> {
                openDeliveredFrag()

            }
            R.id.ll_profile -> {
                openProfileFrag()
                addFragment(VendorProfileFragment())

            }

        }
    }
}