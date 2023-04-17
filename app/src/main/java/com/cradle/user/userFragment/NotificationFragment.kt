package com.cradle.user.userFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.FragmentNotificationBinding
import com.cradle.model.NotificationListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.NotificationListAdapter
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class NotificationFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_notification, container, false)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

        v= mBinding.root
        mBinding.setVariable(BR.onUserBlog,this)
        findId()

        return v
    }

    @SuppressLint("SetTextI18n")
    private fun findId() {
        mBinding.rlToolBar.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)
        mBinding.tvCountryName.text = SharaGoPref.getInstance(requireActivity()).getCountry("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+"nigeria.png").into(mBinding.ivOpenCountry)

        apiHitWithResult()
    }

    private fun apiHitWithResult() {
        mViewModel.notificationListParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),"1","10")
        mViewModel.lNotificationList.observe(requireActivity()){
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
                            }

                        }catch (e:Exception){
                            mBinding.llNoDataFound.visibility= View.GONE
                            mBinding.recylerNotificationList.visibility= View.VISIBLE


                        }

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }

    private fun addRecyclerView(notificationList: List<NotificationListItem?>) {
        mBinding.mNotificationListAdapter= NotificationListAdapter(requireActivity(),notificationList)


    }


    override fun onClick(p0: View?) {

    }
}