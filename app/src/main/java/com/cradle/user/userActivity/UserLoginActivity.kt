package com.cradle.user.userActivity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserLoginBinding
import com.cradle.firebasechat.model.MyUser
import com.cradle.intarfaces.LogInHandler
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_login.*
import kotlinx.android.synthetic.main.logout_dialog.*
import org.json.JSONObject
import java.net.URL
import java.util.*


class UserLoginActivity:BaseActivity(), LogInHandler, View.OnClickListener {

    private val RC_SIGN_IN = 9001
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var userLoginViewModel: MainViewModel
    private lateinit var activityUserLoginBinding: ActivityUserLoginBinding
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var name : String? = null
    private var encryptedId1 : String? = null
    private var emailMobile : String? = null
    private var socialToken : String? = null
    private var mpref: SharaGoPref? = null
    private var callbackManager: CallbackManager? = null


    private var nameNew: String = ""
    private var facebookId =""
    private var googleId=""
    private var image: String = ""
    private var emailId: String = ""
    private var type = ""
    private var mGoogleSignInClient: GoogleSignInClient? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiinitialise()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun uiinitialise() {
        val response = (application as ApplicationClass).repository
        activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login)
        userLoginViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        activityUserLoginBinding.viewModel=userLoginViewModel
        activityUserLoginBinding.handler=this
        activityUserLoginBinding.setVariable(BR.onLogInClick,this)

