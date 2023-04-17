package com.cradle.user.userActivity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityMyUserOrderBinding
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class UserMyOrderActivity: BaseActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityMyUserOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findId()
    }

    private fun findId() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityMyUserOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

    }
}