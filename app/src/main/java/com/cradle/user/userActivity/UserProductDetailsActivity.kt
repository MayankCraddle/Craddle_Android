package com.cradle.user.userActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.cradle.repository.QuoteRepository
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.ActivityUserProductDetailsBinding
import com.cradle.firebasechat.activity.ChatActivity
import com.cradle.model.ProductListItem
import com.cradle.model.ProductListItem1
import com.cradle.model.VariantsItem
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.*
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_product_details.*
import kotlinx.android.synthetic.main.activity_user_product_details.iv_wishlist
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import org.json.JSONObject
import kotlin.math.roundToInt


class UserProductDetailsActivity : AppCompatActivity(), View.OnClickListener,
    SimilarProductByDetailsAdapter.TextState, OnSaleAdapter.TextState,SizeChartAdapter.ClickNowSize {
    private lateinit var response: QuoteRepository
    var data: ProductListItem1? = null
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserProductDetailsBinding
    private var mpref: SharaGoPref? = null
    private var token: String? = null
    private var shareUrl: String? = null
    private var productName: String? = null
    private var vendorID: String? = null
    private var vendorEncryptedId: String? = null
    private var vendorEmailId: String? = null
    private var vendorfcmKey: String? = null
    private var vendorfirstName: String? = null

    lateinit var itemId: String
    lateinit var productId: String
    lateinit var orderable: String
    private var selectVarient: String?=null
    var stock: Int = 0
    var quantity: Int = 0
    var minQuantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiInitialise()
    }

    private fun uiInitialise() {

        response = (application as ApplicationClass).repository
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_product_details)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.productDetails = mViewModel
        mBinding.setVariable(BR.onButtonClick, this)
        mpref = SharaGoPref.getInstance(this)
        AnalyticsUtils.analyticReport(this,MyConstants.ProductDetailScreen)
        itemId = intent.getStringExtra("item_id")!!
        productId = intent.getStringExtra("product_id")!!
        Log.e("product_id",productId)
        // mBinding.ivBack.visibility=View.VISIBLE
        tv_usertoolbartitle.text = getString(R.string.product_details)
        rlCustoolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        mBinding.ivBackDetails.setOnClickListener {
            finish()
        }
        rl_writeReview.setOnClickListener(this)
        card_open_vendor_details.setOnClickListener(this)
        rl_open_cart.setOnClickListener {
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(this, UserAddToCartActivity::class.java))
            else
                startActivity(Intent(this, UserLoginActivity::class.java))
        }
        rlViewReview.setOnClickListener {
            startActivity(
                Intent(this, ReviewActivity::class.java).putExtra("item_id", itemId).putExtra("sectionId","1").putExtra("subject","ddnd"))
        }

        if (MyHelper.isNetworkConnected(this)) {
          /*  apiHit()
            getApiResult()
            statusBarColourChange()*/
        } else showToast(getString(R.string.no_internet_connection))

        rlOpenEnquiry.setOnClickListener {
            AnalyticsUtils.analyticReport(this,MyConstants.OnClickProductInquire)
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                try {

                    if(vendorEncryptedId!!.isNotEmpty()){

                        startActivity(Intent(this, ChatActivity::class.java).putExtra("id",vendorEncryptedId).putExtra("email",vendorEmailId).putExtra("fcmkey",vendorfcmKey).putExtra("vendor_name",vendorfirstName).putExtra("Product_name",productName))
                    }
                }catch (e:Exception){
                    showToast("Not found Vendor ID!")
                }

            }
            else{
                startActivity(Intent(this, UserLoginActivity::class.java))
        }


        }

    }

    private fun setVarientRecycler(variants: ArrayList<VariantsItem?>?) {
        mBinding.sizeChartAdapter= SizeChartAdapter(this,variants,this)
    }

    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
        rl_add_to_cart_click.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }


    private fun apiHit() {

        val userId = SharaGoPref.getInstance(this).getUSERID("")
        Log.e("productID","productID"+productId+"userID"+userId.toString())
        mViewModel.usergetProductDetailParam(productId, userId.toString())
        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
            token = SharaGoPref.getInstance(this).getLoginToken("")
            mViewModel.getCardDetailsByIdParam(
                SharaGoPref.getInstance(this).getLoginToken("").toString(),
                SharaGoPref.getInstance(this).getCartId("").toString()

            )
            cartDetailsAPi()
        //    mViewModel.getCardDetailsUser(token!!)

        } else {

        }

        Log.e("itemId", itemId)
        Log.e("userId", userId.toString())

        mViewModel.userSimilarProductsParam(itemId)
        mViewModel.userOnSaleProductsParam(itemId)
        mViewModel.otherProductsSoldByVendorParam(itemId)
        mViewModel.newArrivalsParam(SharaGoPref.getInstance(this).getCountry("").toString())
        mViewModel.userTopDealsParam(SharaGoPref.getInstance(this).getCountry("").toString())
    }

    private var cartResponse: UserCartItem? = null
    private var wishListItem: Boolean = false

    @SuppressLint("SetTextI18n")
    private fun getApiResult() {
        mViewModel.lmLusergetProductDetailParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {

                }

                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("productDetails",it.toString())

                        vendorEncryptedId=it.vendorEncryptedId
                        orderable=it.orderable!!
                      shareUrl=it.shareUrl!!
                        vendorfcmKey=it.fcmKey
                        vendorEmailId=it.emailMobile
                        vendorfirstName=it.vendorMetaData!!.firstName+" "+it.vendorMetaData.lastName
                        itemId = it.itemId!!
                        stock = it.stock!!
                        mBinding.tvProductDis.text = it.metaData!!.description
                        val image = it.metaData.images
                        wishListItem = it.wishlisted!!
                        if (wishListItem) {
                            iv_wishlist.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.fill_heart
                                )
                            )
                        }
                        else {
                            iv_wishlist.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.wishlist
                                )
                            )
                        }
                        val adapter = SliderProductDetail(
                            this,
                            image,
                            "image",
                            1,
                            itemId,
                            getString(R.string.home)
                        )
                        //  adapter.setItem(imageList)
                        photos_viewpager.adapter = adapter

                        TabLayoutMediator(tab_layout, photos_viewpager) { tab, position ->
                        }.attach()
                        if (image!!.isNotEmpty()) {
                            val imagePath = image[0]
                            Glide.with(this)
                                .load(MyConstants.file_Base_URL + imagePath)
                                .into(mBinding.ivProductImage)

                        }
                        if (it.vendorMetaData!!.image != null) {
                            Glide.with(this)
                                .load(MyConstants.file_Base_URL + it.vendorMetaData.image)
                                .into(mBinding.ivVendorImage)
                        }



                        vendorID = it.vendorId
                        mBinding.tvVendorName.text = it.vendorName

                        mBinding.tvProductName.text = it.name
                        productName = it.name
                        mBinding.tvTotalRating.text = it.rating.toString()
                        mBinding.tvAddressEmailMobile.text =
                            "Address: " + it.vendorMetaData.streetAddress + "," + it.vendorMetaData.landmark + "," + it.vendorMetaData.state + "," + it.vendorMetaData.country + " | " + "Email :" + it.emailMobile
                        mBinding.rBarProductDetails.rating = it.rating!!

                        mBinding.tvDisPrice.text = getString(R.string.currency) + "" + it.discountedPrice.toString()
                        mBinding.tvProductWeight.text = it.metaData.productWeight + " Kg"

                        if((it.discountValue.toString())!="0.0"){
                            mBinding.tvPercentOff.text = it.discountValue.toString() + "% off"
                            mBinding.tvActualPrice.text = getString(R.string.currency) + "" + it.actualPrice.toString()

                            mBinding.tvActualPrice.paintFlags =
                                mBinding.tvActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                        }

                        mBinding.tvVendorName.text = it.vendorName
                        mBinding.tvVendorRating.text = it.rating.toString()
                        val rating = it.productRatingDetail
                        //Set Maximum rating in progressbar
                        mBinding.progressBar5.max = it.rating.toInt()
                        mBinding.progressBar4.max = it.rating.toInt()
                        mBinding.progressBar3.max = it.rating.toInt()
                        mBinding.progressBar2.max = it.rating.toInt()
                        mBinding.progressBar1.max = it.rating.toInt()
                        //Divided Total rating to particular rating
                        try {
                            val num5 = rating!!.jsonMember5!! % it.rating
                            val num4 = rating.jsonMember4!! % it.rating
                            val num3 = rating.jsonMember3!! % it.rating
                            val num2 = rating.jsonMember2!! % it.rating
                            val num1 = rating.jsonMember1!! % it.rating
                            //Set Progress rating in progress bar
                            mBinding.progressBar5.progress = num5.roundToInt()
                            mBinding.progressBar4.progress = num4.roundToInt()
                            mBinding.progressBar3.progress = num3.roundToInt()
                            mBinding.progressBar2.progress = num2.roundToInt()
                            mBinding.progressBar1.progress = num1.roundToInt()
                        } catch (_: Exception) {

                        }



                        mBinding.tvRating1.text = "(" + rating!!.jsonMember1.toString() + ")"
                        mBinding.tvRating2.text = "(" + rating.jsonMember2.toString() + ")"
                        mBinding.tvRating3.text = "(" + rating.jsonMember3.toString() + ")"
                        mBinding.tvRating4.text = "(" + rating.jsonMember4.toString() + ")"
                        mBinding.tvRating5.text = "(" + rating.jsonMember5.toString() + ")"

                        mBinding.ivFacebook.visibility = View.GONE
                        mBinding.ivTwitter.visibility = View.GONE
                        mBinding.ivInsta.visibility = View.GONE


                        mBinding.tvVendorName.text =
                            it.vendorMetaData.firstName + " " + it.vendorMetaData.lastName
                        mBinding.tvReturnPolicy.text = it.returnPolicy
                        if(it.variants!!.isNotEmpty()){
                            if(it.variants.size>=1){
                                if(it.variants.size==1){
                                    selectVarient=it.variants.get(0)!!.variant
                                }
                                setVarientRecycler(it.variants as ArrayList<VariantsItem?>)
                            }else{
                                selectVarient="null"

                            }

                        }


                        Log.e("productDetails", it.toString())

                    }

                }
                is ExceptionHandler.Error -> {
                //    showToast(it.toString())
                }
            }
        }



        mViewModel.lSimilarProductsParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        if (it.productList!!.isNotEmpty()) {
                            ll_similar_pro.visibility = View.VISIBLE
                            mBinding.similarProductAdatper =
                                SimilarProductByDetailsAdapter(this, it.productList, this)
                        } else
                            ll_similar_pro.visibility = View.GONE
                    }
                }
                is ExceptionHandler.Error -> {
                  //  showToast(it.toString())

                }
            }
        }
        mViewModel.lOnSaleProductsParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("onSale", it.toString())
                        if (it.productList!!.isNotEmpty()) {
                            ll_onSale_pro.visibility = View.VISIBLE
                            mBinding.onSaleProductAdatper =
                                OnSaleAdapter(this, it.productList, this)
                        } else
                            ll_onSale_pro.visibility = View.GONE
                    }
                }
                is ExceptionHandler.Error -> {
                    // showToast(it.toString())

                }
            }
        }
        mViewModel.lotherProductsSoldByVendorParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        if (it.productList!!.isNotEmpty()) {
                            ll_vendor_other_pro.visibility = View.VISIBLE
                            mBinding.vendorsOtherProductAdatper =
                                OnSaleAdapter(this, it.productList, this)
                        } else
                            ll_vendor_other_pro.visibility = View.GONE
                    }
                }
                is ExceptionHandler.Error -> {
                    //  showToast(it.toString())

                }
            }
        }
        mViewModel.lnewArrivalsParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        if (it.productList!!.isNotEmpty()) {
                            ll_newArrivals_pro.visibility = View.VISIBLE
                            mBinding.newArrivalAdapter = OnSaleAdapter(this, it.productList, this)
                        } else {
                            ll_newArrivals_pro.visibility = View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    //    showToast(it.toString())

                }
            }
        }
        mViewModel.lUserTopDeals.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        if (it.productList!!.isNotEmpty()) {
                            ll_daily_need_pro.visibility = View.VISIBLE
                            mBinding.topDealAdapter = OnSaleAdapter(this, it.productList, this)
                        } else {
                            ll_daily_need_pro.visibility = View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    //  showToast(it.toString())

                }
            }
        }



        mViewModel.lremoveProductFromWishlistParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        val jsonObject = JSONObject(it.toString())
                        Utility.toastMessage(this, jsonObject.optString("message"))
                        apiHit()
                    }
                }
                is ExceptionHandler.Error -> {
                    // Utility.toastMessage(this,it.errorMessage)
                }
            }
        }



    }


    private fun cartDetailsAPi(){
        mViewModel.getCardDetailsUser(token!!)
        mViewModel.lCartDetailsUser.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                        cartResponse = it
                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error -> {
                    Log.e("Error", it.errorMessage.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (MyHelper.isNetworkConnected(this)) {
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
                token = SharaGoPref.getInstance(this).getLoginToken("")
                cartDetailsAPi()
              //  mViewModel.getCardDetailsUser(token!!)

               val cartId= SharaGoPref.getInstance(this).getCartId("").toString()

                if(!cartId.equals("null")){
                    mViewModel.getCardDetailsByIdParam(
                        SharaGoPref.getInstance(this).getLoginToken("").toString(),
                        SharaGoPref.getInstance(this).getCartId("").toString()
                    )
                    //user Server Product List result
                    mViewModel.lGetCardDetailsParam.observe(this) { it ->
                        when (it) {
                            is ExceptionHandler.Loading -> {}
                            is ExceptionHandler.Success -> {
                                it.data?.let {
                                    cartResponse = it

                                    if (it.cartSize!!.isNotEmpty()) {
                                        mBinding.tvCartCount.text = it.cartSize

                                    }else{
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
                }
                else{
                    mBinding.tvCartCount.text = "0"
                }


            }
            apiHit()
            getApiResult()
            statusBarColourChange()

        }

    }


    private var vendorDetails: Boolean = false
    private var ratingReviews: Boolean = false
    private var privacyPolicy: Boolean = false
    private var Description: Boolean = false
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlVendorDetails -> {
                if (!vendorDetails) {
                    vendorDetails = true
                    card_open_vendor_details.visibility = View.GONE
                    iv_vendor_details_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.up_arrow
                        )
                    )
                } else {
                    vendorDetails = false
                    card_open_vendor_details.visibility = View.VISIBLE
                    iv_vendor_details_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.down_arrow
                        )
                    )
                }
            }
            R.id.rlDescription -> {
                if (!Description) {
                    Description = true
                    tv_product_dis.visibility = View.GONE
                    iv_dis_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.up_arrow
                        )
                    )
                } else {
                    Description = false
                    tv_product_dis.visibility = View.VISIBLE
                    iv_dis_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.down_arrow
                        )
                    )
                }
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.card_open_vendor_details -> {
                val i = Intent(this, UserVendorDetailActivity::class.java)
                i.putExtra("vendor_id", vendorID)
                startActivity(i)
            }
            R.id.iv_wishlist -> {
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                    apiHitWishListAddDelete()
                else
                    showToast("Hey ,you need to login in order to add product to wishlist.")
                   // startActivity(Intent(this, UserLoginActivity::class.java))

            } R.id.iv_share -> {
           shareUrl(this,shareUrl)
            }
            R.id.rlPrivacyPolicy -> {
                if (!privacyPolicy) {
                    privacyPolicy = true
                    card_privacy_policy.visibility = View.GONE
                    iv_privacy_policy_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.up_arrow
                        )
                    )
                } else {
                    privacyPolicy = false
                    card_privacy_policy.visibility = View.VISIBLE
                    iv_privacy_policy_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.down_arrow
                        )
                    )
                }

            }
            R.id.rlRatingReview -> {
                if (!ratingReviews) {
                    ratingReviews = true
                    card_open_rating_review.visibility = View.GONE
                    iv_rating_review_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.up_arrow
                        )
                    )
                }
                else {
                    ratingReviews = false
                    card_open_rating_review.visibility = View.VISIBLE
                    iv_rating_review_open.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.down_arrow
                        )
                    )
                }

            }
            R.id.rl_add_to_cart_click -> {
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                    if (MyHelper.isNetworkConnected(this)) {
                        try {
                            if(selectVarient!!.isNotEmpty())
                                if(orderable.equals("true")){
                                    addToCartProduct()
                                }else{
                                    showToast(getString(R.string.you_do_not))
                                }

                            else
                                showToast("Please Select your varient")
                        }catch (e:Exception){
                            showToast("Please Select your varient")
                        }

                    } else showToast(getString(R.string.no_internet_connection))

                else
                    startActivity(Intent(this, UserLoginActivity::class.java))


            }
            R.id.rl_writeReview -> {
                startActivity(
                    Intent(this, UserWriteReviewActivity::class.java).putExtra(
                        "item_id",
                        itemId
                    )
                )
            }
        }
    }

    private fun apiHitWishListAddDelete() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("itemId", itemId)
        mViewModel.addProductToWishlistParam(token!!, jsonObject)

        mViewModel.lAddProductToWishlistParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {

                    val jsonObject = JSONObject(it.data.toString())
                    Utility.toastMessage(this, jsonObject.optString("message"))
                    //  apiHit()
                }
                is ExceptionHandler.Error -> {
                    showToast(it.errorMessage)
                }
            }
        }
     /*   if (wishListItem) {
            mViewModel.removeProductFromWishlistParam(token!!, jsonObject)
        }
        else {


        }*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun addToCartProduct() {
        if (mpref!!.getLoginToken("")!!.isNotEmpty()) {
            //for logged in user
            if (cartResponse!!.metaData == null || cartResponse!!.metaData!!.items!!.isEmpty()) {
                val item = JsonArray()
                /* for (i in dataList!!) {*/
                val requestBody = JsonObject()
                requestBody.addProperty("itemId", itemId)
               // requestBody.addProperty("quantity", "1")
                requestBody.addProperty("quantity", minQuantity)
                requestBody.addProperty("variant", selectVarient)
                item.add(requestBody)
                //   }

                val jsonMain = JsonObject()
                val requestBodymetadata = JsonObject()
                requestBodymetadata.add("items", item)
                jsonMain.add("metaData", requestBodymetadata)

                try {
                    if (cartResponse!!.cartId!!.toString() == "null") {
                        saveCartApi(jsonMain, "")
                    } else {
                        saveCartApi(jsonMain, cartResponse!!.cartId.toString())
                    }
                } catch (e: Exception) {
                    saveCartApi(jsonMain, "")
                }


            }
            else {
                for (i in cartResponse!!.metaData!!.items!!) {
                    if (i?.itemId ==itemId){
                        quantity = i?.quantity!!
                    }
                }

                if (stock > quantity) {
                    var isAlready = false
                    val item = JsonArray()
                    val itemnew = JsonArray()
                    var count = 0
                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        if (i!!.itemId!! != itemId) {
                            if (count == 0) {
                                requestBody.addProperty("itemId", itemId)
                               // requestBody.addProperty("quantity", "1")
                                requestBody.addProperty("quantity", minQuantity)
                                requestBody.addProperty("variant", selectVarient)
                                item.add(requestBody)
                            }
                            count++

                        } else {
                            isAlready = true
                        }
                    }
                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        requestBody.addProperty("itemId", i!!.itemId)
                        requestBody.addProperty("variant", selectVarient)
                        if (isAlready) {
                            requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                        } else {
                            requestBody.addProperty("quantity", i.quantity)
                        }
                        item.add(requestBody)
                    }
                    if (isAlready) {
                        for (i in cartResponse!!.metaData!!.items!!) {
                            val requestBody = JsonObject()
                            requestBody.addProperty("itemId", i!!.itemId)
                            requestBody.addProperty("variant", selectVarient)
                            if (i.itemId!! == itemId) {
                                requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                            } else {
                                requestBody.addProperty("quantity", i.quantity)
                            }
                            itemnew.add(requestBody)
                        }
                    }
                    val jsonMain = JsonObject()
                    val requestBodymetadata = JsonObject()
                    requestBodymetadata.add("items", item)
                    jsonMain.add("metaData", requestBodymetadata)
                    if (isAlready) {
                        val jsonMain = JsonObject()
                        val requestBodymetadata = JsonObject()
                        requestBodymetadata.add("items", itemnew)
                        jsonMain.add("metaData", requestBodymetadata)
                        Log.e("jsonMain", jsonMain.toString())
                        saveCartApi(
                            jsonMain,
                            SharaGoPref.getInstance(this).getCartId("").toString()
                        )
                    } else {
                        saveCartApi(
                            jsonMain,
                            SharaGoPref.getInstance(this).getCartId("").toString()
                        )
                    }
                }
                else {
                    showToast("Stock Limit finish!")
                }
            }
        } else {

        }

    }

    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if (cartId == "") {
            mViewModel.getSaveCartParam(mpref!!.getLoginToken("")!!, jsonMain)

            mViewModel.lSaveCartParam.observe(this) {
                when (it) {
                    is ExceptionHandler.Loading -> {

                    }
                    is ExceptionHandler.Success -> {

                        it.data?.let {
                            cartDetailsAPi()
                          /*  mViewModel.getCardDetailsUser(
                                SharaGoPref.getInstance(this).getLoginToken("").toString()
                            )*/

                            Utility.toastMessage(this, getString(R.string.item_added))


                        }
                    }
                    is ExceptionHandler.Error -> {


                    }
                }
            }
        } else {

            mViewModel.getUpdateCartParam(mpref!!.getLoginToken("").toString(), cartId, jsonMain)
            Utility.toastMessage(this, getString(R.string.item_added))
            mViewModel.lUpdateCartParam.observe(this) {
                when (it) {
                    is ExceptionHandler.Loading -> {

                    }
                    is ExceptionHandler.Success -> {

                        it.data?.let {

                            mViewModel.getCardDetailsByIdParam(
                                SharaGoPref.getInstance(this).getLoginToken("").toString(),
                                SharaGoPref.getInstance(this).getCartId("").toString()
                            )

                            if (!mpref!!.getLoginToken("").equals("")) {
                               // mViewModel.getCardDetailsUser(token!!)

                                cartDetailsAPi()
                            }
                        }
                    }
                    is ExceptionHandler.Error -> {


                    }
                }
            }

        }
    }

    override fun textStateAction(data: ProductListItem1?, position: Int,itemId:String) {
       stock= data!!.stock!!
        if (mpref!!.getLoginToken("")!!.isNotEmpty()) {
            //for logged in user
            if (cartResponse!!.metaData == null || cartResponse!!.metaData!!.items!!.isEmpty()) {
                val item = JsonArray()
                /* for (i in dataList!!) {*/
                val requestBody = JsonObject()
                requestBody.addProperty("itemId", data!!.itemId)
                requestBody.addProperty("quantity", "1")
                item.add(requestBody)
                //   }

                val jsonMain = JsonObject()

                val requestBodymetadata = JsonObject()
                requestBodymetadata.add("items", item)
                jsonMain.add("metaData", requestBodymetadata)

                try {
                    if (cartResponse!!.cartId!!.toString() == "null") {
                        saveCartApi(jsonMain, "")
                    } else {
                        saveCartApi(jsonMain, cartResponse!!.cartId.toString())
                    }
                } catch (e: Exception) {
                    saveCartApi(jsonMain, "")
                }


            }
            else {
                for (i in cartResponse!!.metaData!!.items!!) {
                    if (i?.itemId ==itemId){
                        quantity = i?.quantity!!
                    }
                }

                if (stock > quantity){
                    var isAlready = false
                    val item = JsonArray()
                    val itemnew = JsonArray()
                    var count = 0
                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        if (i!!.itemId!! != data!!.itemId) {
                            if (count == 0) {
                                requestBody.addProperty("itemId", itemId)
                                requestBody.addProperty("quantity", "1")
                                item.add(requestBody)
                            }
                            count++

                        } else {
                            isAlready = true
                        }
                    }

                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        requestBody.addProperty("itemId", i!!.itemId)
                        if (isAlready) {
                            requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                        } else {
                            requestBody.addProperty("quantity", i.quantity)
                        }
                        item.add(requestBody)
                    }

                    if (isAlready) {
                        for (i in cartResponse!!.metaData!!.items!!) {
                            val requestBody = JsonObject()
                            requestBody.addProperty("itemId", i!!.itemId)
                            if (i.itemId!! == itemId) {
                                requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                            } else {
                                requestBody.addProperty("quantity", i.quantity)
                            }
                            itemnew.add(requestBody)
                        }
                    }
                    val jsonMain = JsonObject()

                    val requestBodymetadata = JsonObject()
                    requestBodymetadata.add("items", item)
                    jsonMain.add("metaData", requestBodymetadata)

                    if (isAlready) {
                        val jsonMain = JsonObject()
                        val requestBodymetadata = JsonObject()
                        requestBodymetadata.add("items", itemnew)
                        jsonMain.add("metaData", requestBodymetadata)
                        saveCartApi(jsonMain, SharaGoPref.getInstance(this).getCartId("").toString())
                    } else {
                        saveCartApi(jsonMain, SharaGoPref.getInstance(this).getCartId("").toString())
                    }
            }else{
                showToast("Stock Limit Finish!")
                }

            }

        }
    }

    override fun addProductToWishList(jsonObject: JsonObject) {

    }

    override fun addProductToCart(data: ProductListItem?, position: Int,itemId: String) {
        stock= data!!.stock!!
        minQuantity= data.variants!!.get(0)!!.minQuantity!!
        if (mpref!!.getLoginToken("")!!.isNotEmpty()) {
            //for logged in user
            if (cartResponse!!.metaData == null || cartResponse!!.metaData!!.items!!.isEmpty()) {
                val item = JsonArray()
                /* for (i in dataList!!) {*/
                val requestBody = JsonObject()
                requestBody.addProperty("itemId", data!!.itemId)
               // requestBody.addProperty("quantity", "1")
                requestBody.addProperty("quantity", minQuantity)
                item.add(requestBody)
                //   }

                val jsonMain = JsonObject()

                val requestBodymetadata = JsonObject()
                requestBodymetadata.add("items", item)
                jsonMain.add("metaData", requestBodymetadata)

                try {
                    if (cartResponse!!.cartId!!.toString() == "null") {
                        saveCartApi(jsonMain, "")
                    } else {
                        saveCartApi(jsonMain, cartResponse!!.cartId.toString())
                    }
                } catch (e: Exception) {
                    saveCartApi(jsonMain, "")
                }


            }
            else {
                for (i in cartResponse!!.metaData!!.items!!) {
                    if (i?.itemId ==itemId){
                        quantity = i?.quantity!!
                    }
                }

                if (stock > quantity){
                    var isAlready = false
                    val item = JsonArray()
                    val itemnew = JsonArray()
                    var count = 0
                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        if (i!!.itemId!! != data!!.itemId) {
                            if (count == 0) {
                                requestBody.addProperty("itemId", itemId)
                             //   requestBody.addProperty("quantity", "1")
                                requestBody.addProperty("quantity", minQuantity)
                                item.add(requestBody)
                            }
                            count++

                        } else {
                            isAlready = true
                        }
                    }

                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        requestBody.addProperty("itemId", i!!.itemId)
                        if (isAlready) {
                            requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                        } else {
                            requestBody.addProperty("quantity", i.quantity)
                        }
                        item.add(requestBody)
                    }

                    if (isAlready) {
                        for (i in cartResponse!!.metaData!!.items!!) {
                            val requestBody = JsonObject()
                            requestBody.addProperty("itemId", i!!.itemId)
                            if (i.itemId!! == itemId) {
                                requestBody.addProperty("quantity", i.quantity!!.toInt() + 1)
                            } else {
                                requestBody.addProperty("quantity", i.quantity)
                            }
                            itemnew.add(requestBody)
                        }
                    }
                    val jsonMain = JsonObject()

                    val requestBodymetadata = JsonObject()
                    requestBodymetadata.add("items", item)
                    jsonMain.add("metaData", requestBodymetadata)

                    if (isAlready) {
                        val jsonMain = JsonObject()
                        val requestBodymetadata = JsonObject()
                        requestBodymetadata.add("items", itemnew)
                        jsonMain.add("metaData", requestBodymetadata)
                        saveCartApi(jsonMain, SharaGoPref.getInstance(this).getCartId("").toString())
                    } else {
                        saveCartApi(jsonMain, SharaGoPref.getInstance(this).getCartId("").toString())
                    }
                }else{
                    showToast("Stock Limit Finish!")
                }

            }

        }
    }

    override fun varient(position: Int, data: VariantsItem) {
        itemId=""
        stock=0
        selectVarient=""
        try {
            mBinding.tvDisPrice.text = getString(R.string.currency) + "" + data.discountedPrice.toString()
            if((data.discountValue.toString())!="0.0"){
                mBinding.tvActualPrice.text = getString(R.string.currency) + "" + data.actualPrice.toString()
                mBinding.tvProductWeight.text = data.variant
                mBinding.tvPercentOff.text = data.discountValue.toString() + "% off"
            }
            selectVarient= data.variant!!

        }catch (e:Exception){
            selectVarient= ""
        }

        stock= data.stock!!
        itemId= data.itemId!!
        minQuantity= data.minQuantity!!

    }

    private fun shareUrl(context: Context, shareURl: String?) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, shareURl)
        context.startActivity(Intent.createChooser(i, "Share URL"))
    }


}
