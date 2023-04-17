package com.cradle.paystack_payment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.exceptions.ExpiredAccessCodeException
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.cradle.BR
import com.cradle.BuildConfig
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment.Companion.dismissLoader
import com.cradle.databinding.ActivityPaymentBinding
import com.cradle.databinding.ActivityUserAddtocartBinding
import com.cradle.repository.ExceptionHandler

import com.cradle.repository.QuoteRepository
import com.cradle.user.userActivity.PaymentSuccessActivity
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import org.json.JSONException
import java.util.*

class PaymentActivity:AppCompatActivity() {
    var backend_url = "https://charge-sample-service.onrender.com"

    // Set this to a public key that matches the secret key you supplied while creating the heroku instance
    var paystack_public_key = "pk_test_1dfd4affcb0a1863c168a220cf8523d91c8ba7af"

    var mEditCardNum: EditText? = null
    var mEditCVC: EditText? = null
    var mEditExpiryMonth: EditText? = null
    var mEditExpiryYear: EditText? = null

    var mTextError: TextView? = null
    var mTextBackendMessage: TextView? = null

    var dialog: ProgressDialog? = null
    private var mTextReference: TextView? = null
    private var ivBack: ImageView? = null
    private var charge: Charge? = null
    private var transaction: Transaction? = null
    private val TAG = "PaymentActivity"

