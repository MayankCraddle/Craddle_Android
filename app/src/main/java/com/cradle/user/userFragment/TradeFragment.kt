package com.cradle.user.userFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.ActivityTradeBinding
import com.cradle.model.trade.ProductListItemByTrade
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.NewArrivalsAdapter
import com.cradle.user.adapters.OnSaleAdapterByTrade
import com.cradle.user.adapters.TopDealsAdapter
import com.cradle.user.adapters.TradeAdapter
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.ViewAllByTradeActivity
import com.cradle.user.userActivity.WishListActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_trade.view.*
import kotlinx.android.synthetic.main.activity_user_product_details.*
import java.util.*
import kotlin.collections.ArrayList

class TradeFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityTradeBinding

    //...
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = ActivityTradeBinding.inflate(inflater)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v= mBinding.root
        mBinding.setVariable(BR.onAccount,this)

        statusbarColourChange()
        findId()
        return v
    }

    private fun findId() {
        AnalyticsUtils.analyticReport(requireActivity(),MyConstants.MarketPlaceScreen)
        statusBarColourChange()
        if (isAdded()&&activity != null) {
            if (MyHelper.isNetworkConnected(requireActivity())) {
                setViewPager()
                apiHit()
                apiResult()
                click()

                mBinding.ivWishlistScreen.setOnClickListener {
                    if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                        startActivity(Intent(requireActivity(), WishListActivity::class.java))
                    else
                        startActivity(Intent(requireActivity(), UserLoginActivity::class.java))


                }
                mBinding.rlOpenCart.setOnClickListener {
                    if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                        startActivity(Intent(requireActivity(), UserAddToCartActivity::class.java))
                    else
                        startActivity(Intent(requireActivity(), UserLoginActivity::class.java))


                }


            } else requireActivity().showToast(getString(R.string.no_internet_connection))
        }
    }
    private fun click(){
        v.tv_view_all_new_arrivals.setOnClickListener {
            startActivity(Intent(requireActivity(), ViewAllByTradeActivity::class.java).putExtra("list","NewArrivals"))
        }
        v.tv_view_all__on_sale.setOnClickListener {
            startActivity(Intent(requireActivity(), ViewAllByTradeActivity::class.java).putExtra("list","OnSale"))
        }
        v.tv_view_all_top_deals.setOnClickListener {
            startActivity(Intent(requireActivity(), ViewAllByTradeActivity::class.java).putExtra("list","TopDeals"))
        }
    }

    private fun apiHit(){
        val country=SharaGoPref.getInstance(requireActivity()).getCountry("").toString()
        mViewModel.newArrivalsByTradParam(country,"","","1","10")
        mViewModel.topDealsByCountryParam(country,"","","1","10")
        mViewModel.onSaleByCountryParam(country,"","","1","10")

    }
    private fun statusbarColourChange() {

    }
    var bannerMarketPlacemediaList: ArrayList<Any?>? = null
    private fun setViewPager(){
        bannerMarketPlacemediaList= ArrayList()
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        val adapter = TradeAdapter(
            requireActivity(),
            bannerMarketPlacemediaList
        )
        //  adapter.setItem(imageList)
        v.photos_viewpager.adapter = adapter

        TabLayoutMediator(v.tab_layout, v.photos_viewpager) { tab, position ->
        }.attach()


        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == 4 - 1) {
                currentPage = 0
            }
            v.photos_viewpager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }


    override fun onClick(p0: View?) {


    }

    private fun apiResult(){
        mViewModel.lnewArrivalsByTrade.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            setNewArrivalsAdapter(it.productList)
                        }else{
                         //   ll_newArrivals_pro.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    requireActivity().showToast(it.toString())

                }
            }
        }
        mViewModel.ltopDealsByCountry.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            setTopDealsAdapter(it.productList)
                        }else{
                         //   ll_daily_need_pro.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    requireActivity().showToast(it.toString())

                }
            }
        }
        mViewModel.lonSaleByCountryReq.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            setOnSaleAdapter(it.productList)
                        }else{
                        //    ll_daily_need_pro.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    requireActivity().showToast(it.toString())

                }
            }
        }
    }

    private fun setNewArrivalsAdapter(newArrivalList: List<ProductListItemByTrade?>) {
        mBinding.newArrivalAdapter= NewArrivalsAdapter(requireActivity(),newArrivalList)

    }
    private fun setTopDealsAdapter(newArrivalList: List<ProductListItemByTrade?>?) {
        mBinding.topDealsAdapter= TopDealsAdapter(requireActivity(),newArrivalList)

    }private fun setOnSaleAdapter(newArrivalList: List<ProductListItemByTrade?>?) {
        mBinding.onSaleAdapterByTrade= OnSaleAdapterByTrade(requireActivity(),newArrivalList)

    }

    private fun statusBarColourChange(){


        if(SharaGoPref.getInstance(requireActivity()).getCountryFlag("")!==null){
            val flag=SharaGoPref.getInstance(requireActivity()).getCountryFlag("").toString()
            Glide.with(requireActivity()).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountryNew)

            mBinding.tvCountryName.text = SharaGoPref.getInstance(requireActivity()).getCountry("")
                .toString()

            mBinding.rlCusToolbarNew.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

        }else{

        }
        /*if(mpref!!.getShowList("").equals(getString(R.string.products))){
          *//*tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()+"'s Products"*//*
            tv_country_name.text = "Products"
        }else{
            tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()
        }*/

    }

    override fun onResume() {
        super.onResume()
        if (MyHelper.isNetworkConnected(requireActivity())) {
            if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty()) {
                val  token = SharaGoPref.getInstance(requireActivity()).getLoginToken("")
                cartDetailsAPi(token!!)
                //  mViewModel.getCardDetailsUser(token!!)

                val cartId = SharaGoPref.getInstance(requireActivity()).getCartId("").toString()

                if (!cartId.equals("null")) {
                    mViewModel.getCardDetailsByIdParam(
                        SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),
                        SharaGoPref.getInstance(requireActivity()).getCartId("").toString()
                    )
                    //user Server Product List result
                    mViewModel.lGetCardDetailsParam.observe(this) { it ->
                        when (it) {
                            is ExceptionHandler.Loading -> {}
                            is ExceptionHandler.Success -> {
                                it.data?.let {
                                    //  cartResponse = it

                                    if (it.cartSize!!.isNotEmpty()) {
                                        mBinding.tvCartCount.text = it.cartSize

                                    } else {
                                        mBinding.tvCartCount.text = "0"
                                    }
                                    // activityUserAddtocartBinding.userAddToCartAdapter= UserAddToCartAdapter(this/*,it.metaData!!.items*/)

                                }
                            }
                            is ExceptionHandler.Error -> {
                                //Utility.toastMessage(this,it.errorMessage)
                            }
                        }
                    }
                } else {
                    mBinding.tvCartCount.text = "0"
                }


            }
        }
    }
    private fun cartDetailsAPi(token:String){
        mViewModel.getCardDetailsUser(token!!)
        mViewModel.lCartDetailsUser.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        //   cartResponse = it
                        SharaGoPref.getInstance(requireActivity()).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error -> {
                    Log.e("Error", it.errorMessage.toString())
                }
            }
        }
    }



}
