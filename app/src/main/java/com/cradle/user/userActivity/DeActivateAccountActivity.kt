package com.cradle.user.userActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_screen.SplashScreenActivity
import com.cradle.databinding.ActivityDeactivateAccountBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.logout_dialog.*
import org.json.JSONObject

class DeActivateAccountActivity : BaseActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mBinding: ActivityDeactivateAccountBinding
    private var token: String? = null
    private var mpref: SharaGoPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ActivityDeactivateAccountBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)
        AnalyticsUtils.analyticReport(this,MyConstants.AccountDeactivatedScreen)
        findId()
    }

    private fun findId() {
        AnalyticsUtils.analyticReport(this,"DeActivateScreen")

        mpref = SharaGoPref.getInstance(this)
        token= mpref!!.getLoginToken("")

        mBinding.rlToolBar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        if (MyHelper.isNetworkConnected(this)) {
            resultCount()
        } else showToast(getString(R.string.no_internet_connection))
        mBinding.ivBack.setOnClickListener { finish() }

        mBinding.tvConfirmDeActivation.setOnClickListener {
           if(mpref!!.getSocialMediaLoggedIn(false)) {
               deActivationDialog()
           }
            else
           {
               if(mBinding.namePass.text.toString().trim().isNotEmpty())
                   deActivationDialog()
               else
                   showToast(getString(R.string.inter_password))
           }


        }
        mBinding.tvEmailId.text=mpref!!.getEmail("").toString()
        if(mpref!!.getSocialMediaLoggedIn(false)){
            mBinding.namePass.visibility=View.GONE
            mBinding.tvPass.passwordVisibilityToggleRequested(false)
            mBinding.tvPass.visibility=View.GONE

        }else
            mBinding.namePass.visibility=View.VISIBLE
            mBinding.tvPass.visibility=View.VISIBLE

    }

    private fun resultCount(){
        mainViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

        //user Server Product List result
        mainViewModel.lGetCardDetailsParam.observe(this){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {

                        if(it.metaData!!.items!!.isNotEmpty()){
                            mBinding.rlCartCount.visibility= View.VISIBLE
                            mBinding.tvCartCount.text=it.cartSize
                        }else{
                            mBinding.rlCartCount.visibility= View.VISIBLE
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
    private fun deActivationDialog() {

     val  dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.logout_dialog)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
       dialog.tvTitle.text=getString(R.string.deactivated)
        dialog.rlYes.setOnClickListener {
            deActivateAccountApi()
            dialog.dismiss()
        }
        dialog.rlNo.setOnClickListener { dialog.dismiss() }
        dialog.rlLogOut.setOnClickListener { dialog.dismiss()}
        dialog.show()
    }

    private fun deActivateAccountApi(){
     /*   {
            "status":"Deleted/ Deactivated/Active",
            "password":"",
            "comment":""
        }*/
        val deAcJsonObject = JsonObject()
        deAcJsonObject.addProperty("status", "Deactivated")
        if (mpref!!.getSocialMediaLoggedIn(false)){
            deAcJsonObject.addProperty("password", "")
        }else{
            deAcJsonObject.addProperty("password", mBinding.namePass.text.toString().trim())

        }

        deAcJsonObject.addProperty("comment", "")

        Log.e("json",deAcJsonObject.toString()+mpref!!.getLoginToken("")!!)
        Log.e("token",mpref!!.getLoginToken("")!!)


        mainViewModel.changeUserAccountStatusParam(mpref!!.getLoginToken("")!!,deAcJsonObject)

        mainViewModel.lchangeUserAccountStatus.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {

                    try {
                        //Log.e("TrackOrder>>>", it.data!!.toString())
                        if (it.data != null) {
                            val jsonObject = JSONObject(it.data.toString())
                            Log.e("cancelOrder>>>", jsonObject.toString())
                            val msg = jsonObject.optString("message")
                            val mpref=  SharaGoPref.getInstance(this)
                            AnalyticsUtils.analyticReport(this,MyConstants.AccountDeactivated)
                            mpref.clear()
                            showToast(msg)
                            val i = Intent(this, UserMainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)


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