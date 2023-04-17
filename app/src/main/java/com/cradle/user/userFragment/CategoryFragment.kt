package com.cradle.user.userFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.databinding.FragmentCategoryBinding
import com.cradle.model.CategoryListItem
import com.cradle.model.category.CategoryItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.CategoryAdapter
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.WishListActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class CategoryFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragmentCategoryBinding
    var categoryList: List<CategoryListItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = FragmentCategoryBinding.inflate(inflater)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v = mBinding.root
        mBinding.setVariable(BR.onUserCategoryClick, this)
        findId()

        return v
    }

    override fun onResume() {
        super.onResume()
        if( SharaGoPref.getInstance(requireActivity()).getToolBarInCate("Yes").equals("Yes")){
            mBinding.rlCusToolbar.visibility=View.VISIBLE
            secToolBar()
        }else{
            mBinding.rlCusToolbar.visibility=View.GONE
        }

    }

    private fun findId() {
         AnalyticsUtils.analyticReport(requireActivity(),MyConstants.CategoriesScreen)

        apiHit()
        apiResult()
        mBinding.ivOpenCountry.setOnClickListener {
         //   startActivity(Intent(requireActivity(), CountryActivity::class.java))
        }
        mBinding.ivCartScreen.setOnClickListener {

            if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), UserAddToCartActivity::class.java))

            else
                startActivity(Intent(requireActivity(), UserLoginActivity::class.java))

        }
        mBinding.ivWishList.setOnClickListener {
            if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), WishListActivity::class.java))
            else
                startActivity(Intent(requireActivity(), UserLoginActivity::class.java))
        }
    }
    private fun apiHit(){
        if (MyHelper.isNetworkConnected(requireActivity())) {
         //   LoaderFragment.showLoader(requireActivity().supportFragmentManager)
            mViewModel.categoryParam()
            mViewModel.getCardDetailsByIdParam(
                SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),
                SharaGoPref.getInstance(requireActivity()).getCartId("").toString())

        } else requireActivity().showToast(getString(R.string.no_internet_connection))
    }

    private fun apiResult(){

        mViewModel.lCategory.observe(requireActivity()){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    //   mainBinding.shimmerFrameLayout.stopShimmer()
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    mBinding.reUserCateList.visibility=View.VISIBLE
                    it.data?.let {
                        LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                        try {
                            CoroutineScope(Dispatchers.IO).async {
                                async {
                                    Log.e("catList",it.categoryList.toString())

                                    setCategoryRecyler(it.categoryList)

                                }

                            }


                        }catch (_:Exception){

                        }

                    }
                }
                is ExceptionHandler.Error->{
                  //  Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
        resultCount()
    }
    private fun resultCount(){
       val cartID= SharaGoPref.getInstance(requireActivity()).getCartId("").toString()
        if(!cartID.equals("null")){
            mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),SharaGoPref.getInstance(requireActivity()).getCartId("").toString())

            //user Server Product List result
            mViewModel.lGetCardDetailsParam.observe(requireActivity()){
                    it->
                when(it){
                    is ExceptionHandler.Loading->{}
                    is ExceptionHandler.Success->{
                        it.data?.let {

                            if(it.cartSize!!.isNotEmpty()){
                                mBinding.rlCartCount.visibility=View.VISIBLE
                                mBinding.tvCartCount.text=it.cartSize
                            }else{
                                mBinding.rlCartCount.visibility=View.VISIBLE
                                mBinding.tvCartCount.text="0"
                            }
                            // activityUserAddtocartBinding.userAddToCartAdapter= UserAddToCartAdapter(this/*,it.metaData!!.items*/)

                        }
                    }
                    is ExceptionHandler.Error->{
                        //  Utility.toastMessage(requireActivity(),it.errorMessage)
                    }
                }
            }
        }else{
            mBinding.rlCartCount.visibility=View.VISIBLE
            mBinding.tvCartCount.text="0"
        }

    }

    private fun setCategoryRecyler(categoryList: List<CategoryItem?>?) {
        mBinding.categoryAdapter= CategoryAdapter(requireActivity(),categoryList)
    }
    private fun secToolBar(){
        mBinding.tvUsertoolbartitle.text=getString(R.string.category)
        val flag=SharaGoPref.getInstance(requireActivity()).getCountryFlag("").toString()
       mBinding.tvCountryName.text = SharaGoPref.getInstance(requireActivity()).getCountry("").toString()

        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)

        mBinding.rlCusToolbar.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

    }


    override fun onClick(p0: View?) {

    }
}