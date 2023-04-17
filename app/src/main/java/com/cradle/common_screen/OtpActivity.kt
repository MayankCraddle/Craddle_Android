package com.cradle.common_screen
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityVerifyOtpBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.user.userActivity.UserForgotPasswordActivity
import com.cradle.user.userActivity.UserMainActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_verify_otp.*
import org.json.JSONObject


class OtpActivity:BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityVerifyOtpBinding
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var sharaGoPref: SharaGoPref? = null
    private var progress: ProgressDialog? = null
    private var name : String? = null
    private var otpID : String? = null
    private var encryptedId1 : String? = null
    private var emailMobile : String? = null
    private var mpref: SharaGoPref? = null
    private var isChat = true
    private var resendOtp = true

    private lateinit var otp:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.otpViewModel=mViewModel
        mBinding.setVariable(BR.onOtpClick,this)

        mpref = SharaGoPref.getInstance(this)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null)

                mpref?.setFcmKey(result.toString())
        }
        initUI()
    }

    private fun initUI(){
        val edit =
            arrayOf(mBinding.editOne,mBinding.editTwo,mBinding.editThree,mBinding.editFour,mBinding.editFive,mBinding.editSix)
        mBinding.editOne.addTextChangedListener(GenericTextWatcher(mBinding.editOne, edit))
        mBinding.editTwo.addTextChangedListener(GenericTextWatcher(mBinding.editTwo, edit))
        mBinding.editThree.addTextChangedListener(GenericTextWatcher(mBinding.editThree, edit))
        mBinding.editFour.addTextChangedListener(GenericTextWatcher(mBinding.editFour, edit))
        mBinding.editFive.addTextChangedListener(GenericTextWatcher(mBinding.editFive, edit))
        mBinding.editSix.addTextChangedListener(GenericTextWatcher(mBinding.editSix, edit))


        otp=mBinding.editOne.text.toString()+mBinding.editTwo.text.toString()+mBinding.editThree.text.toString()+mBinding.editFour.text.toString()+mBinding.editFive.text.toString()+mBinding.editSix.text.toString()

        otpID = intent.getStringExtra("otpId")
        signUpApiResult()
        tv_SignUp_user.setOnClickListener(this)
        tv_SignUp_user.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rele_continue_otp->{
                otp=mBinding.editOne.text.toString()+mBinding.editTwo.text.toString()+mBinding.editThree.text.toString()+mBinding.editFour.text.toString()+mBinding.editFive.text.toString()+mBinding.editSix.text.toString()
                if (otp.length==6) {
                    val otpJsonObj = JsonObject()
                   // otpJsonObj.addProperty("otpId", intent.getStringExtra("otpId"))
                    otpJsonObj.addProperty("otpId", otpID)
                        otpJsonObj.addProperty("otp", otp)
                    otpJsonObj.addProperty("channel", "Android")
                    mViewModel.userOtpSignUP(otpJsonObj)
                    showLoader()

                }else {

                    Utility.toastMessage(this@OtpActivity,this.getString(R.string.verify_otp))

                }
            }
            R.id.tv_SignUp_user->{
                if(resendOtp){
                    resendOtpAPi()
                }
            }/* R.id.tv_SignUp_user->{

            }*/ R.id.tv_forgot_pass->{
                startActivity(Intent(this, UserForgotPasswordActivity::class.java))
                finish()
            }
        }
    }

    //user OTP result
    private fun signUpApiResult(){
        mViewModel.liveOtpUserRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    AnalyticsUtils.analyticReport(this,MyConstants.UserRegistered)
                    Log.d("response", it.data!!.toString())
                    val jsonObject = JSONObject(it.data!!.toString())

                    val token = jsonObject.optString("token")
                    Log.e("token",token)
                    mViewModel.getCardDetailsUser(token!!)
                    val userId = jsonObject.optString("userId")
                    SharaGoPref.getInstance(this).setLoginToken(token)
                    Log.e("userId",userId.toString())
                    SharaGoPref.getInstance(this).setUSERID(userId)
                    SharaGoPref.getInstance(this).setColor(-16743602)
                    SharaGoPref.getInstance(this).setCountry("Nigeria")

                    encryptedId1=jsonObject.optString("encryptedId")
                    emailMobile=jsonObject.optString("emailMobile")
                    SharaGoPref.getInstance(this).setEmail(emailMobile!!)
                    SharaGoPref.getInstance(this).setUserName(jsonObject.optString("name"))
                    name=jsonObject.optString("name")
                    Log.e("encryptedId1",encryptedId1.toString())

                    SharaGoPref.getInstance(this).setVideoID(encryptedId1!!)
                    updateUserInfo()
                    /* findIDEmail(emailMobile!!)
                     if (!isChat){
                         initNewUserInfo()
                     }*/
                    SharaGoPref.getInstance(this).setCountryFlag("nigeria.png")
                    startActivity(Intent(this, UserMainActivity::class.java).putExtra("screen","login"))
                    finish()
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
    private fun updateUserInfo(){
        val hashMap = HashMap<String,Any>() //define empty hashmap
        hashMap.put("emailMobile",emailMobile!!)
        hashMap.put("name",name!!)
        hashMap.put("fcmKey",mpref!!.getFcmKey("")!!)
        /*  val newUser = MyUser()
          newUser.emailMobile=emailMobile
          newUser.name=name
          newUser.fcmKey=mpref!!.getFcmKey("")*/
        FirebaseDatabase.getInstance().reference.child("users").child(encryptedId1!!).updateChildren(hashMap).addOnCompleteListener(
            OnCompleteListener {

            })
            ?.addOnFailureListener(OnFailureListener { e -> Log.e("error", "" + e) })
        //  FirebaseDatabase.getInstance().reference.child("users").setValue(newUser)

    }
    private fun timerOtp(){
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.setText("Remaining Time: " + millisUntilFinished / 1000)
                resendOtp=false
                // logic to set the EditText could go here
            }

            override fun onFinish() {
                resendOtp=true
                tvTimer.setText("done!")
            }
        }.start()
    }
}