    private lateinit var response: QuoteRepository
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        response = (application as ApplicationClass).repository

        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_payment)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
       // mBinding.mainViewModel = userAddToCartViewModel
        mBinding.setVariable(BR.onAddToCartClick, this)

        inisilize()

    }

    private fun inisilize() {


        if (BuildConfig.DEBUG && backend_url == "") {
            throw AssertionError("Please set a backend url before running the sample")
        }
        if (BuildConfig.DEBUG && paystack_public_key == "") {
            throw AssertionError("Please set a public key before running the sample")
        }

        PaystackSdk.setPublicKey(paystack_public_key)

        mEditCardNum = findViewById(R.id.edit_card_number)
        mEditCVC = findViewById(R.id.edit_cvc)
        mEditExpiryMonth = findViewById(R.id.edit_expiry_month)
        mEditExpiryYear = findViewById(R.id.edit_expiry_year)

        val mButtonPerformTransaction = findViewById<Button>(R.id.button_perform_transaction)
        val mButtonPerformLocalTransaction =
            findViewById<Button>(R.id.button_perform_local_transaction)

        mTextError = findViewById(R.id.textview_error)
        mTextBackendMessage = findViewById(R.id.textview_backend_message)
        mTextReference = findViewById(R.id.textview_reference)
        ivBack = findViewById(R.id.iv_back)
        ivBack!!.setOnClickListener {
            finish()
        }

        //initialize sdk

        //initialize sdk
        PaystackSdk.initialize(applicationContext)

        //set click listener

        //set click listener
        mButtonPerformTransaction.setOnClickListener {
            try {
                startAFreshCharge(false)
            } catch (e: Exception) {
                mTextError!!.setText(
                    String.format(
                        "An error occurred while charging card: %s %s",
                        e.javaClass.simpleName,
                        e.message
                    )
                )
            }
        }
        mButtonPerformLocalTransaction.setOnClickListener {
            try {
                startAFreshCharge(true)
            } catch (e: Exception) {
                mTextError!!.setText(
                    String.format(
                        "An error occurred while charging card: %s %s",
                        e.javaClass.simpleName,
                        e.message
                    )
                )
            }
        }
    }

    private fun startAFreshCharge(local: Boolean) {
        // initialize the charge
        charge = Charge()
        charge!!.setCard(loadCardFromForm())
        dialog = ProgressDialog(this@PaymentActivity)
        dialog!!.setMessage("Performing transaction... please wait")
        dialog!!.show()
        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception
            charge!!.setAmount(2000)
            charge!!.setEmail("customer@email.com")
            charge!!.setReference("ChargedFromAndroid_" + Calendar.getInstance().timeInMillis)
            try {
                charge!!.putCustomField("Charged From", "Android SDK")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            chargeCard()
        } else {
            // Perform transaction/initialize on our server to get an access code
            // documentation: https://developers.paystack.co/reference#initialize-a-transaction
            charge!!.setAccessCode(intent.getStringExtra("accessCode"))
            chargeCard()
            //  new fetchAccessCodeFromServer().execute(backend_url + getIntent().getStringExtra("accessCode"));
        }
    }
    private fun chargeCard() {
        transaction = null
        PaystackSdk.chargeCard(this@PaymentActivity, charge, object : TransactionCallback {
            // This is called only after transaction is successful
            override fun onSuccess(transaction: Transaction) {
             dialog!!.dismiss()
              //  dismissDialog()
                val reference = transaction.reference
                Log.e("transaction", transaction.toString())
                this@PaymentActivity.transaction = transaction
                mTextError!!.text = " "
                Toast.makeText(
                    this@PaymentActivity,
                    transaction.reference,
                    Toast.LENGTH_LONG
                ).show()
                apiHitResultVerify(reference,"abcd","")
              //  updateTextViews()

                //     new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            override fun beforeValidate(transaction: Transaction) {
                this@PaymentActivity.transaction = transaction
                Toast.makeText(
                    this@PaymentActivity,
                    transaction.reference,
                    Toast.LENGTH_LONG
                ).show()
             //   updateTextViews()
            }

            /*  @Override
            public void showLoading(Boolean isProcessing) {
                Log.i(TAG, "Paystack SDK loading: " + isProcessing);
                if (isProcessing) {
                    Toast.makeText(PaymentActivity.this, "Processing...", Toast.LENGTH_LONG).show();
                }
            }*/
            override fun onError(error: Throwable, transaction: Transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                this@PaymentActivity.transaction = transaction
                if (error is ExpiredAccessCodeException) {
                    this@PaymentActivity.startAFreshCharge(false)
                    this@PaymentActivity.chargeCard()
                    return
                }
              dialog!!.dismiss()
                if (transaction.reference != null) {
                    Toast.makeText(
                        this@PaymentActivity,
                        transaction.reference + " concluded with error: " + error.message,
                        Toast.LENGTH_LONG
                    ).show()
                    mTextError!!.text = String.format(
                        "%s  concluded with error: %s %s",
                        transaction.reference,
                        error.javaClass.simpleName,
                        error.message
                    )
                  //  verifyOnServer().execute(transaction.reference)
                } else {
                    Toast.makeText(this@PaymentActivity, error.message, Toast.LENGTH_LONG)
                        .show()
                    mTextError!!.text =
                        String.format("Error: %s %s", error.javaClass.simpleName, error.message)
                }
            //    updateTextViews()
            }
        })
    }
    private fun loadCardFromForm(): Card? {
        //validate fields
        val card: Card
        val cardNum = mEditCardNum!!.text.toString().trim { it <= ' ' }

        //build card object with ONLY the number, update the other fields later
        card = Card.Builder(cardNum, 0, 0, "").build()
        val cvc = mEditCVC!!.text.toString().trim { it <= ' ' }
        //update the cvc field of the card
        card.cvc = cvc

        //validate expiry month;
        val sMonth = mEditExpiryMonth!!.text.toString().trim { it <= ' ' }
        var month = 0
        try {
            month = sMonth.toInt()
        } catch (ignored: java.lang.Exception) {
        }
        card.expiryMonth = month
        val sYear = mEditExpiryYear!!.text.toString().trim { it <= ' ' }
        var year = 0
        try {
            year = sYear.toInt()
        } catch (ignored: java.lang.Exception) {
        }
        card.expiryYear = year
        return card
    }

    private fun apiHitResultVerify(transactionRef: String, transaction_id: String, cartId: String) {

        mViewModel.verifyPaymentParam(transactionRef, transaction_id, SharaGoPref.getInstance(this).getCartId("").toString())
        mViewModel.lverifyPayment.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                   // dismissLoader()
                }
                is ExceptionHandler.Success -> {
                  //  dismissLoader()
                    it.data?.let {
                        Log.d("PaymentVerify", it.toString())
                        mViewModel.getCardDetailsUser(SharaGoPref.getInstance(this).getLoginToken("").toString())
                        mViewModel.lCartDetailsUser.observe(this){
                                it ->
                            when(it){
                                is ExceptionHandler.Loading->{
                                  //  dismissLoader()
                                }
                                is ExceptionHandler.Success->{
                                   // dismissLoader()
                                    it.data?.let {
                                        Log.e("cartDetails",it.toString())
                                        SharaGoPref.getInstance(this).setCartId(it.cartId.toString())
                                    }
                                }
                                is ExceptionHandler.Error->{
                                 //   dismissLoader()

                                }
                            }
                        }
                        try {

                            startActivity(
                                Intent(this, PaymentSuccessActivity::class.java).putExtra(
                                    "type", "success"
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
                is ExceptionHandler.Error -> {
                  //  dismissLoader()
                    startActivity(
                        Intent(this, PaymentSuccessActivity::class.java).putExtra("type", "failed")
                    )
                }
            }
        }


    }




}