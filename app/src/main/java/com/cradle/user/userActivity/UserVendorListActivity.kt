package com.cradle.user.userActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserVendorListBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.UserVendorListAdapter
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.*


class UserVendorListActivity: BaseActivity() , View.OnClickListener {
    private lateinit var mBinding: ActivityUserVendorListBinding
    private lateinit var mViewModel: MainViewModel
    private var whichVendor=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserVendorListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)


        setCustomToolBar()
        setRecyclerView()
        apiHit()
        showApiResult()
    }

    private fun setCustomToolBar() {

        iv_back.setOnClickListener {
            onBackPressed()
        }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        ivSearch.visibility=View.VISIBLE
        llCountryContainer.visibility=View.VISIBLE
        tv_country_name.visibility=View.GONE
        iv_open_country.visibility=View.GONE

        ivSearch.setOnClickListener {
           SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.wishList))
           SharaGoPref.getInstance(this).setSearchType(getString(R.string.media))

           startActivity(Intent(this, UserMainActivity::class.java).addFlags(
                   Intent.FLAG_ACTIVITY_CLEAR_TASK
                   ))
           finish()
       }

    }
    private fun setRecyclerView(){
    }

    @SuppressLint("SuspiciousIndentation")
    private fun apiHit() {
        if(intent.getStringExtra("POSITION").equals("0")){
            tv_usertoolbartitle.text=getString(R.string.wholesaler)
            Glide.with(this).load(R.drawable.slide_one_two)
                .error(R.drawable.loading).into(mBinding.ivBanner)
            whichVendor=intent.getStringExtra("WichVendor").toString()
        }
        if(intent.getStringExtra("POSITION").toString().equals("1")){
            tv_usertoolbartitle.text=getString(R.string.exporter)
            Glide.with(this).load(R.drawable.slide_two_two)
                .error(R.drawable.loading).into(mBinding.ivBanner)
             whichVendor=intent.getStringExtra("WichVendor").toString()
        }
        if(intent.getStringExtra("POSITION").toString().equals("2")){
            Glide.with(this).load(R.drawable.slider_three_two)
                .error(R.drawable.loading).into(mBinding.ivBanner)
            tv_usertoolbartitle.text=getString(R.string.manufacturer)
             whichVendor=intent.getStringExtra("WichVendor").toString()
        }
        if(intent.getStringExtra("POSITION").toString().equals("3")){
            Glide.with(this).load(R.drawable.slider4)
                .error(R.drawable.loading).into(mBinding.ivBanner)
            tv_usertoolbartitle.text=getString(R.string.retailers)
             whichVendor="all"
        }
        if(intent.getStringExtra("POSITION").toString().equals("4")){
            Glide.with(this).load(R.drawable.slider5)
                .error(R.drawable.loading).into(mBinding.ivBanner)
            tv_usertoolbartitle.text=getString(R.string.farmers)
             whichVendor="farmer"
        }

        if(whichVendor.isNotEmpty())
            showLoader()
        mViewModel.userVendorListParam(whichVendor,SharaGoPref.getInstance(this).getCountry("")!!)
    }
    private fun showApiResult(){
        mViewModel.lUserVendorList.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        if(it.vendorList!!.isNotEmpty()){
                            Log.e("vendorList",it.vendorList.toString())
                            mBinding.recyclerVendorList.visibility=View.VISIBLE
                            mBinding.ivNoDataFound.visibility=View.GONE
                            mBinding.mUserVendorListAdapter= UserVendorListAdapter(this,it.vendorList)

                        }else{
                            mBinding.recyclerVendorList.visibility=View.GONE
                            mBinding.ivNoDataFound.visibility=View.VISIBLE
                        }

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(p0: View?) {

    }
}
