package com.cradle.user.userActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityViewAllBinding
import com.cradle.model.cart.UserCartItem
import com.cradle.model.trade.ProductListItemByTrade
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.ViewAllByTradeAdapter
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_product.*
import kotlinx.android.synthetic.main.activity_user_product_details.*
import kotlinx.android.synthetic.main.activity_view_all.*
import kotlinx.android.synthetic.main.dialog_sortby.*

class ViewAllByTradeActivity : BaseActivity(), View.OnClickListener,
    ViewAllByTradeAdapter.TextState {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityViewAllBinding
    private var mpref: SharaGoPref? = null
    var categoryId = ""
    var sortBy = ""
    var country = ""
    var stock: Int = 0
    var limit: Int = 20
    var pageNUmber: Int = 1
    // var quantity: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityViewAllBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        if (MyHelper.isNetworkConnected(this)) {
            findId()
        } else showToast(getString(R.string.no_internet_connection))

    }


    private lateinit var listWhich: String
    lateinit var token: String
    private fun findId() {
        mpref = SharaGoPref.getInstance(this)

        listWhich = intent.getStringExtra("list").toString()
        country = SharaGoPref.getInstance(this).getCountry("").toString()


        apiHit(listWhich)
        iv_back_trade.setOnClickListener { finish() }
        mBinding.rlOpenCartScreen.setOnClickListener {
            startActivity(
                Intent(
                    this, UserAddToCartActivity::class.java
                )
            )
        }

        mBinding.rlCusToolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        resultCount()
        llSortby.setOnClickListener { sortByDialog() }
        llCategoryFilter.setOnClickListener {
            startActivityForResult(
                Intent(
                    this, CategoryFilterActivity::class.java
                ), 100
            )
        }


            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                token = SharaGoPref.getInstance(this).getLoginToken("")!!
                mViewModel.getCardDetailsUser(token)
            }


    }

    private fun apiHit(listWhich: String) {
        if (listWhich.equals("NewArrivals")) {
            mViewModel.newArrivalsByTradParam(
                country,
                categoryId,
                sortBy,
                pageNUmber.toString(),
                limit.toString()
            )
            mBinding.tvUsertoolbartitle.text = getString(R.string.p_new_arrivals)
        }
        if (listWhich.equals("OnSale")) {
            mViewModel.topDealsByCountryParam(
                country,
                categoryId,
                sortBy,
                pageNUmber.toString(),
                limit.toString()
            )
            mBinding.tvUsertoolbartitle.text = getString(R.string.top_deals)
        }
        if (listWhich.equals("TopDeals")) {
            mViewModel.onSaleByCountryParam(
                country,
                categoryId,
                sortBy,
                pageNUmber.toString(),
                limit.toString()
            )
            mBinding.tvUsertoolbartitle.text = getString(R.string.on_sale)
        }
        apiResult()
    }

    private fun resultCount() {
        val cartID= SharaGoPref.getInstance(this).getCartId("").toString()

        if(!cartID.equals("null")){
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

                            if (it.cartSize!!.isNotEmpty()) {
                                mBinding.rlCartCount.visibility = View.VISIBLE
                                mBinding.tvCartCount.text = it.cartSize
                            } else {
                                mBinding.rlCartCount.visibility = View.VISIBLE
                                mBinding.tvCartCount.text = "0"
                            }
                            // activityUserAddtocartBinding.userAddToCartAdapter= UserAddToCartAdapter(this/*,it.metaData!!.items*/)

                        }
                    }
                    is ExceptionHandler.Error -> {
                        //  Utility.toastMessage(requireActivity(),it.errorMessage)
                    }
                }
            }
        }else{
            mBinding.rlCartCount.visibility = View.VISIBLE
            mBinding.tvCartCount.text = "0"
        }

    }

    override fun onClick(p0: View?) {

    }

    private var cartResponse: UserCartItem? = null
    private fun apiResult() {
        mViewModel.lCartDetailsUser.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    it.data?.let {
                        Log.e("cartResponse", it.toString())
                        cartResponse = it
                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()

                }
            }
        }
        mViewModel.lUpdateCartParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    it.data?.let {
                        try {
                         //   Utility.toastMessage(this, getString(R.string.item_added))
                            resultCount()
                            if (!mpref!!.getLoginToken("").equals("")) {
                                mViewModel.getCardDetailsUser(mpref!!.getLoginToken("")!!)
                                // cartDetailsofUser(""+mpref!!.getToken(""))
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()

                }
            }
        }
        mViewModel.lnewArrivalsByTrade.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        try {
                            //  Log.e("cartDetails", it.toString())
                            if (it.productList!!.isNotEmpty()) {
                                setNewArrivalsAdapter(it.productList)
                            } else {
                                ll_newArrivals_pro.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    this.showToast(it.toString())

                }
            }
        }
        mViewModel.ltopDealsByCountry.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        try {
                            //  Log.e("cartDetails", it.toString())
                            if (it.productList!!.isNotEmpty()) {
                                setNewArrivalsAdapter(it.productList)
                            } else {
                                ll_daily_need_pro.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    this.showToast(it.toString())

                }
            }
        }
        mViewModel.lonSaleByCountryReq.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        try {
                            // Log.e("cartDetails", it.toString())
                            if (it.productList!!.isNotEmpty()) {
                                setNewArrivalsAdapter(it.productList)
                            } else {
                                ll_daily_need_pro.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    this.showToast(it.toString())

                }
            }

            mViewModel.lSaveCartParam.observe(this) {
                when (it) {
                    is ExceptionHandler.Loading -> {
                        dismissLoader()
                    }
                    is ExceptionHandler.Success -> {
                        dismissLoader()
                        it.data?.let {
                            try {
                                mViewModel.getCardDetailsUser(
                                    SharaGoPref.getInstance(this).getLoginToken("").toString()
                                )
                                Utility.toastMessage(this, getString(R.string.item_added))
                                resultCount()
                                // cartDetailsofUser(""+mpref!!.getToken(""))
                            } catch (e: Exception) {
                            }
                        }
                    }
                    is ExceptionHandler.Error -> {
                        dismissLoader()

                    }
                }
            }

        }
    }

    private fun setNewArrivalsAdapter(newArrivalList: List<ProductListItemByTrade?>) {
        mBinding.viewAllByTradeAdapter = ViewAllByTradeAdapter(this, newArrivalList, this)

    }

    override fun textStateAction(data: ProductListItemByTrade?, position: Int) {
        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
            stock = data!!.stock!!
            val itemId = data.itemId
            var quantity = 0
            if (!mpref!!.getLoginToken("")!!.isEmpty()) {
                //for logged in user
                if (cartResponse!!.metaData == null || cartResponse!!.metaData!!.items!!.isEmpty()) {
                    val item = JsonArray()
                    /* for (i in dataList!!) {*/
                    val requestBody = JsonObject()
                    requestBody.addProperty("itemId", data!!.itemId)
                   // requestBody.addProperty("quantity", "1")
                    requestBody.addProperty("quantity", data.variants!!.get(0)!!.minQuantity)
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


                } else {

                    for (i in cartResponse!!.metaData!!.items!!) {
                        if (i?.itemId == itemId) {
                            quantity = i?.quantity!!
                        }
                    }

                    if (stock > quantity) {
                        var isAlready = false
                        val item = JsonArray()
                        val itemnew = JsonArray()
                        var count: Int = 0
                        for (i in cartResponse!!.metaData!!.items!!) {
                            val requestBody = JsonObject()
                            if (i!!.itemId!! != data!!.itemId) {
                                if (count == 0) {
                                    requestBody.addProperty("itemId", data.itemId)
                                   // requestBody.addProperty("quantity", "1")
                                    requestBody.addProperty("quantity", data.variants!!.get(0)!!.minQuantity)
                                    item.add(requestBody)
                                }
                                count++

                            } else {
                                /*    requestBody.addProperty("itemId", i!!.itemId)
                                    requestBody.addProperty("quantity", i.quantity!!.toInt()+1)
                                    item.add(requestBody)*/
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
                                if (i.itemId!! == data!!.itemId) {
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
                                jsonMain, SharaGoPref.getInstance(this).getCartId("").toString()
                            )
                        } else {
                            saveCartApi(
                                jsonMain, SharaGoPref.getInstance(this).getCartId("").toString()
                            )
                        }
                    } else {
                        showToast("Stock Limit Finish!")
                    }


                }

            }
        }
        else{
            startActivity(Intent(this, UserLoginActivity::class.java))
        }



    }

    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if (cartId == "") {
            mViewModel.getSaveCartParam(mpref!!.getLoginToken("")!!, jsonMain)
        } else {
            mViewModel.getUpdateCartParam(mpref!!.getLoginToken("").toString(), cartId, jsonMain)
        }
    }

    override fun addProductToWishList(jsonObject: JsonObject) {

    }

    override fun onResume() {
        super.onResume()
        resultCount()
        findId()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            var id = data?.getStringExtra("id")
            //mViewModel.newArrivalsByTradParam(country, id.toString(), "", "1", "10")
            categoryId = id.toString()
            sortBy = ""
            pageNUmber = 1
            limit = 20
            apiHit(listWhich)

            //showToast("id" + id)
        }
    }

    private fun sortByDialog() {
        var dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_sortby)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        dialog!!.setCanceledOnTouchOutside(true)

        val llHightToLow = dialog!!.findViewById(R.id.llHightToLow) as LinearLayout
        val llLowToHigh = dialog!!.findViewById(R.id.llLowtoHigh) as LinearLayout
        val llRating = dialog!!.findViewById(R.id.llRating) as LinearLayout
        val ivHightToLow = dialog!!.findViewById(R.id.ivHightToLow) as ImageView
        val ivLowToHigh = dialog!!.findViewById(R.id.ivLowtoHigh) as ImageView
        val ivRating = dialog!!.findViewById(R.id.ivRating) as ImageView
        val rl_dialog = dialog!!.findViewById(R.id.rl_dialog) as RelativeLayout

        llHightToLow.setOnClickListener {
            ivHightToLow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.check_radio))
            categoryId = ""
            sortBy = "Price_HL"
            pageNUmber = 1
            limit = 20
            apiHit(listWhich)

            dialog!!.dismiss()
        }
        llLowToHigh.setOnClickListener {
            ivLowToHigh.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.check_radio))
            categoryId = ""
            sortBy = "Price_LH"
            pageNUmber = 1
            limit = 20
            apiHit(listWhich)
            dialog!!.dismiss()
        }
        llRating.setOnClickListener {
            ivRating.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.check_radio))
            categoryId = ""
            sortBy = "rating"
            pageNUmber = 1
            limit = 20
            apiHit(listWhich)
            dialog!!.dismiss()
        }
        rl_dialog.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

}
