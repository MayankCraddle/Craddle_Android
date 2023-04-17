package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserVendorDetailBinding
import com.cradle.firebasechat.activity.ChatActivity
import com.cradle.intarfaces.AddItemToCart
import com.cradle.model.NewProductListOfVendorListItem
import com.cradle.model.ProductListItem
import com.cradle.model.RatingListItem
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.*
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_vendor_detail.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*

class UserVendorDetailActivity: BaseActivity(), View.OnClickListener ,OnSaleAdapter.TextState,AdapterNewProductOfVendor.TextState,AddItemToCart{

    private lateinit var mBinding: ActivityUserVendorDetailBinding
    private lateinit var mViewModel: MainViewModel
    lateinit var vendorId:String
    lateinit var firstName:String
    lateinit var token:String
    private var mpref: SharaGoPref? = null
    var quantity: Int = 0
    var stock: Int = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserVendorDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)
        mpref = SharaGoPref.getInstance(this)
        vendorId= intent.getStringExtra("vendor_id")!!

        Log.e("vendorId", vendorId)
        token= mpref!!.getLoginToken("").toString()
        apiHit()
        apiResult()
        setCustomToolBar()
        statusBarColourChange()
        cartdetailsResult()

        resultSaveAndUpdateCart()
        iv_open_search.setOnClickListener {
            SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.wishList))
            SharaGoPref.getInstance(this).setSearchType(getString(R.string.media))
            startActivity(Intent(this, UserMainActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK
            ))
            finish()  }

        if(SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
            resultCount()
        }
        rl_cart_count.setOnClickListener {
            if(SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                startActivity(Intent(this, UserAddToCartActivity::class.java))
            }else{
                startActivity(Intent(this, UserLoginActivity::class.java))
            }
        }


    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
        mBinding.llToolBar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharaGoPref.getInstance(this).setShowList("Vendor")
        finish()
    }



    private fun setCustomToolBar() {
        mBinding.ivBackVendor.setOnClickListener{
            onBackPressed()
        }
        iv_user_profile.setOnClickListener(this)
        rl_writeReview.setOnClickListener(this)


        tvChatOpen.setOnClickListener {
            if(SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                if(vendorEncryptedId.isNotEmpty()){
                    startActivity(Intent(this, ChatActivity::class.java).putExtra("id",vendorEncryptedId).putExtra("email",emailId).putExtra("fcmkey",fcmKey).putExtra("vendor_name",firstName).putExtra("Product_name","Product"))
                }
            }else{
                startActivity(Intent(this, UserLoginActivity::class.java))
            }

        }



    }
    private fun apiHit(){
       // showLoader()
        mViewModel.userVendorDetailParam(vendorId)
        mViewModel.userVendorRatingDetails(vendorId)
        mViewModel.userNewProductsofVendor(vendorId)
        mViewModel.getDiscountProductsListofVendor(vendorId)
        mViewModel.getRequestForSampleProducts(vendorId)
        mViewModel.userOnSaleProductsParam(vendorId)
        cartApi()

    }

    override fun onResume() {
        super.onResume()
        apiHit()
    }
    lateinit var emailId:String
    lateinit var vendorEncryptedId:String
    lateinit var fcmKey:String
    private fun apiResult(){
        mViewModel.lUserVendorDetail.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        mBinding.tvCompanyName.text=it.metaData!!.companyName
                        mBinding.tvTitle.text=it.metaData.firstName+" | "+"Store"
                        firstName=it.metaData.firstName+" "+it.metaData.lastName
                        mBinding.tvProductDis.text=it.metaData.about
                        mBinding.rBarProductDetails.rating=it.rating!!.dec()

                        emailId=it.email.toString()
                        vendorEncryptedId=it.vendorEncryptedId.toString()
                        fcmKey=it.fcmKey.toString()
                        emailId=it.email.toString()
                        Glide.with(this)
                            .load(MyConstants.file_Base_URL+it.metaData.image).placeholder(R.drawable.avatar)
                            .into(mBinding.ivUserProfile)
                        Glide.with(this)
                            .load(MyConstants.file_Base_URL+it.metaData.coverImage).placeholder(R.drawable.loading)
                            .into(mBinding.ivVendorCoverImage)

                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lUserVendorRatingDetails.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                      mBinding.mAdapterVendorRatingAndReview= AdapterVendorRatingAndReview(this,it.ratingList as ArrayList<RatingListItem?>)
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                 //   Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lUuserNewProductsofVendor.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if (it.productList!!.isNotEmpty()){
                            mBinding.tvNewProduct.visibility=View.VISIBLE
                            mBinding.mAdapterNewProductOfVendor= AdapterNewProductOfVendor(this,it.productList as ArrayList<NewProductListOfVendorListItem?>,this)
                        }
                        else
                            mBinding.tvNewProduct.visibility=View.GONE
                         }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                   // Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lDiscountProductsofVendor.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if (it.productList!!.isNotEmpty()){
                            mBinding.tvDiscountItems.visibility=View.VISIBLE
                            mBinding.mAdapterDiscountProductListOfVendor= AdapterDiscountProductListOfVendor(this,it.productList as ArrayList<NewProductListOfVendorListItem?>,this)
                        }
                        else
                            mBinding.tvDiscountItems.visibility=View.GONE
                        }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                  //  Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lRequestForSampleProducts.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if (it.productList!!.isNotEmpty()){
                            mBinding.tvReqSample.visibility=View.VISIBLE
                           mBinding.mAdapterRequestForSampleProductOfVendor = AdapterRequestForSampleProductOfVendor(this,it.productList as ArrayList<NewProductListOfVendorListItem?>,this)
                        }
                       else
                       mBinding.tvReqSample.visibility=View.GONE
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                 //   Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lOnSaleProductsParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("onSale",it.toString())
                        if (it.productList!!.isNotEmpty()){
                            mBinding.tvOnSaleProducts.visibility=View.VISIBLE
                            mBinding.onSaleProductAdatper= OnSaleAdapter(this,it.productList,this)
                        }else
                            mBinding.tvOnSaleProducts.visibility=View.GONE
                    }
                }
                is ExceptionHandler.Error->{
                    showToast(it.toString())

                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rl_writeReview -> {
                if(SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                    startActivity(Intent(this, UserWriteReviewActivity::class.java).putExtra("item_id",vendorId))
                }else{
                    startActivity(Intent(this, UserLoginActivity::class.java))
                }

            } R.id.iv_user_profile -> {
           // startActivity(Intent(this, ChatActivity::class.java).putExtra("id",vendorEncryptedId).putExtra("email",emailId).putExtra("fcmkey",fcmKey))
            }R.id.tvChatOpen -> {
            }

        }
    }

    override fun addProductToCart(data: ProductListItem?, postion: Int,itemId: String) {
        if(mpref!!.getLoginToken("")!!.isNotEmpty()){
            //for logged in user
            stock=data!!.stock!!
            addITemToCart(data!!.itemId!!,)
        }else{
            startActivity(Intent(this, UserLoginActivity::class.java))
        }
    }
    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if(cartId==""){
            mViewModel.getSaveCartParam(mpref!!.getLoginToken("")!!, jsonMain)
        }else{
            mViewModel.getUpdateCartParam(mpref!!.getLoginToken("").toString(),cartId, jsonMain)
        }
    }


    private var cartResponse: UserCartItem?=null
    private fun cartdetailsResult(){
        mViewModel.lCartDetailsUser.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        cartResponse = it
                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error->{


                }
            }
        }

    }
    private fun cartApi(){
        mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())
        mViewModel.lGetCardDetailsParam.observe(this){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {
                        cartResponse = it

                    }
                }
                is ExceptionHandler.Error->{
                    //Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    private fun resultSaveAndUpdateCart(){
        mViewModel.lSaveCartParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        mViewModel.getCardDetailsUser(SharaGoPref.getInstance(this).getLoginToken("").toString())
                        Utility.toastMessage(this,getString(R.string.item_added))
                        // cartDetailsofUser(""+mpref!!.getToken(""))

                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
        mViewModel.lUpdateCartParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        Utility.toastMessage(this,getString(R.string.item_added))
                        if(!mpref!!.getLoginToken("").equals("")){
                            mViewModel.getCardDetailsUser(token)
                            mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

                            // cartDetailsofUser(""+mpref!!.getToken(""))
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
    }

    override fun addProductToCart(data: NewProductListOfVendorListItem?, position: Int, itemId: String?) {
        if(mpref!!.getLoginToken("")!!.isNotEmpty()){
            //for logged in user
            stock=data!!.stock!!
            addITemToCart(data!!.itemId!!)
        }
    }

    private fun addITemToCart(itemId:String){

        if(mpref!!.getLoginToken("")!!.isNotEmpty()){
            //for logged in user
            if(cartResponse!!.metaData==null|| cartResponse!!.metaData!!.items!!.isEmpty()){
                val item = JsonArray()
                /* for (i in dataList!!) {*/
                val requestBody = JsonObject()
                requestBody.addProperty("itemId",itemId)
                requestBody.addProperty("quantity", "1")
                item.add(requestBody)
                //   }

                val jsonMain = JsonObject()

                val requestBodymetadata = JsonObject()
                requestBodymetadata.add("items", item)
                jsonMain.add("metaData", requestBodymetadata)

                try {
                    if(cartResponse!!.cartId!!.toString()=="null"){
                        saveCartApi(jsonMain, "")
                    }else{
                        saveCartApi(jsonMain, cartResponse!!.cartId.toString())
                    }
                }catch (e:Exception){
                    saveCartApi(jsonMain, "")
                }


            }
            else{

                for (i in cartResponse!!.metaData!!.items!!) {
                    if (i?.itemId ==itemId){
                        quantity = i?.quantity!!
                    }
                }

                if (stock > quantity) {
                    var isAlready=false
                    val item = JsonArray()
                    val itemnew = JsonArray()
                    var count =0
                    for( i in cartResponse!!.metaData!!.items!!){
                        val requestBody = JsonObject()
                        if(i!!.itemId!! != itemId){
                            if(count==0){
                                requestBody.addProperty("itemId", itemId)
                                requestBody.addProperty("quantity", "1")
                                item.add(requestBody)
                            }
                            count++

                        }else{
                            isAlready=true
                        }
                    }

                    for (i in cartResponse!!.metaData!!.items!!) {
                        val requestBody = JsonObject()
                        requestBody.addProperty("itemId", i!!.itemId)
                        if(isAlready){
                            requestBody.addProperty("quantity", i.quantity!!.toInt()+1)
                        }else{
                            requestBody.addProperty("quantity", i.quantity)
                        }
                        item.add(requestBody)
                    }

                    if(isAlready){
                        for (i in cartResponse!!.metaData!!.items!!) {
                            val requestBody = JsonObject()
                            requestBody.addProperty("itemId", i!!.itemId)
                            if(i.itemId!! == itemId){
                                requestBody.addProperty("quantity", i.quantity!!.toInt()+1)
                            }else{
                                requestBody.addProperty("quantity", i.quantity)
                            }
                            itemnew.add(requestBody)
                        }
                    }
                    val jsonMain = JsonObject()

                    val requestBodymetadata = JsonObject()
                    requestBodymetadata.add("items", item)
                    jsonMain.add("metaData", requestBodymetadata)

                    if(isAlready){
                        val jsonMain = JsonObject()
                        val requestBodymetadata = JsonObject()
                        requestBodymetadata.add("items", itemnew)
                        jsonMain.add("metaData", requestBodymetadata)
                        Log.e("jsonMain",jsonMain.toString())
                        saveCartApi(jsonMain,SharaGoPref.getInstance(this).getCartId("").toString())
                    }else{
                        saveCartApi(jsonMain,SharaGoPref.getInstance(this).getCartId("").toString())
                    }

                }else{
                    showToast("Stock Limit Finish!")
                }

            }

        }
    }

    override fun addDataToCart(itemId: Any,reqCode:Int) {
        if(reqCode==MyConstants.REQUEST_FOR_SAMPLE_BY_VD_REQ_CODE){
            addITemToCart(itemId as String)
        }
        if(reqCode==MyConstants.DISCOUNT_BY_VD_REQ_CODE){
            addITemToCart(itemId as String)
        }


    }
    private fun resultCount(){
        mViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

        //user Server Product List result
        mViewModel.lGetCardDetailsParam.observe(this){
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
                    //  Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }

}
