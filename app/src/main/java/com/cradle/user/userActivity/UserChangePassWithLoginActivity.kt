package com.cradle.user.userActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserChangePasswithloginBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_change_passwithlogin.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*


class UserChangePassWithLoginActivity: BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserChangePasswithloginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserChangePasswithloginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        //  mBinding.forgotPassVM = mViewModel
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)

        initUI()
    }

    private fun initUI() {
       mBinding.ivBack.setOnClickListener{
           onBackPressed()
       }
        changePassWithApiResult()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rl_changes_pass -> {
                if(old_pass.text.toString().trim().isEmpty())
                    Utility.toastMessage(this,getString(R.string.enter_old_pass))
                else if(new_pass.text.toString().trim().isEmpty())
                    Utility.toastMessage(this,getString(R.string.enter_new_pasword))
                else if (!Utility.isValidPass(new_pass.text.toString().trim().trim { it <= ' ' }))
                    showToast(getString(R.string.pass_validation))
                else if(confrim_pass.text.toString().trim().isEmpty())
                    Utility.toastMessage(this,getString(R.string.enter_confrim_new_pasword))
                else if(confrim_pass.text.toString().trim() != new_pass.text.toString().trim())
                    Utility.toastMessage(this,getString(R.string.enter_match_password))

                else{
                    val listDataChild = JsonObject()
                    listDataChild.addProperty("oldPassword", old_pass.text.toString().trim())
                    listDataChild.addProperty("newPassword", new_pass.text.toString().trim())
                    mViewModel.userChangePassWithLogin(SharaGoPref.getInstance(this).getLoginToken("")!!,listDataChild)
                    showLoader()
                }
            }
        }
    }
    //user OTP result
    private fun changePassWithApiResult(){
        mViewModel.lUserChagePassWithLoginRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    Log.e(MyConstants.TAG,it.data.toString())
                //    val jsonObject = JSONObject(it.data.toString())
                    finish()
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
