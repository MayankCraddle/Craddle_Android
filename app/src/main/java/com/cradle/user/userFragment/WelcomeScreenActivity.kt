package com.cradle.user.userFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.ActivityWelcomescreenBinding
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class WelcomeScreenActivity: Fragment(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityWelcomescreenBinding
    private lateinit var v: View



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = ActivityWelcomescreenBinding.inflate(layoutInflater)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v= mBinding.root
        mBinding.setVariable(BR.onWelcomeClick,this)

        initUI()
        return v
    }

    private fun initUI() {
     
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rl_login -> {
                startActivity(Intent(requireActivity(), UserLoginActivity::class.java))

            }
            R.id.rl_start_selling -> {
                Utility.toastMessage(requireActivity(),"Under Development")
            }
        }
    }

}