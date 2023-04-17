package com.cradle.user.userActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityOrderDetailBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.chat_box_bottom.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import org.json.JSONObject

class OrderDetailActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityOrderDetailBinding
    private var orderState = ""
    private var vendorId = ""
    private var itemId = ""
    private var orderID = ""
    private var productId = ""
    private var imagePath = ""
    private var productName = ""
    private var qty = ""
    private var disCountedPrice = ""
    private var sellar = ""
    private var isRated = false
    private var rating = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)
        AnalyticsUtils.analyticReport(this,MyConstants.OrderDetailScreen)
        findId()
    }

    @SuppressLint("SetTextI18n")
    private fun findId() {

        //tvOrderId.text= "Order State:"+intent.getStringExtra("orderState")

        /*orderID = intent.getStringExtra("orderId").toString()
        productId = intent.getStringExtra("productId").toString()
        tvOrderId.text = "Order Id #" + orderID
        tvTotalOrderPrice.text = "Total Price: " + getString(R.string.currency_symbol) + intent.getStringExtra("totalPrice1")
        tv_product_name.text = intent.getStringExtra("product_name")
        tv_quantity.text = "Qty: " + intent.getStringExtra("qty")
        tvSeller.text = "Seller: " + intent.getStringExtra("seller")
        tvCustomerName.text = intent.getStringExtra("name")
        tvAddress.text = intent.getStringExtra("address")
        tvPhoneNumber.text = intent.getStringExtra("phone_number")
        tvProductQty.text = intent.getStringExtra("qty")
        // tvProductPrice.text=      "$"+intent.getStringExtra("price")
        tvDiscount.text = getString(R.string.currency_symbol) + intent.getStringExtra("price")
        tv_price.text = getString(R.string.currency_symbol) + intent.getStringExtra("price")
        orderState = intent.getStringExtra("status").toString()
        vendorId = intent.getStringExtra("vendorId").toString()
        itemId = intent.getStringExtra("itemId").toString()
        rating = intent.getFloatExtra("rating", 0.0f)
        isRated = intent.getBooleanExtra("isRated", false)
        intent.getStringExtra("phone_number")
        tvCustomerNameShiping.text = intent.getStringExtra("shippingRate")

        var image = intent.getStringExtra("product_image")
        Glide.with(this).load(MyConstants.file_Base_URL + image)
            .placeholder(R.drawable.loading).error(R.drawable.loading).into(iv_product_item)*/
        orderDetailApi()
        /*if (orderState == "Delivered" && isRated) {
            mBinding.rBarProductDetails.visibility = View.VISIBLE
            mBinding.rBarProductDetails.rating = rating
            tvRating.text = "You already Rated this product."
        } else if (orderState == "Delivered" && !isRated) {
            mBinding.rBarProductDetails.visibility = View.VISIBLE
            tvRating.text = "Write your Review."
        } else {
            mBinding.llRating.visibility = View.GONE
            mBinding.rBarProductDetails.visibility = View.GONE
        }*/
        llRating.setOnClickListener {
            if (orderState == "Delivered" && !isRated) {
                startActivity(
                    Intent(this, UserWriteReviewActivity::class.java)
                        .putExtra("item_id", itemId)
                        .putExtra("orderId", orderID)
                )
            } else {
                showToast("You already Rated this product.")
            }
        }
        iv_back.setOnClickListener { finish() }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        tv_usertoolbartitle.text = "Order Details"
        trackOrderApi()
        cancelOrder()
        ivOpenProductDetail.setOnClickListener {
            val i = Intent(this, UserProductDetailsActivity::class.java)
            i.putExtra("item_id", itemId)
            i.putExtra("product_id", productId)
            startActivity(i)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun orderDetailApi() {

        mViewModel.ordDetailParam(
            SharaGoPref.getInstance(this).getLoginToken("")!!, intent.getStringExtra("orderId")!!
        )
        mViewModel.lOrdDetails.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()

                    try {
                        it.data.let {
                            it!!.addressMetaData!!.firstName
                            tvOrderId.text = "Order Id #" + it.orderId
                            val image = it.cartMetaData!!.items!!.get(0)!!.metaData!!.images

                            if (image!!.isNotEmpty()) {
                                imagePath = image[0].toString()
                                Glide.with(this).load(MyConstants.file_Base_URL + imagePath)
                                    .placeholder(R.drawable.loading).error(R.drawable.loading)
                                    .into(iv_product_item)
                            }
                            productId = it.cartMetaData.items!!.get(0)!!.productId!!
                            orderID = it.orderId.toString()
                            isRated = it.isRated == false
                            orderState = it.orderState.toString()
                            rating = it.rating!!.toFloat()
                            itemId = it?.cartMetaData!!.items!!.get(0)!!.itemId.toString()
                            qty = it.cartMetaData.items.get(0)!!.quantity.toString()
                            productName = it.cartMetaData.items.get(0)!!.name.toString()
                            sellar =
                                it.vendorMetadata!!.firstName + " " + it.vendorMetadata.lastName
                            disCountedPrice =
                                it.cartMetaData.items.get(0)!!.discountedPrice.toString()

                            tv_quantity.text =
                                "Qty: " + qty
                            tv_product_name.text = productName
                            tvSeller.text =
                                "Seller: " + sellar
                            tvCustomerName.text = it.addressMetaData!!.firstName
                            tvAddress.text =
                                it.addressMetaData.streetAddress + "," + it.addressMetaData.landmark + ","
                            tvCity.text =
                                it.addressMetaData.city + "," + it.addressMetaData.state + ","
                            tvPhoneNumber.text = "Mobile:" + " " + it.addressMetaData.phone + ","
                            tvCity.text =
                                it.addressMetaData.city + "," + it.addressMetaData.state + ","
                            tvProductQty.text = getString(R.string.currency_symbol) + it.subTotal
                            tvCustomerNameShiping.text =
                                getString(R.string.currency_symbol) + it.shippingRate
                            tvDiscount.text =
                                getString(R.string.currency_symbol) + it.cartMetaData.items.get(0)!!.discountedPrice.toString()
                            tvTotalOrderPrice.text =
                                getString(R.string.currency_symbol) + it.cartMetaData.items.get(0)!!.totalPrice.toString()
                            tv_price.text =
                                getString(R.string.currency_symbol) + it.cartMetaData.items.get(0)!!.discountedPrice.toString()
                            if (it.orderState.equals(
                                    "created",
                                    ignoreCase = true
                                ) || it.orderState.equals("payment_completed", ignoreCase = true)
                                || it.orderState.equals(
                                    "Confirmed",
                                    ignoreCase = true
                                ) || it.orderState.equals("Packed", ignoreCase = true)
                            ) {
                                llCancelOrder.visibility = View.VISIBLE
                            } else {
                                llCancelOrder.visibility = View.GONE
                            }
                            if (orderState == "Delivered" && isRated) {
                                mBinding.rBarProductDetails.visibility = View.VISIBLE
                                mBinding.rBarProductDetails.rating = rating
                                tvRating.text = "You already Rated this product."
                            } else if (orderState == "Delivered" && !isRated) {
                                mBinding.rBarProductDetails.visibility = View.VISIBLE
                                tvRating.text = "Write your Review."
                            } else {
                                mBinding.llRating.visibility = View.GONE
                                mBinding.rBarProductDetails.visibility = View.GONE
                            }
                        }
                        //Log.e("TrackOrder>>>", it.data!!.toString())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                   // Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }

    private fun trackOrderApi() {
        mViewModel.trackOrderApi(
            SharaGoPref.getInstance(this).getLoginToken("")!!, intent.getStringExtra("orderId")!!
        )
        mViewModel.ltrackOrd.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    try {
                        //Log.e("TrackOrder>>>", it.data!!.toString())
                        if (it.data != null) {
                            val jsonObject = JSONObject(it.data.toString())
                            Log.e("trackList", jsonObject.toString())
                            val tracklist = jsonObject.optJSONArray("trackDetailList")
                            for (i in 0..tracklist.length()) {
                                var state =
                                    (tracklist.get(i) as JSONObject).optString("state").toString()
                                var date =
                                    (tracklist.get(i) as JSONObject).optString("updatedOn")
                                        .toString()

                                /*if (state.equals("created")) {
                                    llCancelOrder.visibility = View.VISIBLE
                                } else if (state.equals("payment_completed")) {
                                    llCancelOrder.visibility = View.VISIBLE
                                } else*/ if (state.equals("Confirmed")) {
                                    viewConfirmedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewConfirmedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    tvCreateOn.text = date
                                    //llCancelOrder.visibility = View.VISIBLE
                                } else if (state.equals("Packed")) {
                                    viewConfirmedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewConfirmedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    tvPackedDate.text = date
                                    // llCancelOrder.visibility = View.VISIBLE
                                } else if (state.equals("Shipped")) {
                                    viewConfirmedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewConfirmedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewShippedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    tvShippedDate.text = date
                                    llCancelOrder.visibility = View.GONE

                                } else if (state.equals("Delivered")) {
                                    viewConfirmedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewConfirmedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewShippedDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewPackedTrack.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    viewDeliveredDot.background =
                                        resources.getDrawable(R.drawable.shap_circel)
                                    tv_deliveredDate.text = date
                                    llCancelOrder.visibility = View.GONE

                                } /*else if (state.equals("Cancelled")) {
                                    Log.e("cancel",state.toString())
                                    llCancelOrder.visibility = View.GONE
                                    tvCancelOrder.setTextColor(resources.getColor(R.color.colour_red))
                                    llCancelOrder.isClickable = false
                                }*/
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

    private fun cancelOrder() {
        llCancelOrder.setOnClickListener {
            var i = Intent(this, CancelOrderActivity::class.java)
            i.putExtra("orderId", orderID)
            i.putExtra("productId", productId)
            i.putExtra("product_name", productName)
            i.putExtra("seller", sellar)
            i.putExtra("qty", qty)
            i.putExtra("price", disCountedPrice)
            startActivity(i)

        }
    }

    override fun onClick(p0: View?) {

    }

}