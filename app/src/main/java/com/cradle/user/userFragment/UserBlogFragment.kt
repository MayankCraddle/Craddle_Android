package com.cradle.user.userFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.FragUserBlogBinding
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory

class UserBlogFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: FragUserBlogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.frag_user_blog, container, false)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

        v= mBinding.root
        mBinding.setVariable(BR.onUserBlog,this)
        findId()

        return v
    }

    private fun findId() {

    }

    override fun onClick(p0: View?) {

    }
}