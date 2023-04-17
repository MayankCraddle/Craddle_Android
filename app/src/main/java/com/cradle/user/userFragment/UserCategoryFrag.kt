package com.cradle.user.userFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.FragmentUserCategoryBinding
import com.cradle.model.CategoryListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterUserCate
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.view.*
import kotlinx.android.synthetic.main.fragment_user_home.*
import kotlinx.android.synthetic.main.fragment_user_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class UserCategoryFrag : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragmentUserCategoryBinding
    var categoryList: List<CategoryListItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = FragmentUserCategoryBinding.inflate(inflater)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v = mBinding.root
        mBinding.setVariable(BR.onUserCategoryClick, this)
        findId()

        return v
    }
    var screen:String? = null
    private fun findId() {
        screen=requireActivity().intent.getStringExtra("screen").toString()

        mBinding.tvUsertoolbartitle.visibility=View.VISIBLE
        mBinding.tvUsertoolbartitle.text=getText(R.string.category_list)

        if (MyHelper.isNetworkConnected(requireActivity())) {
            LoaderFragment.showLoader(requireActivity().supportFragmentManager)
            mViewModel.getCategoryList()
            mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),SharaGoPref.getInstance(requireActivity()).getCartId("").toString())

            resultCount()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

        mBinding.ivOpenCountry.setOnClickListener {
            startActivity(Intent(requireActivity(),CountryActivity::class.java))
        }
        getResult()
        onSearchCountry()
        if (screen=="product")
           v.rl_cus_toolbar.visibility=View.GONE
    }

    private fun getResult(){
        //user Local Product List result
        mViewModel.lUserCateReq.observe(requireActivity()){
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
                                    val parentCatList=ArrayList<CategoryListItem>()
                                    for ( i in 0 until it.categoryList!!.size)  {
                                        if((it.categoryList[i]!!.parentCategory!!).equals("0")){
                                            parentCatList.add(it.categoryList[i]!!)
                                        }
                                    }
                                    Log.e("parent",parentCatList.toString())
                                    mBinding.userCateAdapter= AdapterUserCate(requireActivity(),parentCatList)
                                }
                                categoryList=it.categoryList
                            }


                        }catch (e:Exception){

                        }

                    }
                }
                is ExceptionHandler.Error->{
                  //  Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
        mViewModel.lGetCardDetailsParam.observe(requireActivity()){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {

                        if(it.metaData!!.items!!.isNotEmpty()){
                            v.rl_cart_count.visibility=View.VISIBLE
                            v.tv_cart_count.text=it.metaData.items!!.size.toString()


                        }else{
                            v.rl_cart_count.visibility=View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    v.rl_cart_count.visibility=View.GONE
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }

    }

    private fun resultCount(){
        mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),SharaGoPref.getInstance(requireActivity()).getCartId("").toString())

        //user Server Product List result
        mViewModel.lGetCardDetailsParam.observe(requireActivity()){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {

                        if(it.metaData!!.items!!.size>0){
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
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_cart_screen->{
                startActivity(Intent(requireActivity(), UserAddToCartActivity::class.java))
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val flag=SharaGoPref.getInstance(requireActivity()).getCountryFlag("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)
        mBinding.rlCusToolbar.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)
        resultCount()

    }
    //Country list api
    fun onSearchCountry(){
        mBinding.countrySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })
    }
    private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<CategoryListItem> = ArrayList()

        //looping through existing elements
        for (s in this.categoryList!!) {
            //if the existing elements contains the search input
            if (s!!.name!!.lowercase().contains(text.lowercase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = mBinding.userCateAdapter!!.filterList(filterdNames)

    }
}