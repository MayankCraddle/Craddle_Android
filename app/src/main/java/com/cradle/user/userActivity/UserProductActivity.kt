package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.ActivityUserProductBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.ProductListItem1
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.roomDataBase.UserDataBase
import com.cradle.user.adapters.AdapterUserProductList
import com.cradle.utils.AnalyticsUtils
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_product.*
import kotlinx.android.synthetic.main.activity_user_product.rl_cart_count
import kotlinx.android.synthetic.main.activity_user_product.tv_cart_count
import kotlinx.android.synthetic.main.custom_toolbar_user.iv_back
import kotlinx.android.synthetic.main.custom_toolbar_user.iv_open_country
import kotlinx.android.synthetic.main.custom_toolbar_user.rl_cus_toolbar
import kotlinx.android.synthetic.main.custom_toolbar_user.tv_usertoolbartitle
import org.json.JSONObject

class UserProductActivity:BaseActivity(),ItemClickListner,AdapterUserProductList.TextState,View.OnClickListener{
    private lateinit var  mBinding:ActivityUserProductBinding
    private lateinit var mViewModel:MainViewModel
    private lateinit var dataBase: UserDataBase
    private var mpref: SharaGoPref? = null
    private var token: String? = null
    private var productList: List<ProductListItem1?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        //mBinding.setVariable(BR.onChaPassWithLoginClick, this)
      //  dataBase= UserDataBase.getDataBase(this@UserProductActivity)

        AnalyticsUtils.analyticReport(this,MyConstants.CategoryProductsScreen)
        mpref = SharaGoPref.getInstance(this)
        iv_back.setOnClickListener(this)
        tv_usertoolbartitle.text=getString(R.string.product_list)
        tvCountryName.text=SharaGoPref.getInstance(this).getCountry("")
        iv_open_country.visibility=View.VISIBLE
        iv_open_country.setOnClickListener {
            startActivity(Intent(this, CountryActivity::class.java))
        }
        iv_cart_screen.setOnClickListener {
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(this, UserAddToCartActivity::class.java))
            else
                startActivity(Intent(this, UserLoginActivity::class.java))

        }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        apiHit()
        setApiSult()
        onSearchCountry()
        statusBarColourChange()
    }

    override fun onResume() {
        super.onResume()
        tv_usertoolbartitle.text=getString(R.string.product_list)
        iv_open_country.visibility=View.VISIBLE
        val flag=SharaGoPref.getInstance(this).getCountryFlag("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(iv_open_country)

        iv_open_country.setOnClickListener {
            startActivity(Intent(this, CountryActivity::class.java))
        }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        card_open_search_view.visibility=View.GONE
        iv_open_search.setOnClickListener {
            rl_cus_toolbar.visibility=View.GONE
            card_open_search_view.visibility=View.VISIBLE
        }

        apiHit()


    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }
    private fun apiHit(){
        val cat_id=intent.getStringExtra("cat_id").toString()
        token=SharaGoPref.getInstance(this).getLoginToken("")

        mViewModel.getUserProListWithCatID(cat_id,"1", "10",
            "",SharaGoPref.getInstance(this).getCountry("").toString())

        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
            mViewModel.getCardDetailsUser(token!!)
            mViewModel.getCardDetailsByIdParam(
                SharaGoPref.getInstance(this).getLoginToken("").toString(),
                SharaGoPref.getInstance(this).getCartId("").toString()
            )
        }


    }
    private var cartResponse: UserCartItem?=null
    private fun setApiSult(){
        mViewModel.lUProListWithCatID.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if(it.productList!!.isNotEmpty()){
                            mBinding.recycProductList.visibility=View.VISIBLE
                            mBinding.ivNoDataFound.visibility=View.GONE
                            mBinding.mAdapterUserProList= AdapterUserProductList(this,it.productList,this,this,this)
                            productList=it.productList
                        }else{
                            mBinding.recycProductList.visibility=View.GONE
                            mBinding.ivNoDataFound.visibility=View.VISIBLE
                        }

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }

        mViewModel.lCartDetailsUser.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        cartResponse = it
                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                   }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }

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
                            mViewModel.getCardDetailsUser(token!!)
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
        mViewModel.lAddProductToWishlistParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    val jsonObject= JSONObject(it.data.toString())
                    Utility.toastMessage(this,jsonObject.optString("message"))

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

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

   /* private fun getLocalData(){
        dataBase.userDao().getAllData().observe(this) {
            Log.e("rahul", it.toString())
            mBinding.mAdapterUserProList= AdapterUserProductList(this,it,this,this,this)
        }
    }*/

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back-> {
                finish()
            }
        }
    }


    override fun textStateAction(data: ProductListItem1?, position: Int) {

        val minQty=data!!.variants!!.get(0)!!.minQuantity
        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
            if(!mpref!!.getLoginToken("")!!.isEmpty()){
                //for logged in user
                if(cartResponse!!.metaData==null|| cartResponse!!.metaData!!.items!!.isEmpty()){
                    val item = JsonArray()
                    /* for (i in dataList!!) {*/
                    val requestBody = JsonObject()
                    requestBody.addProperty("itemId", data!!.itemId)
                  //  requestBody.addProperty("quantity", "1")
                    requestBody.addProperty("quantity", minQty)
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
                    var isAlready=false
                    val item = JsonArray()
                    val itemnew = JsonArray()
                    var count:Int=0
                    for( i in cartResponse!!.metaData!!.items!!){
                        val requestBody = JsonObject()
                        if(i!!.itemId!! != data!!.itemId){
                            if(count==0){
                                requestBody.addProperty("itemId", data.itemId)
                               // requestBody.addProperty("quantity", "1")
                                requestBody.addProperty("quantity", minQty)
                                item.add(requestBody)
                            }
                            count++

                        }else{
                            /*    requestBody.addProperty("itemId", i!!.itemId)
                                requestBody.addProperty("quantity", i.quantity!!.toInt()+1)
                                item.add(requestBody)*/
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
                            if(i.itemId!! == data!!.itemId){
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

                }

            }
        else
            startActivity(Intent(this, UserLoginActivity::class.java))


    }

    override fun addProductToWishList(jsonObject: JsonObject) {
        mViewModel.addProductToWishlistParam(token!!,jsonObject)

    }

    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if(cartId==""){
          mViewModel.getSaveCartParam(mpref!!.getLoginToken("")!!, jsonMain)
        }else{
            mViewModel.getUpdateCartParam(mpref!!.getLoginToken("").toString(),cartId, jsonMain)
        }
    }

    //Country list api
    private fun onSearchCountry(){
        mBinding.countrySearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")){
                    rl_cus_toolbar.visibility=View.VISIBLE
                    card_open_search_view.visibility=View.GONE
                    apiHit()

                }else{
                    rl_cus_toolbar.visibility=View.GONE
                    card_open_search_view.visibility=View.VISIBLE
                    filter(newText.toString())
                }

                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })
    }
    private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<ProductListItem1> = ArrayList()

        //looping through existing elements
        for (s in this.productList!!) {
            //if the existing elements contains the search input
            if (s!!.name!!.lowercase().contains(text.lowercase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = mBinding.mAdapterUserProList!!.filterList(filterdNames)

    }


}