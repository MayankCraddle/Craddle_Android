package com.cradle.vendor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.databinding.FragmentVendorReceivedorderBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.Utility
import com.cradle.vendor.ui.adapter.VendorReceivedOrderAdapter
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class ReceivedOrderFragment : Fragment(), View.OnClickListener {
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

    private fun findId() {

    }
    private fun apiResult(){
        mViewModel.lUuserNewProductsofVendor.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{
                    it.data?.let {
                        if (it.productList!!.isNotEmpty()){
                          //  mBinding.tvNewProduct.visibility=View.VISIBLE
                            mBinding.mVendorReceivedOrderAdapter= VendorReceivedOrderAdapter(requireActivity()/*,it.productList as ArrayList<NewProductListOfVendorListItem?>*/)
                        }
                       // else
                           // mBinding.tvNewProduct.visibility=View.GONE
                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }

    private fun statusbarColourChange() {

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}
