package com.cradle.user.userFragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.common_screen.SplashScreenActivity
import com.cradle.databinding.ActivityUserAccountBinding
import com.cradle.model.UserMetaData
import com.cradle.model.address.AddressListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.userActivity.*
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_account.view.*
import kotlinx.android.synthetic.main.logout_dialog.*


class UserAccountFragment : Fragment(),View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserAccountBinding
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = ActivityUserAccountBinding.inflate(inflater)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v= mBinding.root
        mBinding.setVariable(BR.onAccount,this)
        AnalyticsUtils.analyticReport(requireActivity(),"AccountScreen")

        if (MyHelper.isNetworkConnected(requireActivity())) {
            findId()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

        statusbarColourChange()
        return v
    }
    private fun statusbarColourChange(){
        val whichFrag=SharaGoPref.getInstance(requireActivity()).getWhichFrag("")
        if(whichFrag.equals(getString(R.string.account_multimedia))){
            mBinding.rlToolbar.visibility=View.GONE
            mBinding.rlAccountHeader.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

        }else{
            mBinding.rlToolbar.visibility=View.VISIBLE
            mBinding.rlAccountHeader.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

        }

    }

    private fun findId() {
        v.ll_change_now.setOnClickListener(this)
        v.llDeleteAccount.setOnClickListener(this)
        v.llDeactivateAccount.setOnClickListener(this)
        v.ll_my_review.setOnClickListener(this)
        v.tv_manage_add.setOnClickListener(this)
        v.edt_default_address.setOnClickListener(this)
        v.iv_user_profile.setOnClickListener(this)
        v.ll_my_order.setOnClickListener(this)
      //  v.llNotification.setOnClickListener(this)
        v.ivOpenNotificationList.setOnClickListener(this)
        mBinding.llLogout.setOnClickListener {
            logOutDialog()
        }
        apiHit()
        apiResult()
    }
    private fun apiHit(){
        if(MyHelper.isNetworkConnected(requireActivity())){
          //  LoaderFragment.showLoader(requireActivity().supportFragmentManager)

        // Log.e("token",SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())
            mViewModel.userDetailByTokenParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())

        }else requireActivity().showToast(getString(R.string.no_internet_connection))
    }
    var addresEdit:AddressListItem?=null
    var userMetaData: UserMetaData?=null
    var emaild_id: String?=null
    var country: String?=null
    var address: String?=null
    @SuppressLint("SetTextI18n")
    private fun apiResult(){
        mViewModel.luserDetailByTokenReqParam.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    it.data?.let {
                        Log.e("profile",it.toString())
                        emaild_id=it.emailMobile.toString()
                        country=it.country.toString()
                       /* if (it.metaData!!.image!=null){
                            Glide.with(requireActivity()).load(MyConstants.file_Base_URL_flag+it.metaData!!.image).into(mBinding.ivUserProfile)
                        }*/
                        try {
                            if (it.metaData!=null){
                                userMetaData= it.metaData
                                mBinding.tvUserName.text= it.metaData.firstName+" "+it.metaData.lastName
                                mBinding.tvUserEmailId.text=emaild_id
                                Glide.with(requireActivity()).load(MyConstants.file_Base_URL+ it.metaData.image).error(
                                    R.drawable.avatar).into(mBinding.ivUserProfile)
                            }

                        }catch (e:Exception){

                        }


                        try {
                            if(it.address!=null){
                                mBinding.tvManageAdd.text=getString(R.string.manage_address)
                                address=getString(R.string.manage_address)
                              //  mBinding.tvUserEmailId.text=SharaGoPref.getInstance(requireActivity()).getEmail("")
                                mBinding.tvUserPhoneNum.text= it.address.metaData!!.phone.toString()
                                mBinding.llAddres.visibility=View.VISIBLE
                                mBinding.edtDefaultAddress.visibility=View.VISIBLE
                                mBinding.ivNoAddrss.visibility=View.GONE
                                mBinding.tvName.text= it.address.metaData.firstName+" "+ it.address.metaData.lastName.toString()
                                mBinding.tvAddress.text= it.address.metaData.streetAddress.toString()+", "+ it.address.metaData.city+", "+ it.address.metaData.country
                                mBinding.tvPhone.text="Mobile : "+ it.address.metaData.phone.toString()

                                addresEdit=it.address as AddressListItem
                            }else{
                                mBinding.llAddres.visibility=View.GONE
                                mBinding.edtDefaultAddress.visibility=View.GONE
                                mBinding.ivNoAddrss.visibility=View.VISIBLE
                                mBinding.tvManageAdd.text=getString(R.string.add)
                                address=getString(R.string.add)
                            }
                        }catch (e:Exception){
                            mBinding.llAddres.visibility=View.GONE
                            mBinding.ivNoAddrss.visibility=View.VISIBLE
                        }


                    }

                }
                is ExceptionHandler.Error->{


                }
            }
        }
    }

    private fun logOut(){
        AnalyticsUtils.analyticReport(requireActivity(),MyConstants.UserLoggedOut)
        val mpref=  SharaGoPref.getInstance(requireActivity())
        mpref.clear()
        val i = Intent(requireActivity(), SplashScreenActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        if (MyHelper.isNetworkConnected(requireActivity())) {
            mViewModel.userDetailByTokenParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())
            apiResult()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

    }


    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.ll_change_now->{
                startActivity(Intent(requireActivity(), UserChangePassWithLoginActivity::class.java))
            } R.id.llNotification->{
                startActivity(Intent(requireActivity(), NotificationActivity::class.java))
            }R.id.ivOpenNotificationList->{
                startActivity(Intent(requireActivity(), NotificationActivity::class.java))
            }R.id.llDeleteAccount->{
                startActivity(Intent(requireActivity(), DeleteAccountActivity::class.java))
            }R.id.llDeactivateAccount->{
                startActivity(Intent(requireActivity(), DeActivateAccountActivity::class.java))
            }R.id.ll_my_order->{
                startActivity(Intent(requireActivity(), MyOderActivity::class.java))
            } R.id.ll_my_review->{
                startActivity(Intent(requireActivity(), UserVendorListActivity::class.java))
            } R.id.tv_manage_add->{
            if (address.equals(getString(R.string.manage_address)))
                startActivity(Intent(requireActivity(), UserAddressActivity::class.java))
            else
                startActivity(Intent(requireActivity(), UserAddressActivity::class.java))
           // startActivity(Intent(requireActivity(), UserAddAddressActivity::class.java))


            }R.id.edt_default_address->{
            addresEdit?.let {
                // not null do something
                startActivity(Intent(requireActivity(), UserAddAddressActivity::class.java)
                    .putExtra("data",addresEdit!! as AddressListItem))

            }

                  }R.id.iv_user_profile->{
            startActivity(Intent(requireActivity(), UserEditProfileActivity::class.java)
                .putExtra("first_name",userMetaData!!.firstName.toString())
                .putExtra("last_name",userMetaData!!.lastName.toString())
                .putExtra("email_id",emaild_id)
                .putExtra("image",userMetaData!!.image.toString())
                .putExtra("country",country.toString()))
               }

        }
    }
    private fun logOutDialog() {
        dialog = Dialog(requireActivity())
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.logout_dialog)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
        dialog!!.rlYes.setOnClickListener {logOut()}
        dialog!!.rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.rlLogOut.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

}