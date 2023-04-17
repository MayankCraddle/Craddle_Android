package com.cradle.user.userActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.cradle.R
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityPaymentSuccessBinding
import kotlinx.android.synthetic.main.activity_payment_success.*

class PaymentSuccessActivity : BaseActivity() {
    private lateinit var mBinding: ActivityPaymentSuccessBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var type = intent.getStringExtra("type")
        try {
            if (type.equals("success")) {
                ivImage.setImageResource(R.drawable.image)
                tvStatus.setText("Payment Successful!")
                tvMsg.setText("Your payment is successfully completed.")
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.colorGreen))
                btnViewOrder.visibility = View.VISIBLE
                btnContinue.setText("Continue Shopping")
            } else {
                ivImage.setImageResource(R.drawable.failed)
                tvStatus.setText("Payment Failed!")
                tvMsg.setText("You can retry the payment below to continue this.")
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.colour_red))
                btnContinue.setText("Retry")
                btnViewOrder.visibility = View.GONE

            }
        } catch (e: Exception) {
        }
        btnContinue.setOnClickListener {
            if (type.equals("success")) {
                finish()
                startActivity(
                    Intent(this, UserMainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            } else {
                finish()
            }
        }
        btnViewOrder.setOnClickListener {
            if (type.equals("success")) {
                finish()
                startActivity(
                    Intent(this, MyOderActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            } else {
                finish()
            }
        }
    }
}