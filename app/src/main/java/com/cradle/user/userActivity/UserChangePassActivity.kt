package com.cradle.user.userActivity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ChangePasswordActivityBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.change_password_activity.*
import org.json.JSONObject


class UserChangePassActivity: BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ChangePasswordActivityBinding
    private var otpID : String? = null
    private var resendOtp = true


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ChangePasswordActivityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
      //  mBinding.forgotPassVM = mViewModel
        mBinding.setVariable(BR.onClickChangePass, this)

        initUI()
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.blue_light))
    }

    private fun initUI() {
        otpID=intent.getStringExtra("otp_id")
        changePassApiResult()
        tv_SignUp_user.setOnClickListener(this)
        stringColourChangeActive()
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_chang_pass->{
                onClickPassChage()
            }
            R.id.tv_SignUp_user->{
                if(resendOtp){
                    stringColourChange()
                 //   mBinding.tvSignUpUser.text=getString(R.string.don_t_receive_the_otp_resend_otp_new)
                    tvTimer.visibility=View.VISIBLE
                    resendOtpAPi()
                }
            }
        }
    }
    private fun stringColourChange(){
        val builder = SpannableStringBuilder()
        val red = "Don't receive the Otp?"
        val redSpannable = SpannableString(red)
        redSpannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, red.length, 0)

        val white = "RESEND OTP"
        val whiteSpannable = SpannableString(white)
        whiteSpannable.setSpan(ForegroundColorSpan(Color.GRAY), 0, white.length, 0)
        builder.append(redSpannable)
        builder.append(whiteSpannable)
        mBinding.tvSignUpUser.setText(builder, TextView.BufferType.SPANNABLE);

    }
    private fun stringColourChangeActive(){
        val builder = SpannableStringBuilder()
        val red = "Don't receive the Otp?"
        val redSpannable = SpannableString(red)
        redSpannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, red.length, 0)

        val white = "RESEND OTP"
        val whiteSpannable = SpannableString(white)
        whiteSpannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, white.length, 0)
        builder.append(redSpannable)
        builder.append(whiteSpannable)
        mBinding.tvSignUpUser.setText(builder, TextView.BufferType.SPANNABLE);

    }
    private fun resendOtpAPi(){
        val otpJsonObj = JsonObject()
        otpJsonObj.addProperty("otpId", otpID)
        mViewModel.resendOtpReq(otpJsonObj)
        // showLoader()
        timerOtp()
        mViewModel.lResendOtpRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    /* {
                         "otpId" : "11qohOt3qm5YlaTFeutSqCoUXfSqeNyh8","message":"otp resent successfully!"
                     }*/
                    Log.d("response", it.data!!.toString())
                    val jsonObject = JSONObject(it.data.toString())

                    otpID = jsonObject.optString("otpId")
                    val message = jsonObject.optString("message")
                    //   Utility.toastMessage(this,"Otp resent successfully!")


                    /*   startActivity(Intent(this, UserLoginActivity::class.java))
                       finish()*/
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    //user Change Pass result
    private fun changePassApiResult(){
        mViewModel.lUserCPssRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    Utility.toastMessage(this,getString(R.string.sussfully_change_password))
                    startActivity(Intent(this,UserLoginActivity::class.java))
                    finish()
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }
    
    //onClick on Change Password
    private fun onClickPassChage(){
        if(et_otp_id_user.text.toString().trim().isEmpty())
            Utility.toastMessage(this,getString(R.string.enter_otp))
        else if(et_new_password_user.text.toString().trim().isEmpty())
            Utility.toastMessage(this,getString(R.string.enter_new_pasword))
        else if (!Utility.isValidPass(et_new_password_user.text.toString().trim().trim { it <= ' ' }))
            showToast(getString(R.string.pass_validation))
        else if(et_confrim_password_user.text.toString().trim().isEmpty())
            Utility.toastMessage(this,getString(R.string.enter_confrim_new_pasword))
         else if(et_confrim_password_user.text.toString().trim() != et_new_password_user.text.toString().trim())
            Utility.toastMessage(this,getString(R.string.enter_match_password))
        else{
            val changePass = JsonObject()
            with(changePass) {
                addProperty("otpId", otpID)
                addProperty("otp", et_otp_id_user.text.toString().trim())
                addProperty("password", et_new_password_user.text.toString().trim())
            }
            showLoader()
            mViewModel.userForgotPass(changePass)

        }
    }
    private fun timerOtp(){
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.setText("Remaining Time: " + millisUntilFinished / 1000)
                resendOtp=false
                // logic to set the EditText could go here
            }

            override fun onFinish() {
                tvTimer.visibility=View.GONE
                stringColourChangeActive()
              //  mBinding.tvSignUpUser.text=getString(R.string.don_t_receive_the_otp_resend_otp)
                resendOtp=true

            }
        }.start()
    }
}
