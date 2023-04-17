package com.cradle.user.userActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityTradeBinding
import com.cradle.model.ProductListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.*
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_trade.photos_viewpager
import kotlinx.android.synthetic.main.activity_trade.tab_layout
import kotlinx.android.synthetic.main.activity_user_product_details.*

class TradeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityTradeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityTradeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        setViewPager()
     }
    private fun apiHit(){
        mViewModel.newArrivalsParam("itemId")
        mViewModel.userTopDealsParam(SharaGoPref.getInstance(this).getCountry("").toString())

    }
    private fun apiResult(){
        mViewModel.lnewArrivalsParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            ll_newArrivals_pro.visibility=View.VISIBLE
                          //  setNewArrivalsAdapter(it.productList!!)
                        }else{
                            ll_newArrivals_pro.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    showToast(it.toString())

                }
            }
        }
        mViewModel.lUserTopDeals.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            ll_daily_need_pro.visibility=View.VISIBLE
                         //   setTopDealsAdapter(it.productList)
                        }else{
                            ll_daily_need_pro.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    showToast(it.toString())

                }
            }
        }
    }
    private fun setNewArrivalsAdapter(newArrivalList: List<ProductListItem?>) {
     //   mBinding.newArrivalAdapter= NewArrivalsAdapter(this/*,newArrivalList*/)

    }
    private fun setTopDealsAdapter(newArrivalList: List<ProductListItem?>) {
      //  mBinding.topDealsAdapter= TopDealsAdapter(this/*,newArrivalList*/)

    }

    override fun onClick(p0: View?) {

    }

    var bannerMarketPlacemediaList: ArrayList<Any?>? = null
    private fun setViewPager(){
        bannerMarketPlacemediaList= ArrayList()
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        val adapter = TradeAdapter(
            this,
            bannerMarketPlacemediaList
        )
        //  adapter.setItem(imageList)
        photos_viewpager.adapter = adapter

        TabLayoutMediator(tab_layout, photos_viewpager) { tab, position ->
        }.attach()
    }
}
