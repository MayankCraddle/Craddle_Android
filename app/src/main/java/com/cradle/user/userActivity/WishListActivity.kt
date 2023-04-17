package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.FragmentUserWishlistBinding
import com.cradle.model.UserWishList
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.UserWishListAdapter
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import org.json.JSONObject

class WishListActivity :BaseActivity(),UserWishListAdapter.TextState{
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mBinding: FragmentUserWishlistBinding
    private var token: String? = null
    private var mpref: SharaGoPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = FragmentUserWishlistBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        AnalyticsUtils.analyticReport(this,MyConstants.WishListScreen)
        mpref = SharaGoPref.getInstance(this)
        token= SharaGoPref.getInstance(this).getLoginToken("")

        mBinding.rlToolBar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.rlOpenCartScreen.setOnClickListener {   startActivity(Intent(this, UserAddToCartActivity::class.java)) }

        if (MyHelper.isNetworkConnected(this)) {
            gatData()
            showData()
            resultCount()
        } else showToast(getString(R.string.no_internet_connection))



    }

    private fun resultCount(){
        val cartId=SharaGoPref.getInstance(this).getCartId("")
        if(!cartId.equals("null")){
            mainViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

            //user Server Product List result
            mainViewModel.lGetCardDetailsParam.observe(this){
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
    private fun addRecyclerView(whishList: List<UserWishList?>?){
        mBinding.userWishListAdapter= UserWishListAdapter(this,whishList,this)
    }
    private fun gatData(){
                mainViewModel.getUserWishList(SharaGoPref.getInstance(this).getLoginToken("").toString())
               mainViewModel.getCardDetailsUser(token!!)
    }
    var cartResponse: UserCartItem?=null
    private fun showData(){
        mainViewModel.liveDataUserLUserWishList.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        try {
                            if (it.metaData!!.items!!.size>0){
                                mBinding.ivNoDataFound.visibility= View.GONE
                                mBinding.recyclerWishList.visibility= View.VISIBLE
                                addRecyclerView(it.metaData.items)
                            }else{
                                mBinding.ivNoDataFound.visibility= View.VISIBLE
                                mBinding.recyclerWishList.visibility= View.GONE
                            }

                        }catch (e:Exception){
                            mBinding.ivNoDataFound.visibility= View.VISIBLE
                            mBinding.recyclerWishList.visibility= View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mainViewModel.lSaveCartParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{

                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        mainViewModel.getCardDetailsUser(SharaGoPref.getInstance(this).getLoginToken("").toString())
                        Utility.toastMessage(this,getString(R.string.item_added))
                        resultCount()
                        // cartDetailsofUser(""+mpref!!.getToken(""))

                    }
                }
                is ExceptionHandler.Error->{


                }
            }
        }
        mainViewModel.lUpdateCartParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{

                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        Utility.toastMessage(this,getString(R.string.item_added))
                        if(!token.toString().equals("")){
                            mainViewModel.getCardDetailsUser(token!!)
                            resultCount()
                            // cartDetailsofUser(""+mpref!!.getToken(""))
                        }
                    }
                }
                is ExceptionHandler.Error->{


                }
            }
        }
        mainViewModel.lCartDetailsUser.observe(this){
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

        mainViewModel.lremoveProductFromWishlistParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        val jsonObject= JSONObject(it.toString())
                     //   Utility.toastMessage(this,jsonObject.optString("message"))
                        gatData()
                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun addtoCard(data: UserWishList?, position: Int) {
      val  minQuantity= data!!.minQuantity
        if(mpref!!.getLoginToken("")!!.isNotEmpty()){
            //for logged in user
            if(cartResponse!!.metaData==null|| cartResponse!!.metaData!!.items!!.isEmpty()){
                val item = JsonArray()
                /* for (i in dataList!!) {*/
                val requestBody = JsonObject()
                requestBody.addProperty("itemId",data!!.itemId)
                requestBody.addProperty("quantity", minQuantity)
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
                var count =0
                for( i in cartResponse!!.metaData!!.items!!){
                    val requestBody = JsonObject()
                    if(i!!.itemId!! != data!!.itemId){
                        if(count==0){
                            requestBody.addProperty("itemId", data.itemId)
                            requestBody.addProperty("quantity", minQuantity)
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

                    saveCartApi(jsonMain,SharaGoPref.getInstance(this).getCartId("").toString())
                }else{
                    saveCartApi(jsonMain,SharaGoPref.getInstance(this).getCartId("").toString())
                }

            }

        }
    }
    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if(cartId==""){
            mainViewModel.getSaveCartParam(token.toString(), jsonMain)
        }else{
            mainViewModel.getUpdateCartParam(token.toString(),cartId, jsonMain)
        }
    }

    override fun removeProductToWishList(jsonObject: JsonObject) {
        mainViewModel.removeProductFromWishlistParam(SharaGoPref.getInstance(this).getLoginToken("")!!,jsonObject)


    }

    override fun onResume() {
        super.onResume()
        resultCount()
    }

}