package com.cradle.user.userFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cradle.repository.QuoteRepository
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.databinding.FragmentProbycountryBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.*
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProByCountryFragment :Fragment(){
    private var response: QuoteRepository? =null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var v: View
    private lateinit var mBinding: FragmentProbycountryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        response = (requireActivity().application as ApplicationClass).repository
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_probycountry, container, false)
        v=mBinding.root
        findId()
        return v
    }

    private fun findId() {
        CoroutineScope(Dispatchers.IO).launch {
            /*  launch {
                LoaderFragment.showLoader(requireActivity().supportFragmentManager)
                mainViewModel.getUserWishList()
            }*/
            mBinding.productByCategoryAdapter = ProductByCategoryAdapter(requireActivity())
            mBinding.adapterUserLocalProduct = AdapterUserLocalProduct(requireActivity())
            mBinding.productByCategoryAdapter = ProductByCategoryAdapter(requireActivity())
            mBinding.adapterUserMajorExpoSeg = AdapterUserMajorExpoSeg(requireActivity())
            mBinding.adapterUserAttractionManu = AdapterUserAttractionManu(requireActivity())
            mBinding.userAgricultralMenuAdapter = UserAgricultralMenuAdapter(requireActivity())


        }
    }
    private fun getResult(){
        //user Local Product List result
        mainViewModel.liveDataUserLUserWishList.observe(requireActivity()){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    //   mainBinding.shimmerFrameLayout.stopShimmer()
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    mBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {
                        /* Log.d("countryList" ,it.countries[0].name)
                         mainViewModel.findByStateID(it.countries[0].countryId)*/
                        LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)

                        try {
                            CoroutineScope(Dispatchers.IO).async {
                                async {
                                    /*      mBinding.adapterUserLocalProduct= AdapterUserLocalProduct(this@UserProductListInCountryActivity*//*,it.metaData!!.items*//*)
                                    activityUserProductListInCountryBinding.productByCategoryAdapter= ProductByCategoryAdapter(this@UserProductListInCountryActivity*//*,it.metaData!!.items*//*)
                                    activityUserProductListInCountryBinding.adapterUserMajorExpoSeg= AdapterUserMajorExpoSeg(this@UserProductListInCountryActivity*//*,it.metaData.items*//*)
                                    activityUserProductListInCountryBinding.adapterUserAttractionManu= AdapterUserAttractionManu(this@UserProductListInCountryActivity*//*,it.metaData.items*//*)
                                    activityUserProductListInCountryBinding.userAgricultralMenuAdapter= UserAgricultralMenuAdapter(this@UserProductListInCountryActivity*//*,it.metaData.items*//*)
*/
                                }
                            }


                        }catch (_:Exception){

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }
}