        AnalyticsUtils.analyticReport(this,MyConstants.LoginScreen)
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(application);
        firebaseAnalytics = Firebase.analytics
        onClickButtonResult()
        mpref = SharaGoPref.getInstance(this)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null)

                mpref?.setFcmKey(result.toString())
        }
        ivBack.setOnClickListener {
            onBackPressed()

        }

        initFirebase()
        loginApiResult()
        initGoogleSignIn()
        initFacebook()
        setObserver()
        clickOnGoogle()
        statusBarColourChange()

    }



    // Watching for button click result
    private  fun onClickButtonResult(){
        userLoginViewModel.getLogInResult().observe(this, Observer { result ->
            if (result.equals("go")){
                jsonCreateApiHit()
            }else{
                Utility.toastMessage(this,result)
            }
        })



    }


    //api heat user login
    private fun jsonCreateApiHit(){
        if (MyHelper.isNetworkConnected(this)) {
            val loginJsonObject = JsonObject()
            loginJsonObject.addProperty("emailMobile", name_edit_text.text.toString().trim())
            loginJsonObject.addProperty("password", name_pass.text.toString().trim())
            loginJsonObject.addProperty("fcmKey", mpref!!.getFcmKey(""))
            loginJsonObject.addProperty("channel", "Android")
            Log.e("json",loginJsonObject.toString())
            showLoader()
            userLoginViewModel.userLogin(loginJsonObject)

        } else showToast(getString(R.string.no_internet_connection))




    }


    //facebook login
    private fun setObserver() {
        activityUserLoginBinding.rlFb.setOnClickListener {   this.type = "facebook"
            activityUserLoginBinding.ivFbLogo.visibility = View.GONE
            activityUserLoginBinding.tvFb.visibility = View.GONE
            activityUserLoginBinding.progressFb.visibility = View.VISIBLE
            facebookLogin()
        }
    }
    fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }
    //Facebook
    fun initFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                   // showToast("cancel")
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    //showToast("error")
                    // App code
                }
            })
    }

    //google login
    private fun clickOnGoogle(){
        activityUserLoginBinding.rlGoogle.setOnClickListener {
            this.type = "google"
            activityUserLoginBinding.ivLogo.visibility = View.GONE
            activityUserLoginBinding.tvGoogle.visibility = View.GONE
            activityUserLoginBinding.progressGoogle.visibility = View.VISIBLE
            googleLogin()
        }
    }
    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener { Log.e("Signout", "Completed") }
    }
    private fun googleLogin() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        notValidLogin()
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val email = account!!.email

            socialToken = ""
            Log.e("socialToken",account.idToken.toString())
            val id = account.id
            val name = account.displayName
            // String imageData = "" + account.getPhotoUrl();

            val imageData = if (account.photoUrl != null) account.photoUrl.toString() else ""
            if (email != null) {
                emailId = email
            }
            if (id != null) {
                googleId = id
            }
            var userName = name
            image = imageData
            //Logger.e("id---"+""+googleId);
            //Logger.e("google_name--+"+name);
            //    Logger.e("google_image---" + image)
            val loginJsonObject = JsonObject()
            loginJsonObject.addProperty("email", emailId)
            loginJsonObject.addProperty("mobile", "")
            loginJsonObject.addProperty("country", "")
            loginJsonObject.addProperty("firstName", name)
            loginJsonObject.addProperty("lastName", "")
            loginJsonObject.addProperty("fcmKey", mpref!!.getFcmKey(""))
            loginJsonObject.addProperty("channel", "Android")
            Log.e("json",loginJsonObject.toString())
            loginRequest(loginJsonObject)
        } catch (e: ApiException) {
            Log.e("sssiiigggexception", e.toString())
            //Log.d("signInResult:failed code=" , e.getStatusCode());
        }
    }
    fun notValidLogin() {

        activityUserLoginBinding.ivFbLogo.visibility = View.VISIBLE
        activityUserLoginBinding.tvFb.visibility = View.VISIBLE
        activityUserLoginBinding.progressFb.visibility = View.INVISIBLE


        activityUserLoginBinding.ivLogo.visibility = View.VISIBLE
        activityUserLoginBinding.tvGoogle.visibility = View.VISIBLE
        activityUserLoginBinding.progressGoogle.visibility = View.INVISIBLE
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val request = GraphRequest.newMeRequest(
            token
        ) { `object`, response ->
            try {
                socialToken = token.toString()
                Log.e("FbResultResponse",`object`.toString())
                val facebookId = `object`!!.getString("id")
                name = `object`.getString("name")

                name = `object`.getString("name")
                var email = ""
                var imageUrl = ""
                try {
                    name = `object`.getString("name")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    email = `object`.getString("email")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val imgUrl =
                        URL("https://graph.facebook.com/$facebookId/picture?type=large")
                    imageUrl = imgUrl.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                emailId = email
                this.facebookId = facebookId
                image = imageUrl
                Log.e("facebookId----", "" + facebookId)
                Log.e("name", "  " + email + " " + imageUrl)

                /*  {
                      "email":"",
                      "mobile":"",
                      "country":"",
                      "firstName":"",
                      "lastName":"",
                      "fcmKey":"",
                      "channel":""
                  }*/
                val loginJsonObject = JsonObject()
                loginJsonObject.addProperty("email", emailId)
                loginJsonObject.addProperty("mobile", "")
                loginJsonObject.addProperty("country", "")
                loginJsonObject.addProperty("firstName", name)
                loginJsonObject.addProperty("lastName", "")
                loginJsonObject.addProperty("fcmKey", mpref!!.getFcmKey(""))
                loginJsonObject.addProperty("channel", "Android")
                Log.e("json",loginJsonObject.toString())
                //   showLoader()

                loginRequest(loginJsonObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture")
        request.parameters = parameters
        request.executeAsync()
    }

    //user login result
    private fun loginApiResult(){
        userLoginViewModel.liveDataUserLoginRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    Log.d("response", it.data!!.toString())
                    val jsonObject = JSONObject(it.data.toString())
                    val token = jsonObject.optString("token")
                    val deactivated = jsonObject.optBoolean("deactivated")
                    SharaGoPref.getInstance(this).setLoginToken(token)
                    if(!deactivated){
                        AnalyticsUtils.analyticReport(this,MyConstants.UserLoggedIn)
                        Log.e("token",token)
                        userLoginViewModel.getCardDetailsUser(token)
                        val userId = jsonObject.optString("userId")

                        Log.e("userId",userId.toString())
                        SharaGoPref.getInstance(this).setUSERID(userId)
                        SharaGoPref.getInstance(this).setColor(-16743602)
                        SharaGoPref.getInstance(this).setCountry("Nigeria")

                        encryptedId1=jsonObject.optString("encryptedId")
                        emailMobile=jsonObject.optString("emailMobile")
                        SharaGoPref.getInstance(this).setEmail(emailMobile!!)
                        SharaGoPref.getInstance(this).setServerKey(jsonObject.optString("serverKey"))
                        SharaGoPref.getInstance(this).setUserName(jsonObject.optString("name"))
                        name=jsonObject.optString("name")
                        Log.e("encryptedId1",encryptedId1.toString())

                        SharaGoPref.getInstance(this).setEncrypetedkey(encryptedId1!!)
                        Log.e("getEncryptedId",SharaGoPref.getInstance(this).getEncrypetedkey("").toString())

                        updateUserInfo()
                        SharaGoPref.getInstance(this).setCountryFlag("nigeria.png")
                        //   startActivity(Intent(this, UserMainActivity::class.java).putExtra("screen","login"))
                        onBackPressed()
                    }else{
                        deActivationDialog()
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }

        userLoginViewModel.lCartDetailsUser.observe(this){
                it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        Log.e("cartDetails",it.toString())
                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
    }


    override fun onLogInClicked() {
        userLoginViewModel.performValidation(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_signUpOpen->{
           startActivity(Intent(this,UserSignUpActivity::class.java))
            } R.id.tv_forgot_pass->{
           startActivity(Intent(this,UserForgotPasswordActivity::class.java))
            }
    }
    }
    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Utility.UID = user?.uid.toString()
                // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid())
            } else {
                // Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    private fun updateUserInfo(){
        val hashMap = HashMap<String,Any>() //define empty hashmap
        hashMap.put("emailMobile",emailMobile!!)
        hashMap.put("name",name!!)
        hashMap.put("fcmKey",mpref!!.getFcmKey("")!!)

        FirebaseDatabase.getInstance().reference.child("users").child(encryptedId1!!).updateChildren(hashMap).addOnCompleteListener(
            OnCompleteListener {

            })
            .addOnFailureListener(OnFailureListener { e -> Log.e("error", "" + e) })
        //  FirebaseDatabase.getInstance().reference.child("users").setValue(newUser)

    }

    private  fun initNewUserInfo() {
        val newUser = MyUser()
        /* newUser.email = user?.email
         newUser.name = name*/
        newUser.emailMobile=emailMobile
        newUser.name=name
        newUser.fcmKey=mpref!!.getFcmKey("")

        Log.e("newuser",newUser.toString())
        FirebaseDatabase.getInstance().reference.child("users/" + encryptedId1).setValue(newUser)
        //  FirebaseDatabase.getInstance().reference.child("users/" + user?.uid).setValue(newUser)
        //  saveUserInfo()
     //   findIDEmail(emailMobile!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }






    private fun loginRequest(jsonObject: JsonObject) {
        userLoginViewModel.loginWithSocialMediaParam(socialToken!!,jsonObject)
        userLoginViewModel.lFbLogin.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    Log.d("Fbresponse", it.data!!.toString())
                    val jsonObject = JSONObject(it.data.toString())

                    val token = jsonObject.optString("token")
                    val socialMediaLoggedIn = jsonObject.optBoolean("socialMediaLoggedIn")
                    SharaGoPref.getInstance(this).setSocialMediaLoggedIn(socialMediaLoggedIn)
                    Log.e("socialMediaLoggedIn",socialMediaLoggedIn.toString())
                    Log.e("token",token)
                    userLoginViewModel.getCardDetailsUser(token)
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

                    SharaGoPref.getInstance(this).setEncrypetedkey(encryptedId1!!)
                    Log.e("getEncryptedId",SharaGoPref.getInstance(this).getEncrypetedkey("").toString())

                    updateUserInfo()
                    SharaGoPref.getInstance(this).setCountryFlag("nigeria.png")
                       startActivity(Intent(this, UserMainActivity::class.java).putExtra("screen","login"))
                    finish()

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }

    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.blue_light))
    }

    //reset account
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
           dialog.tvTitle.text=getString(R.string.your_account)

        dialog.rlYes.setOnClickListener {
            deActivateAccountApi()
            dialog.dismiss()
        }
        dialog.rlNo.setOnClickListener { dialog.dismiss() }

        dialog.rlLogOut.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun deActivateAccountApi(){
        /*   {
               "status":"Deleted/ Deactivated/Active",
               "password":"",
               "comment":""
           }*/
        val deAcJsonObject = JsonObject()
        deAcJsonObject.addProperty("status", "Active")
        deAcJsonObject.addProperty("password", name_pass.text.toString().trim())
        deAcJsonObject.addProperty("comment", "")
        Log.e("json",deAcJsonObject.toString())
        userLoginViewModel.changeUserAccountStatusParam(mpref!!.getLoginToken("")!!,deAcJsonObject)

        userLoginViewModel.lchangeUserAccountStatus.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {

                    try {
                        //Log.e("TrackOrder>>>", it.data!!.toString())
                        if (it.data != null) {
                            AnalyticsUtils.analyticReport(this,MyConstants.AccountReactivated)
                            val jsonObject = JSONObject(it.data.toString())
                           jsonCreateApiHit()
                            loginApiResult()
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