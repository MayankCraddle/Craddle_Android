package com.cradle.user.userActivity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityMyOderBinding
import com.cradle.model.orderhistory.OrderListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.MyOderAdapter
import com.cradle.utils.AnalyticsUtils
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.*

class MyOderActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityMyOderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityMyOderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        AnalyticsUtils.analyticReport(this,MyConstants.ViewOrdersScreen)

        apiHit()
        apiResult()
        iv_back.setOnClickListener { finish() }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        tv_usertoolbartitle.text = "My Order"
    }
    private fun apiHit(){
        mViewModel.orderListParam(SharaGoPref.getInstance(this).getLoginToken("")!!)
    }
    private fun apiResult(){
        mViewModel.lOrder.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    //Log.e("commitLIST", it.data!!.toString())
                    if(it.data?.orderList!!.isNotEmpty()){
                        mBinding.myOderAdapter= MyOderAdapter(this,it.data.orderList)
                        setRecyclerList(it.data.orderList)
                        mBinding.llNoDataFound.visibility = View.GONE
                        mBinding.rvOrder.visibility = View.VISIBLE

                    }else{
                        mBinding.llNoDataFound.visibility = View.VISIBLE
                        mBinding.rvOrder.visibility = View.GONE
                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    mBinding.llNoDataFound.visibility = View.VISIBLE
                    mBinding.rvOrder.visibility = View.GONE
                  //  Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    override fun onClick(p0: View?) {

    }

    override fun onResume() {
        super.onResume()
        apiHit()
    }
    private fun setRecyclerList(orderList: List<OrderListItem?>) {
        mBinding.myOderAdapter= MyOderAdapter(this,orderList)
    }

}