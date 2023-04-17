package com.cradle.user.userActivity


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserAddtocartBinding
import com.cradle.model.address.AddressListItem
import com.cradle.model.cart.ItemsItem
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.repository.QuoteRepository
import com.cradle.user.adapters.*
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_addtocart.*
import kotlinx.android.synthetic.main.checkout_dialog.*
import kotlinx.android.synthetic.main.checkout_dialog.rlLogOut
import kotlinx.android.synthetic.main.logout_dialog.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class UserAddToCartActivity : BaseActivity(), View.OnClickListener,
    UserAddToCartAdapter.ServerDeleteCart, UserAddToCartAdapter.ServerUpdateQty {


    private lateinit var cartAdapter: LocalCartListAdapter
    private lateinit var response: QuoteRepository
    private lateinit var userAddToCartViewModel: MainViewModel
    private lateinit var activityUserAddtocartBinding: ActivityUserAddtocartBinding
  //  private lateinit var dataBase: UserDataBase
    private var mpref: SharaGoPref? = null
    private var token: String? = null
    private var publicKey: String? = null
    private var secretKey: String? = null
    private var encryptionKey: String? = null
    private var txRef: String? = null
    private var shippingRate: Double = 0.0
    var isShowing =false


    val cartList = ArrayList<ItemsItem>()
    private var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inisilizeUI()
    }

    @SuppressLint("ResourceAsColor")
    private fun inisilizeUI() {
        response = (application as ApplicationClass).repository

        activityUserAddtocartBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_addtocart)
        userAddToCartViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        activityUserAddtocartBinding.mainViewModel = userAddToCartViewModel
        activityUserAddtocartBinding.setVariable(BR.onAddToCartClick, this)
     //   dataBase = UserDataBase.getDataBase(this)
        mpref = SharaGoPref.getInstance(this)
        token = SharaGoPref.getInstance(this).getLoginToken("")
        AnalyticsUtils.analyticReport(this,MyConstants.CartViewed)
        activityUserAddtocartBinding.ivBack.visibility = View.VISIBLE
        activityUserAddtocartBinding.tvUsertoolbartitle.text = getString(R.string.cart_list)
        activityUserAddtocartBinding.rlCusToolbar.setBackgroundColor(
            SharaGoPref.getInstance(this).getColor(0)!!
        )


        activityUserAddtocartBinding.ivBack.setOnClickListener {
            finish()
        }
        tv_manage_add.setOnClickListener(this)
        edt_default_address.setOnClickListener(this)
        rl_checkout.setOnClickListener(this)
        isCheckLocalAndServer()
        statusBarColourChange()

    }

    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    private fun isCheckLocalAndServer() {
        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
            getData()
            apiResult()
            orderProcessResult()
        }
    }

    private fun getData() {

        if (MyHelper.isNetworkConnected(this)) {
            apiHit()

        } else showToast(getString(R.string.no_internet_connection))
    }

    private fun apiHit() {
        //Log.e("cartID",SharaGoPref.getInstance(this).getCartId("").toString())

        userAddToCartViewModel.getCardDetailsUser(token!!)

        userAddToCartViewModel.getCardDetailsByIdParam(
            token!!, SharaGoPref.getInstance(this).getCartId("").toString()
        )
        userAddToCartViewModel.userDetailByTokenParam(
            SharaGoPref.getInstance(this).getLoginToken("").toString()
        )
    }

    override fun onResume() {
        super.onResume()
        apiHit()
        apiResult()
    }

  /*  private fun setLocalCartListAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        activityUserAddtocartBinding.recyclerCartList.layoutManager = manager

        dataBase.userDao().getAllData().observe(this) {
            Log.e("rahul", it.toString())
            cartAdapter = LocalCartListAdapter(this, it)
            activityUserAddtocartBinding.recyclerCartList.adapter = cartAdapter
        }


    }*/

    var cartResponse: UserCartItem? = null
    var cartId = ""
    var addresEdit: AddressListItem? = null

    var emaild_id: String? = null
    var address: String? = null
    var addressId: String? = null
    var usercartData: UserCartItem? = null

    @SuppressLint("SetTextI18n")
    private fun apiResult() {
        //user Server Product List result
        userAddToCartViewModel.lGetCardDetailsParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    it.data?.let {
                        cartResponse = it
                        for (i in 0 until it.metaData!!.items!!.size) {
                            cartList.add(it.metaData.items!!.get(i)!!)
                        }
                        cartId = it.cartId.toString()
                        if (it.metaData.items!!.size > 0) {

                            usercartData = it
                            activityUserAddtocartBinding.ivNoDataFound.visibility = View.GONE
                            activityUserAddtocartBinding.recyclerCartList.visibility = View.VISIBLE
                            val manager =
                                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                            activityUserAddtocartBinding.recyclerCartList.layoutManager = manager
                            val cartAdapter =
                                UserAddToCartAdapter(this, it.metaData.items, this, this)
                            activityUserAddtocartBinding.recyclerCartList.adapter = cartAdapter
                            serverAmountCalculation(it.metaData.items)

                        } else {
                            cartList.clear()
                            tv_sub_total.text = ""
                            tv_total_amount.text = ""
                            activityUserAddtocartBinding.ivNoDataFound.visibility = View.VISIBLE
                            activityUserAddtocartBinding.recyclerCartList.visibility = View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error -> {
                    //  Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
        userAddToCartViewModel.lRemoveItemFromCartParam.observe(this) {

            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    it.data?.let {
                        try {
                        val jsonObject = JSONObject(it.toString())
                        //   Utility.toastMessage(this, jsonObject.optString("message"))
                        userAddToCartViewModel.getCardDetailsByIdParam(
                            token!!, SharaGoPref.getInstance(this).getCartId("").toString()
                        )

                            dialog!!.dismiss()

                            apiHit()
                            getData()
                        }catch (e:Exception){}

                        //userAddToCartViewModel.getCardDetailsUser(token!!)

                    }
                }
                is ExceptionHandler.Error -> {
                    //  Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
        userAddToCartViewModel.lUpdateCartParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    it.data?.let {
                        Log.d("response", it.toString())
                        apiHit()
                        val jsonObject = JSONObject(it.toString())
                        //   Utility.toastMessage(this, jsonObject.optString("message"))
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()

                }
            }
        }



        userAddToCartViewModel.luserDetailByTokenReqParam.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {
                    it.data?.let {
                        Log.e("profile", it.toString())
                        emaild_id = it.emailMobile.toString()
                        /* if (it.metaData!!.image!=null){
                             Glide.with(requireActivity()).load(MyConstants.file_Base_URL_flag+it.metaData!!.image).into(mBinding.ivUserProfile)
                         }*/



                        try {
                            if (it.address != null) {

                                activityUserAddtocartBinding.tvManageAdd.text =
                                    getString(R.string.manage)
                                address = getString(R.string.manage)
                                activityUserAddtocartBinding.llAddres.visibility = View.VISIBLE
                                /*activityUserAddtocartBinding.edtDefaultAddress.visibility =
                                    View.GONE*/
                                activityUserAddtocartBinding.ivNoAddrss.visibility = View.GONE
                                activityUserAddtocartBinding.tvName.text =
                                    it.address.metaData!!.firstName + " " + it.address.metaData.lastName.toString()
                                activityUserAddtocartBinding.tvAddress.text =
                                    it.address.metaData.streetAddress.toString() + ", " + it.address.metaData.city + ", " + it.address.metaData.country

                                addresEdit = it.address as AddressListItem
                                addressId = it.address.id
                            } else {
                                activityUserAddtocartBinding.llAddres.visibility = View.GONE

                                activityUserAddtocartBinding.ivNoAddrss.visibility = View.VISIBLE
                                activityUserAddtocartBinding.tvManageAdd.text =
                                    getString(R.string.add)
                                address = getString(R.string.add)
                            }
                        } catch (e: Exception) {
                            activityUserAddtocartBinding.llAddres.visibility = View.GONE
                            activityUserAddtocartBinding.ivNoAddrss.visibility = View.VISIBLE
                        }


                    }

                }
                is ExceptionHandler.Error -> {

                }
            }
        }

    }

    private fun orderProcessResult() {
        userAddToCartViewModel.lProcessOrder.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    it.data?.let {
                        Log.d("orderId", it.toString())

                        try {
                            val jsonObject = JSONObject(it.toString())

                            /*  {"success":true,"redirectUrl":"https://ravemodal-dev.herokuapp.com/v3/hosted/pay/d1c85e085bb448e633f2"
                                  ,"publicKey":"FLWPUBK_TEST-27de60d2977ae0513a24af4de842a758-X",
                                  "secretKey":"FLWSECK_TEST-2610c651acdd98e1533a57ec66597b50-X",
                                  "encryptionKey":"FLWSECK_TESTbe131c8a0412",
                                  "txRef":"d1c85e085bb448e633f2"}*/
                            publicKey = jsonObject.optString("publicKey")

                            secretKey = jsonObject.optString("secretKey")
                            encryptionKey = jsonObject.optString("encryptionKey")
                            txRef = jsonObject.optString("txRef")
                      /*   var   accessCode = jsonObject.optString("accessCode")
                            Log.e("publicKey", publicKey.toString())
*/
                            //showToast("jawed")
                          //   startActivity(Intent(this, PaymentActivity::class.java).putExtra("accessCode",accessCode))

                            paymentGatewaySetUp(totalAmount, "USD", txRef, "fbfbf")

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
                is ExceptionHandler.Error -> {
                    showToast(it.errorMessage)
                    dismissLoader()
                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_checkout -> {
                try {
                    if (cartList.isEmpty()) {
                        showToast("Cart is Empty!")
                        return
                    } else if (usercartData == null && cartList.isEmpty()) {
                        showToast("Cart is Empty!")
                        return
                    } else if (addressId == null && addressId!!.isEmpty()) {
                        showToast("Please Add Shipping Address!")
                        return
                    } else {
                        AnalyticsUtils.analyticReport(this,MyConstants.CheckoutClicked)
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("cartId", usercartData!!.cartId)
                        jsonObject.addProperty("shippingAddressId", addressId)
                        getShipmentRateApi(jsonObject)


                    }
                } catch (e: Exception) {
                    showToast("Please Add Shipping Address!")
                }

                //checkoutDialog()
                // processOrder()
            }
            R.id.iv_back -> {
                finish()
            }
            R.id.edt_default_address -> {
                addresEdit?.let {
                    // not null do something
                    startActivity(
                        Intent(
                            this, UserAddAddressActivity::class.java
                        ).putExtra("data", addresEdit!! as AddressListItem)
                    )

                }
            }
            R.id.tv_manage_add -> {
                if (address.equals(getString(R.string.manage))) startActivity(
                    Intent(
                        this, UserAddressActivity::class.java
                    )
                )
                else startActivity(Intent(this, UserAddressActivity::class.java))


            }
        }
    }

    private fun apiHitResultVerify(transactionRef: String, transaction_id: String, cartId: String) {

        userAddToCartViewModel.verifyPaymentParam(transactionRef, transaction_id, cartId)
        userAddToCartViewModel.lverifyPayment.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    it.data?.let {
                        Log.d("PaymentVerify", it.toString())
                        userAddToCartViewModel.getCardDetailsUser(token!!)
                        userAddToCartViewModel.lCartDetailsUser.observe(this){
                                it ->
                            when(it){
                                is ExceptionHandler.Loading->{
                                    dismissLoader()
                                }
                                is ExceptionHandler.Success->{
                                    dismissLoader()
                                    it.data?.let {
                                        Log.e("cartDetails",it.toString())
                                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                                    }
                                }
                                is ExceptionHandler.Error->{
                                    dismissLoader()

                                }
                            }
                        }
                        try {

                            startActivity(
                                Intent(this, PaymentSuccessActivity::class.java).putExtra(
                                    "type", "success"
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    startActivity(
                        Intent(this, PaymentSuccessActivity::class.java).putExtra("type", "failed")
                    )
                }
            }
        }


    }

    override fun serverDeleteItem(position: Int, dataList: ItemsItem?) {
        if(isShowing==false){
            dialog = Dialog(this)
            deleteItemDialog(dataList)
        }



    }

    override fun moveToWishList(position: Int, dataList: ItemsItem?) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("itemId", dataList?.itemId)
        userAddToCartViewModel.addProductToWishlistParam(token!!, jsonObject)
        userAddToCartViewModel.lAddProductToWishlistParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {


                }
                is ExceptionHandler.Success -> {
                    try {
                        val jsonObject = JSONObject(it.data.toString())
                        Utility.toastMessage(this, jsonObject.optString("message"))
                        deleteItem(dataList)
                    } catch (e: Exception) {
                    }
                }
                is ExceptionHandler.Error -> {
                    showToast(it.errorMessage)

                }
            }
        }
    }

    private fun deleteItem(dataList: ItemsItem?) {
        try {
            userAddToCartViewModel.removeItemFromCartParam(
                token!!, cartId, dataList!!.itemId.toString()
            )

            serverAmountCalculation(listOf(dataList))
        } catch (e: java.lang.Exception) {
        }
    }


    private fun deleteItemDialog(dataList: ItemsItem?) {

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.logout_dialog)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
        dialog!!.tvTitle.text = getString(R.string.remove_item)
        isShowing=true
        dialog!!.rlYes.setOnClickListener {
            isShowing=false
            deleteItem(dataList) }
        dialog!!.rlNo.setOnClickListener {
            isShowing=false
            dialog!!.dismiss() }
        dialog!!.rlLogOut.setOnClickListener {
            isShowing=false
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

    override fun serverUpdateQty(position: Int, dataList: List<ItemsItem?>) {
        var item: JsonArray = JsonArray()

        for (i in dataList) {
            var requestBody = JsonObject()
            requestBody.addProperty("itemId", i!!.itemId)
            requestBody.addProperty("quantity", i.quantity)
            requestBody.addProperty("variant", i.variant)
            item.add(requestBody)
        }

        val jsonMain = JsonObject()

        val requestBodymetadata = JsonObject()
        requestBodymetadata.add("items", item)
        jsonMain.add("metaData", requestBodymetadata)

        userAddToCartViewModel.getUpdateCartParam(
            mpref!!.getLoginToken("").toString(), cartId, jsonMain
        )

        serverAmountCalculation(dataList)
    }

    var totalAmount = 0.0;

    @SuppressLint("SetTextI18n")
    fun serverAmountCalculation(dataList: List<ItemsItem?>?) {
        totalAmount = 0.0;
        for (i in dataList!!) {
            totalAmount = totalAmount + (i!!.discountedPrice)!!.toDouble() * (i.quantity)!!.toInt()
        }
        tv_sub_total.text = getString(R.string.currency)+totalAmount
        tv_total_amount.text = getString(R.string.currency)+totalAmount

    }

    private fun getShipmentRateApi(jsonObject: JsonObject) {
        showLoader()
        dialog = Dialog(this)
        userAddToCartViewModel.getShipmentRateApi(
            SharaGoPref.getInstance(this).getLoginToken("")!!, jsonObject
        )
        userAddToCartViewModel.lshipmentRate.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    Utility.toastMessage(this, it.errorMessage)
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    try {
                        userAddToCartViewModel.lshipmentRate.removeObservers(this);
                        lifecycleScope.launch{
                            it.data?.let {
                                //Log.e("TrackOrder>>>", it.data!!.toString())
                                val jsonObject = JSONObject(it.toString())
                                shippingRate = jsonObject.optDouble("shippingRate")

                              //   checkoutDialog()
                                //new
                                val yourInent = Intent(this@UserAddToCartActivity, ProcessOrderActivity::class.java)
                                val b = Bundle()
                                b.putDouble("shippingRate", shippingRate)
                                b.putDouble("totalAmount", totalAmount)
                                yourInent.putExtras(b)
                                startActivity(yourInent)
                                /*    startActivity(Intent(this@UserAddToCartActivity, ProcessOrderActivity::class.java)
                                    .putExtra("shippingRate",shippingRate).putExtra("totalAmount",totalAmount))
    */
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }

    private fun checkoutDialog() {
        // dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.checkout_dialog)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
        dialog!!.tvSubtotal.text = totalAmount.toString()
        dialog!!.tvShippingCost.text = shippingRate.toString()
        totalAmount = (totalAmount + shippingRate)
        dialog!!.tvTotalAmount.text = (totalAmount).toString()
        dialog!!.setCancelable(true)
        dialog!!.rlCancel.setOnClickListener {
            AnalyticsUtils.analyticReport(this,MyConstants.ShippingRateRejected)
            dialog!!.dismiss() }
        dialog!!.llContinue.setOnClickListener {
            AnalyticsUtils.analyticReport(this,MyConstants.ShippingRateAccepted)
            processOrder()
            dialog!!.dismiss()
        }
        //dialog!!.rlLogOut.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    fun processOrder() {
        var cartMetaDataArray = JsonArray()
        if (usercartData != null && cartList!!.isNotEmpty()) {
            for (i in usercartData!!.metaData!!.items!!) {
                var cartMetaDataJson = JsonObject()
                cartMetaDataJson.addProperty("productId", i!!.productId)
                cartMetaDataJson.addProperty("itemId", i.itemId)
                cartMetaDataJson.addProperty("quantity", i.quantity)
                cartMetaDataJson.addProperty("name", i.name)
                cartMetaDataJson.addProperty("stock", i.stock)
                cartMetaDataJson.addProperty("currency", i.currency)
                cartMetaDataJson.addProperty("actualPrice", i.actualPrice)
                cartMetaDataJson.addProperty("discountValue", i.discountValue)
                cartMetaDataJson.addProperty("discountedPrice", i.discountedPrice)
                cartMetaDataJson.addProperty("totalPrice", totalAmount)
                cartMetaDataJson.addProperty("rating", i.rating)
                cartMetaDataJson.addProperty("country", i.country)
                cartMetaDataJson.addProperty("vendorId", i.vendorId)
                cartMetaDataJson.addProperty("vendorEncryptedId", i.vendorEncryptedId)
                val metaData = JsonObject()
                metaData.addProperty("description", i.metaData!!.description)
                metaData.addProperty("shortDescription", i.metaData.shortDescription)
                metaData.addProperty("material", i.metaData.material)
                metaData.addProperty("noOfPieces", i.metaData.noOfPieces)
                metaData.addProperty("productLength", i.metaData.productLength)
                metaData.addProperty("productWidth", i.metaData.productWidth)
                metaData.addProperty("productHeight", i.metaData.productHeight)
                metaData.addProperty("dimensionUnit", i.metaData.dimensionUnit)
                metaData.addProperty("boxId", i.metaData.boxId)
                val jsonArray = JsonArray()
                for (i in i.metaData.images!!) {
                    jsonArray.add(i)
                }
                metaData.add("images", jsonArray)
                cartMetaDataJson.add("metaData", metaData)

                cartMetaDataArray.add(cartMetaDataJson)

            }
            val finalJson = JsonObject()

            finalJson.addProperty("cartId", usercartData!!.cartId)
            val jsonItems = JsonObject()
            jsonItems.add("items", cartMetaDataArray)

            finalJson.add("cartMetaData", jsonItems)
            finalJson.addProperty("totalPrice", totalAmount)
            finalJson.addProperty("paymentMethod", "flutterwave ")
            val jsonorderMetaData = JsonObject()
            if (addressId != null && addressId!!.isNotEmpty()) {
                jsonorderMetaData.addProperty("billingAddressId", addressId)
                jsonorderMetaData.addProperty("shippingAddressId", addressId)
            } else {
                showToast("Please add Address!")
                return
            }

            finalJson.add("orderMetaData", jsonorderMetaData)

            Log.e("finalJson", finalJson.toString())

            userAddToCartViewModel.processOrderParam(token!!, finalJson)
        } else {
            showToast("Cart is Empty")
        }

    }

    fun paymentGatewaySetUp(
        amount: Double, currencyCode: String?, transactionRef: String?, message: String
    ) {
        try {
            /*    subAccount!!.id = "1234"
                // subAccount.transactionSplitRatio = "1"
                subAccount.transactionChargeType = "flat"
                subAccount.transactionCharge = "0"
                subAccounts.add(subAccount)*/

            Log.e("TAG", "amount>>>>>>>>>>>>>>>>>>>>>>>>>>: " + amount.toString())
            RaveUiManager(this)
                //.setCurrency("NGN")
                .setCurrency(currencyCode).setAmount(amount.toDouble())
                .setEmail(SharaGoPref.getInstance(this).getEmail(""))
                .setfName(SharaGoPref.getInstance(this).getUserName(""))
                //.setfName("rahul")
                .setlName(" ").setNarration(message ?: " ")
               /* .setPublicKey("FLWPUBK_TEST-e05336e20b9e8edf32112b4fed96acc7-X")
                .setEncryptionKey("FLWSECK_TEST661ed6882d0e")*/
                .setPublicKey(publicKey)
                .setEncryptionKey(encryptionKey)
                /*.setPublicKey(MyConstants.FLUTTERWAVE_PUBLIC_API_KEY)
                .setEncryptionKey(MyConstants.FLUTTERWAVE_SECRET_API_KEY)*/
                .setTxRef("" + transactionRef).setPhoneNumber("0123456789", false)
                .acceptAccountPayments(true).acceptCardPayments(true).acceptMpesaPayments(false)
                .acceptAchPayments(false)
                /*.setSubAccounts(subAccounts)*/.acceptGHMobileMoneyPayments(false)
                .acceptUgMobileMoneyPayments(false).acceptZmMobileMoneyPayments(false)
                .acceptRwfMobileMoneyPayments(false).acceptSaBankPayments(false)
                .acceptUkPayments(false).acceptBankTransferPayments(false).acceptUssdPayments(false)
                .acceptBarterPayments(false).acceptFrancMobileMoneyPayments(false, "US")
                .allowSaveCardFeature(false).onStagingEnv(false)
                /* .setMeta(List<Meta>)
                     .withTheme(styleId)
                     .isPreAuth(boolean)
                     .setSubAccounts(List<SubAccount>)*/.shouldDisplayFee(true)
                .showStagingLabel(false).initialize();
        } catch (e: java.lang.Exception) {
            showToast("")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RaveConstants.RAVE_REQUEST_CODE && data != null) {
            try {
                val message: String = data!!.getStringExtra("response").toString()
                if (resultCode.equals(RavePayActivity.RESULT_SUCCESS)) {
                    val jsonObject = JSONObject(message.toString())
                    val transactionId = jsonObject.optJSONObject("data")!!.optString("AccountId")
                    val txRef = jsonObject.optJSONObject("data")!!.optString("txRef")
                    /*Log.e(
                        "TAG",
                        "onActivityResult: " + jsonObject.optJSONObject("data")!!
                            .optString("cartId")
                    )*/

                    apiHitResultVerify(
                        txRef,
                        transactionId,
                        SharaGoPref.getInstance(this).getCartId("").toString()
                    )

                } else if (resultCode.equals(RavePayActivity.RESULT_ERROR)) {
                    //finish()
                } else if (resultCode.equals(RavePayActivity.RESULT_CANCELLED)) {
                   // finish()
                }
            } catch (_: Exception) {
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data)
           // finish()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

    }

}
