package com.cradle.vendor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.FragmentVendorReceivedorderBinding
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class VendorDeliveredFragment: Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragmentVendorReceivedorderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = FragmentVendorReceivedorderBinding.inflate(inflater)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v = mBinding.root
        mBinding.setVariable(BR.onAccount, this)
        findId()
        statusbarColourChange()
        return v
    }

    private fun statusbarColourChange() {

    }

    private fun findId() {

    }
    fun addFragment(fragment: Fragment, tag: String) {
        childFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}