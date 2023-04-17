package com.cradle.vendor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityVendorLoginBinding
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_vendor_login.*

class VendorLoginActivty : BaseActivity(), View.OnClickListener{

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityVendorLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }
    private fun initUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityVendorLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onOtpClick,this)

        mBinding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, VendorVerifyCodeActivty::class.java))
        }
        onClick()
    }

    private fun onClick() {
        submit_button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.submit_button -> {
                startActivity(Intent(this, VendorMainActivity::class.java))

            }
            R.id.rl_start_selling -> {
             //   Utility.toastMessage(requireActivity(),"Under Development")
            }
        }
    }

    private fun onClickVendorLogin(){
        if (!Utility.isValidEmail(edtMobileNo.text!!.toString().trim { it <= ' ' })) {
            Toast.makeText(
                this@VendorLoginActivty,
                resources.getString(R.string.enter_valid_email),
                Toast.LENGTH_SHORT
            ).show()


        } else if (edt_pass.text!!.toString().trim { it <= ' ' }.length < 7) {
            Toast.makeText(
                this@VendorLoginActivty,
                resources.getString(R.string.enter_pass),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var listDataChild: JsonObject = JsonObject()
            listDataChild!!.addProperty("emailMobile", edtMobileNo.text.toString().trim())
            listDataChild!!.addProperty("password", edt_pass.text.toString().trim())
            listDataChild!!.addProperty("fcmKey", "11111")
            /*listDataChild!!.addProperty("fcmKey", SharaGoPref.getInstance(this).getFcmKey(""))*/
            listDataChild!!.addProperty("channel", "Android")
           // loginApi(listDataChild)

        }
    }
}