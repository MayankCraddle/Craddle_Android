package com.cradle.user.userActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.databinding.ActivityVerifyOtpBinding
import com.cradle.databinding.FragmentNotificationBinding
import com.cradle.model.NotificationListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.NotificationListAdapter
import com.cradle.utils.AnalyticsUtils
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationActivity :AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragmentNotificationBinding
    private var mpref: SharaGoPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = FragmentNotificationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

        mpref = SharaGoPref.getInstance(this)
        findId()
        
    }
    @SuppressLint("SetTextI18n")
    private fun findId() {
        AnalyticsUtils.analyticReport(this,MyConstants.NotificationsScreen)

        mBinding.rlToolBar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        mBinding.tvCountryName.text = SharaGoPref.getInstance(this).getCountry("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+"nigeria.png").into(mBinding.ivOpenCountry)
        ivBack.setOnClickListener { finish() }
        apiHitWithResult()
    }

    private fun apiHitWithResult() {
        mViewModel.notificationListParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),"1","10")
        mViewModel.lNotificationList.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        try {
                            if (it.notificationList!!.size>0){
                                mBinding.llNoDataFound.visibility= View.GONE
                                mBinding.recylerNotificationList.visibility= View.VISIBLE
                                addRecyclerView(it.notificationList)
                            }else{
                                mBinding.llNoDataFound.visibility= View.VISIBLE
                                mBinding.recylerNotificationList.visibility= View.GONE

                            }

                        }catch (e:Exception){
                            mBinding.llNoDataFound.visibility= View.GONE
                            mBinding.recylerNotificationList.visibility= View.VISIBLE


                        }

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    private fun addRecyclerView(notificationList: List<NotificationListItem?>) {
        mBinding.mNotificationListAdapter= NotificationListAdapter(this,notificationList)


    }


}