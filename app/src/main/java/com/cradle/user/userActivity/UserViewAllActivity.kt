package com.cradle.user.userActivity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserViewallBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterUserViewAll
import com.cradle.utils.SharaGoPref
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.custom_toolbar_user.*



class UserViewAllActivity: BaseActivity() , View.OnClickListener {
    private lateinit var mBinding:ActivityUserViewallBinding
    private lateinit var mViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserViewallBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onChaPassWithLoginClick, this)


        setCustomToolBar()
        apiHit()
        setApiSult()
        statusBarColourChange()

    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    private fun apiHit(){
        val intent = intent
        val id = intent.getStringExtra("id")
        showLoader()
        mViewModel.getVAllWithID(id!!,"1","50","","")
    }

    private fun setCustomToolBar(){
        iv_back.setOnClickListener {
            finish()
        }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)


    }

    private fun setApiSult(){
        mViewModel.lVAllWithIDReq.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        tv_usertoolbartitle.text=it.sectionName
                        mBinding.userViewAllAdapter= AdapterUserViewAll(this,it.contentList)
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Toast.makeText(this,it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(p0: View?) {

    }
}