package com.cradle.user.userActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment.Companion.dismissLoader
import com.cradle.databinding.ActivityCancelOrderBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cancel_order.*
import kotlinx.android.synthetic.main.activity_cancel_order.tvSeller
import kotlinx.android.synthetic.main.activity_cancel_order.tv_price
import kotlinx.android.synthetic.main.activity_cancel_order.tv_product_name
import kotlinx.android.synthetic.main.activity_cancel_order.tv_quantity
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import org.json.JSONObject

class CancelOrderActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityCancelOrderBinding

    private var orderID = ""
    private var productId = ""
    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ActivityCancelOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel = ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        iv_back.setOnClickListener { finish() }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        tv_usertoolbartitle.text = "Cancel Order"
        find()
    }

    private fun find() {
        orderID = intent.getStringExtra("orderId").toString()
        productId = intent.getStringExtra("productId").toString()
        tv_product_name.text = intent.getStringExtra("product_name")
        tv_quantity.text = "Qty: " + intent.getStringExtra("qty")
        tvSeller.text = "Seller: " + intent.getStringExtra("seller")
        tv_price.text = getString(R.string.currency_symbol) + intent.getStringExtra("price")
        radioGroup = findViewById(R.id.radioGroup)
        rlSubmit.setOnClickListener {
            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId

            if (intSelectButton<0){
                showToast("Please Select Reason for Cancellation!")
                return@setOnClickListener
            }
            else if (etReason.text.trim().isEmpty()){
                showToast("Please Enter Reason for Cancellation!")
                return@setOnClickListener
            }
            radioButton = findViewById(intSelectButton)

            val jsonObject = JsonObject()
            jsonObject.addProperty("orderId", orderID)
            jsonObject.addProperty("reason",radioButton.text.toString() )
            jsonObject.addProperty("comment", etReason.text.trim().toString())
            cancelOrderApi(jsonObject)
        }

    }

    private fun cancelOrderApi(jsonObject : JsonObject) {

        mViewModel.cancelOrderParam(
            SharaGoPref.getInstance(this).getLoginToken("")!!,jsonObject
        )
        mViewModel.lCancelOrder.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {

                    try {
                        //Log.e("TrackOrder>>>", it.data!!.toString())
                        if (it.data != null) {
                            val jsonObject = JSONObject(it.data.toString())
                            Log.e("cancelOrder>>>", jsonObject.toString())
                            startActivity(Intent(this@CancelOrderActivity, MyOderActivity::class.java))
                            finish()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                is ExceptionHandler.Error -> {

                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }

}