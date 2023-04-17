package com.cradle.common_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.cradle.BuildConfig
import com.cradle.R
import com.cradle.databinding.ActivitySplashScreenBinding
import com.cradle.user.userActivity.UserMainActivity
import com.cradle.utils.SharaGoPref
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import okhttp3.internal.and
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity:AppCompatActivity(){
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private  var mPref: SharaGoPref?=null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.blue_light))
        setHander()
        firbaseEvent()

  //  haskey=Qofh3wJ8iaPOphdhUiQvHFcLfVU=
    }


    private fun firbaseEvent(){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "id")
            param(FirebaseAnalytics.Param.ITEM_NAME, "Rahul")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setHander() {
        mPref = SharaGoPref.getInstance(this)
        createSha1(this,"SHA1")
        // Add code to print out the key hash
        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                "com.cradle",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (_: PackageManager.NameNotFoundException) {
        } catch (_: NoSuchAlgorithmException) {
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null)

                mPref?.setFcmKey(result.toString())
        }
        Handler(Looper.getMainLooper()).postDelayed({
            SharaGoPref.getInstance(this).setWhichFrag("Home")
            SharaGoPref.getInstance(this).setColor(-16743602)
            SharaGoPref.getInstance(this).setCountry("Nigeria")

            SharaGoPref.getInstance(this).setCountryFlag("nigeria.png")
            SharaGoPref.getInstance(this).setShowList(this.getString(R.string.products))

            startActivity(Intent(this@SplashScreenActivity, UserMainActivity::class.java))
            finish()

          /*   if(SharaGoPref.getInstance(this@SplashScreenActivity).getLoginToken("")!!.isNotEmpty())
             {
                 if(SharaGoPref.getInstance(this).getUserType("")!!.equals(getString(R.string.user))){
                      SharaGoPref.getInstance(this).setWhichFrag("Home")
                startActivity(Intent(this@SplashScreenActivity, UserMainActivity::class.java))
                finish()
                 }else{
                     startActivity(Intent(this@SplashScreenActivity, UserMainActivity::class.java))
                     finish()
                 }

             } else{
                 startActivity(Intent(this@SplashScreenActivity, UserLoginActivity::class.java))
                 finish()
             }*/

        },1000)
    }


    private fun createSha1(context: Context, key: String) {
        try {
            val info: PackageInfo = context.getPackageManager()
                .getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance(key)
                md.update(signature.toByteArray())
                val digest = md.digest()
                val toRet = StringBuilder()
                for (i in digest.indices) {
                    if (i != 0) toRet.append(":")
                    val b: Int = digest[i] and 0xff
                    val hex = Integer.toHexString(b)
                    if (hex.length == 1) toRet.append("0")
                    toRet.append(hex)
                }
                Log.e("Sha1", "$key $toRet")
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }
}