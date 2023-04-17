package com.cradle.user.userActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserAddressBinding
import com.cradle.model.address.AddressListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.UserAddressListAdapter
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_address.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import org.json.JSONObject

class UserAddressActivity : BaseActivity(), View.OnClickListener,UserAddressListAdapter.TextBook  {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        findId()
    }

    private fun findId() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        apiHit()
        getApiResult()
        btn_AddNewAddress.setOnClickListener(this)
        setToolBar()
        statusBarColourChange()
    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
        mBinding.btnAddNewAddress.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }
    private fun setToolBar(){
        tv_usertoolbartitle.text=getString(R.string.address_list)
        iv_back.setOnClickListener { onBackPressed() }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getApiResult() {
        mViewModel.lAddressListParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        val defalutList=ArrayList<AddressListItem>()
                        val otherList=ArrayList<AddressListItem>()
                        val homeList=ArrayList<AddressListItem>()
                        Log.e("addresslist",it.addressList.toString())

                        for ( i in 0 until it.addressList!!.size)  {
                            if(it.addressList[i]!!.isDefault!!){
                                defalutList.add(it.addressList[i]!!)
                            }else{
                                if(it.addressList[i]!!.metaData!!.landmark!!.equals("home")){
                                    homeList.add(it.addressList[i]!!)
                                }else{
                                    otherList.add(it.addressList[i]!!)
                                }

                            }
                        }
                        if(it.addressList.isNotEmpty()){
                            recycler_homelist.visibility=View.VISIBLE
                            iv_no_data_found.visibility=View.GONE
                            mBinding.mUserAddressListHomeAdapter= UserAddressListAdapter(this,it.addressList as ArrayList<AddressListItem>,this)

                        }else{
                            iv_no_data_found.visibility=View.VISIBLE
                            recycler_homelist.visibility=View.GONE
                        }

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
        mViewModel.lDeleteAddressParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        val jsonObject= JSONObject(it.toString())
                        Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                        apiHit()

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }

        mViewModel.luserMarkAsDefaultParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        val jsonObject= JSONObject(it.toString())
                        Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                        apiHit()

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()

                }
            }
        }
    }

    private fun apiHit(){
        if(MyHelper.isNetworkConnected(this)){
            mViewModel.userAddressListParam(SharaGoPref.getInstance(this).getLoginToken("").toString())
        }else showToast(getString(R.string.no_internet_connection))
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_AddNewAddress-> {
                startActivity(Intent(this, UserAddAddressActivity::class.java))

            }
        }
    }

    override fun onResume() {
        super.onResume()
        apiHit()
    }
    private var dialog: Dialog? = null
    private fun moreDialog(addressListItem:String,data: Any) {
        data as AddressListItem
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dailog_address_filter)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        dialog!!.setCanceledOnTouchOutside(true)

        val tv_mark_as_default = dialog!!.findViewById(R.id.tv_mark_as_default) as TextView
        val tv_edit_address = dialog!!.findViewById(R.id.tv_edit_address) as TextView
        val tv_delete_address = dialog!!.findViewById(R.id.tv_delete_address) as TextView
        val tv_dismiss = dialog!!.findViewById(R.id.tv_dismiss) as TextView
        val rl_dialog = dialog!!.findViewById(R.id.rl_dialog) as RelativeLayout

        tv_mark_as_default.setOnClickListener {
            mViewModel.userMarkAsDefault(SharaGoPref.getInstance(this).getLoginToken("").toString(),addressListItem)
            dialog!!.dismiss()
        }
        rl_dialog.setOnClickListener {
            dialog!!.dismiss()
        }
        tv_edit_address.setOnClickListener {
            startActivity(
                Intent(this, UserAddAddressActivity::class.java).apply {
                    putExtra("data", data).putExtra("id",addressListItem).putExtra("buttonName","Update address")
                }
            )
            dialog!!.dismiss()
        }
        tv_delete_address.setOnClickListener {
            mViewModel.userDeleteAddress(SharaGoPref.getInstance(this).getLoginToken("").toString(),addressListItem)
            dialog!!.dismiss()
        }
        tv_dismiss.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.show()
    }



    override fun deleteAddress(position: Int, data: Any) {

        val addressListItem=(data as AddressListItem).id
        moreDialog(addressListItem!!,data)
      /*  if(MyConstants.MARKDEFAULT_ADDRESS_REQ_CODE.equals(position)){
           mViewModel.userMarkAsDefault(SharaGoPref.getInstance(this).getLoginToken("").toString(),addressListItem)
        }
        if(MyConstants.DELETE_ADDRESS_REQ_CODE.equals(position)){
            mViewModel.userDeleteAddress(SharaGoPref.getInstance(this).getLoginToken("").toString(),addressListItem)

        }*/
           }
}