package com.cradle.user.userActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ForgotPasswordUserBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.forgot_password_user.*
import org.json.JSONObject

class UserForgotPasswordActivity: BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ForgotPasswordUserBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ForgotPasswordUserBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.forgotPassVM = mViewModel
        mBinding.setVariable(BR.forgotClick, this)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.blue_light))
        initUI()
    }

    private fun initUI() {
        rl_forgot_pass.setOnClickListener(this)
        ivBack.setOnClickListener { finish() }
        signUpApiResult()
    }
    //user OTP result
    private fun signUpApiResult(){
        mViewModel.liveserForgotPassRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    val jsonObject = JSONObject(it.data.toString())
                    val otp_id = jsonObject.optString("otpId")
                    startActivity(Intent(this,UserChangePassActivity::class.java).putExtra("otp_id",otp_id))
                       finish()
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rl_forgot_pass -> {
                if (!Utility.isValidEmail(etEmail_forgot_pass.text!!.toString().trim { it <= ' ' })) {
                Utility.toastMessage(this@UserForgotPasswordActivity,
                    resources.getString(R.string.enter_valid_email))
                }else {

                    val forJsonObj = JsonObject()
                    forJsonObj.addProperty("emailMobile", etEmail_forgot_pass.text.toString().trim())

                    mViewModel.userForgotPassVeri(forJsonObj)
                    showLoader()

                }

            }
        }
    }
}
