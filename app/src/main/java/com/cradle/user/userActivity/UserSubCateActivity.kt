package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.ActivityUserSubCateBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.UserSubCategoryListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterUserSubCate
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_sub_cate.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class UserSubCateActivity:BaseActivity(), ItemClickListner, View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserSubCateBinding
    var userSubCategoryListItem: List<UserSubCategoryListItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiInitialise()
    }

    private fun uiInitialise() {
        val response = (application as ApplicationClass).repository
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_sub_cate)

       // mBinding = ActivityUserSubCateBinding.inflate()
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.viewModel=mViewModel
        mBinding.setVariable(BR.onUserSubCateClick,this)
        showLoader()
        mViewModel.getSubCateList(intent.getStringExtra("cat_id").toString())
        getResult()
        setToolBar()
        onSearchCountry()
        statusBarColourChange()
        apiHit()
    }

    private fun apiHit() {
        mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

    }

    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }
    private fun setToolBar(){
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.tvUsertoolbartitle.text=getString(R.string.subcate_list)


        mBinding.ivOpenCountry.setOnClickListener { startActivity(Intent(this,CountryActivity::class.java) )}
    }

    override fun onResume() {
        super.onResume()
        val flag=SharaGoPref.getInstance(this).getCountryFlag("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)

        mBinding.rlCusToolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }

    private fun getResult(){
        //user Local Product List result
        mViewModel.lUserSubCateReq.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    //   mainBinding.shimmerFrameLayout.stopShimmer()
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    mBinding.recyclerSubCate.visibility=View.VISIBLE
                    it.data?.let {
                        dismissLoader()
                        try {
                            CoroutineScope(Dispatchers.IO).async {
                                async {
                                    mBinding.userSubCateAdapter= AdapterUserSubCate(this@UserSubCateActivity,it.categoryList)
                                    userSubCategoryListItem=it.categoryList
                                }
                            }


                        }catch (_:Exception){

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lGetCardDetailsParam.observe(this){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {

                        if(it.metaData!!.items!!.isNotEmpty()){
                            rl_cart_count.visibility=View.VISIBLE
                            tv_cart_count.text=it.cartSize


                        }else{
                            rl_cart_count.visibility=View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    rl_cart_count.visibility=View.GONE
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun onClickItem(position: Int, requestcode: Int) {

    }

    override fun onClick(p0: View?) {
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
        var filterdNames: ArrayList<UserSubCategoryListItem> = ArrayList()

        //looping through existing elements
        for (s in this.userSubCategoryListItem!!) {
            //if the existing elements contains the search input
            if (s!!.name!!.lowercase().contains(text.lowercase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = mBinding.userSubCateAdapter!!.filterList(filterdNames)

    }
}