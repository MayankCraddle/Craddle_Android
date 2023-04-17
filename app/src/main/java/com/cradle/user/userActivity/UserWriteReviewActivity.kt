package com.cradle.user.userActivity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserWriteReviewBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_write_review.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import org.json.JSONObject


class UserWriteReviewActivity: BaseActivity(),View.OnClickListener {

    private var pref: SharaGoPref? = null
    private lateinit var mBinding: ActivityUserWriteReviewBinding
    private lateinit var mViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseUI()
    }
    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserWriteReviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)

        editRatingDis.setFilters(arrayOf<InputFilter>(LengthFilter(50)))

        editRatingDis.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length> 40) showToast("finsh test limit")
            }
        })

        pref = SharaGoPref.getInstance(this)
        rele_notNowRating.setOnClickListener(this)
        rele_submitRating.setOnClickListener(this)
        setCustomToolBar()
        apiResult()
        statusBarColourChange()
    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    private fun setCustomToolBar() {
        iv_back.setOnClickListener { finish() }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)


    }
    private fun apiResult(){
        mViewModel.lgiveRating.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    Log.d("response", it.data!!.toString())
                    val jsonObject = JSONObject(it.data.toString())
                    showToast(jsonObject.optString("message").toString())
                    finish()
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }
    private fun onClickRating(){
        val giveRating = JsonObject()
        giveRating.addProperty("fromId", pref!!.getUSERID(""))
        giveRating.addProperty("toId", intent.getStringExtra("item_id"))
        giveRating.addProperty("orderId", intent.getStringExtra("orderId"))
        giveRating.addProperty("title", edit_subject.text.toString())
        giveRating.addProperty("rating", rBar.rating.toString())
        giveRating.addProperty("review", editRatingDis.text.toString())
        //loginApi(listDataChild)
        mViewModel.userGiveRatingParam(pref!!.getLoginToken("").toString(),giveRating)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rele_submitRating -> {
                onClickRating()
            }
            R.id.rele_notNowRating -> {
                finish()
            }
        }
    }
}