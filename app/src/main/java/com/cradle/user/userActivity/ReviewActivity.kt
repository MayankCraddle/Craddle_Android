package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityViewReviewBinding
import com.cradle.model.RatingListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.ReviewAdapter

import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.chat_box_bottom.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*

class ReviewActivity: BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityViewReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityViewReviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        consult_send_message.setOnClickListener(this)
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        try {
            mBinding.tvContent.text=intent.getStringExtra("subject")!!.toString()
        }
        catch (_:Exception){

        }

        iv_back.setOnClickListener { finish() }
        tv_usertoolbartitle.text=getString(R.string.review)
        apiHit()
        apiResult()
    }
    private fun apiHit(){
        try {
            mBinding.tvContent.text=intent.getStringExtra("subject")!!
            mViewModel.userVendorRatingDetails(intent.getStringExtra("item_id")!!)


        }
        catch (_:Exception){

        }


    }
    private fun apiResult(){
        mViewModel.lUserVendorRatingDetails.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if(it.ratingList!!.isNotEmpty()){
                            mBinding.llNoDataFound.visibility=View.GONE
                            mBinding.recyclerViewRating.visibility=View.VISIBLE
                            mBinding.reviewAdapter= ReviewAdapter(this,it.ratingList as ArrayList<RatingListItem?>)

                        }else{
                            mBinding.llNoDataFound.visibility=View.VISIBLE
                            mBinding.recyclerViewRating.visibility=View.GONE
                        }
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    //   Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.consult_send_message->{
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                    et_writeComments.text.isNotEmpty()
                    if(et_writeComments.text.isNotEmpty()){
                        submitCommit()
                    }
                    else{
                        showToast(getString(R.string.please_write))
                    }
                }
                else{
                    startActivity(Intent(this, UserLoginActivity::class.java))
                }





            }
        }
    }

    private fun submitCommit(){
        val loginJsonObject = JsonObject()
        val id=intent.getStringExtra("item_id")
        loginJsonObject.addProperty("id", id)
        loginJsonObject.addProperty("comment",et_writeComments.text.toString() )

        mViewModel.userAddCommitParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),loginJsonObject)

    }
}
