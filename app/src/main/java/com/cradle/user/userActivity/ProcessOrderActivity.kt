package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityProcessOrderBinding
import com.cradle.model.address.AddressListItem
import com.cradle.model.cart.ItemsItem
import com.cradle.model.cart.UserCartItem
import com.cradle.paystack_payment.PaymentActivity
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_product_details.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class ProcessOrderActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityProcessOrderBinding
    var totalAmount = 0.0
    var shippingRate = 0.0
    private var mpref: SharaGoPref? = null
    private var token: String? = null

    var cartResponse: UserCartItem? = null
    var cartId = ""
    var addresEdit: AddressListItem? = null

    var emaild_id: String? = null
    var paymentType: String? = null
    var address: String? = null
    var addressId: String? = null
    var usercartData: UserCartItem? = null
    val cartList = ArrayList<ItemsItem>()
    private var publicKey: String? = null
    private var secretKey: String? = null
    private var encryptionKey: String? = null
    private var txRef: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityProcessOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        mpref = SharaGoPref.getInstance(this)
        token = SharaGoPref.getInstance(this).getLoginToken("")
        setData()
        apiCartDetail()
        addressDetailApi()
        mBinding.llProcessToPay.setOnClickListener {
            if (paymentType!=null){

                processOrder()
            }else
                showToast("Please select PaymentType")

        }

        mBinding.rlFlutterwave.setOnClickListener {

            mBinding.ivFlutterActive.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.radio_active
                    )
                )
            mBinding.ivPayStackActive.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.radio
                    )
                )
            paymentType="flutterwave"
        }
        mBinding.rlPayStack.setOnClickListener {
            mBinding.ivFlutterActive.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.radio
                )
            )
            mBinding.ivPayStackActive.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.radio_active
                )
            )
            paymentType="PAYSTACK"
        }
        mBinding.ivBack.setOnClickListener {
            finish()
        }
    }
    private fun setData(){
        val b = intent.extras
         shippingRate= b!!.getDouble("shippingRate")
         totalAmount= b.getDouble("totalAmount")

        mBinding.tvSubtotal.text = totalAmount.toString()
        mBinding.tvShippingCost.text = shippingRate.toString()
        totalAmount = (totalAmount + shippingRate)

        mBinding.tvTotalAmount.text = (totalAmount).toString()
    }
    private fun apiCartDetail() {
        mViewModel.getCardDetailsByIdParam(
            token!!, SharaGoPref.getInstance(this).getCartId("").toString()
        )
        //user Server Product List result
        mViewModel.lGetCardDetailsParam.observe(this) { it ->
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

                        } else {
                            cartList.clear()

                        }

                    }
                }
                is ExceptionHandler.Error -> {
                    //  Utility.toastMessage(this, it.errorMessage)
                }
            }
        }

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
            finalJson.addProperty("paymentMethod",  paymentType)
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
            orderProcessResult(finalJson)

        } else {
            showToast("Cart is Empty")
        }

    }

    private fun orderProcessResult(finalJson: JsonObject) {
        mViewModel.processOrderParam(token!!, finalJson)
        mViewModel.lProcessOrder.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {

                    mViewModel.lProcessOrder.removeObservers(this);
                    lifecycleScope.launch {
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
                                   var   accessCode = jsonObject.optString("accessCode")
                                   //   Log.e("publicKey", publicKey.toString())
                                //showToast("jawed")

                                if(paymentType.equals("PAYSTACK")){
                                       startActivity(Intent(this@ProcessOrderActivity, PaymentActivity::class.java).putExtra("accessCode",accessCode))

                                }else{
                                    paymentGatewaySetUp(totalAmount, "USD", txRef, "fbfbf")

                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        }
                    }

                }
                is ExceptionHandler.Error -> {
                    mViewModel.lProcessOrder.removeObservers(this);
                    lifecycleScope.launch {
                        showToast(it.errorMessage)
                        dismissLoader()
                    }

                }
            }
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
    private fun apiHitResultVerify(transactionRef: String, transaction_id: String, cartId: String) {

        mViewModel.verifyPaymentParam(transactionRef, transaction_id, cartId)
        mViewModel.lverifyPayment.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()

                    it.data?.let {
                        Log.d("PaymentVerify", it.toString())
                        mViewModel.getCardDetailsUser(token!!)
                        mViewModel.lCartDetailsUser.observe(this){
                                it ->

                            when(it){
                                is ExceptionHandler.Loading->{
                                    dismissLoader()
                                }
                                is ExceptionHandler.Success->{
                                    dismissLoader()
                                    mViewModel.lMultimediaProducts.removeObservers(this);
                                    lifecycleScope.launch{
                                        it.data?.let {
                                            Log.e("cartDetails",it.toString())
                                            SharaGoPref.getInstance(this@ProcessOrderActivity).setCartId(it.cartId.toString())
                                        }
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

    fun addressDetailApi(){
        mViewModel.userDetailByTokenParam(
            SharaGoPref.getInstance(this).getLoginToken("").toString()
        )
        mViewModel.luserDetailByTokenReqParam.observe(this) { it ->
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

                                mBinding.tvManageAdd.text =
                                    getString(R.string.manage)
                                address = getString(R.string.manage)
                                mBinding.llAddres.visibility = View.VISIBLE
                                /*activityUserAddtocartBinding.edtDefaultAddress.visibility =
                                    View.GONE*/
                                mBinding.ivNoAddrss.visibility = View.GONE
                                mBinding.tvName.text =
                                    it.address.metaData!!.firstName + " " + it.address.metaData.lastName.toString()
                                mBinding.tvAddress.text =
                                    it.address.metaData.streetAddress.toString() + ", " + it.address.metaData.city + ", " + it.address.metaData.country

                                addresEdit = it.address as AddressListItem
                                addressId = it.address.id
                            } else {
                                mBinding.llAddres.visibility = View.GONE

                                mBinding.ivNoAddrss.visibility = View.VISIBLE
                                mBinding.tvManageAdd.text =
                                    getString(R.string.add)
                                address = getString(R.string.add)
                            }
                        } catch (e: Exception) {
                            mBinding.llAddres.visibility = View.GONE
                            mBinding.ivNoAddrss.visibility = View.VISIBLE
                        }


                    }

                }
                is ExceptionHandler.Error -> {

                }
            }
        }

    }



    override fun onClick(p0: View?) {

    }
}
