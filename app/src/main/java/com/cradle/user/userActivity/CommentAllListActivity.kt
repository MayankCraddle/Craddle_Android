package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_fragment.LoaderFragment
import com.cradle.databinding.ActivityCommentAllListBinding
import com.cradle.model.ContentListItem
import com.cradle.model.commit.CommentsItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.UserBlogCommitAdapter
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.chat_box_bottom.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*

class CommentAllListActivity: BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityCommentAllListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityCommentAllListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)

        findId()
    }

    private fun findId() {
        AnalyticsUtils.analyticReport(this, MyConstants.CommentsScreen)
        consult_send_message.setOnClickListener(this)
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)


        try {

            mBinding.tvContent.text=intent.getStringExtra("subject")!!.toString()
        }
        catch (_:Exception){

        }

        iv_back.setOnClickListener {
           onBackPressed()
          //  addCommitLocal(intent.getSerializableExtra("contentListItem")!! as ContentListItem)

        }
        tv_usertoolbartitle.text=getString(R.string.comment)
        apiHit()
        apiResult()
    }

    private fun addCommitLocal(contentListItem: ContentListItem){

        contentListItem!!.commentCount=2
        var commentsItem= CommentsItem("","","rahul","")
        contentListItem.comments!!.add(commentsItem)

        val intent = Intent()
        intent.putExtra("MESSAGE", "contentListItem")
        setResult(2, intent)
        finish()

    }


    private fun apiHit(){
        mViewModel.getCommitParam(intent.getStringExtra("id")!!.toInt())

    }
    private fun apiResult(){
        mViewModel.lVAllWithIDReq.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {
                    //    dismissLoader()
                    it.data?.let {
                        LoaderFragment.dismissLoader(supportFragmentManager)
                        // tv_usertoolbartitle.text=it.sectionName
                        val contentList = ArrayList<ContentListItem>()
                        for (i in 0 until it.contentList!!.size) {

                            if (!it.contentList.get(i)!!.id.toString().equals("331")) {
                                contentList.add(it.contentList[i]!!)
                            }

                        }
                    }
                }
                is ExceptionHandler.Error -> {
                    LoaderFragment.dismissLoader(this.supportFragmentManager)
                }
            }
        }
        mViewModel.lGetCommit.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    Log.e("commitLIST", it.data!!.toString())
                    if(it.data.comments!!.size>0){

                        mBinding.commitAdapterAll= UserBlogCommitAdapter(this,it.data.comments)


                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
        mViewModel.lAddCommit.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {
                    et_writeComments.setText("")
                    mViewModel.getCommitParam(intent.getStringExtra("id")!!.toInt())
                    mViewModel.userSectionListParam(
                        "1", "10",
                        "", SharaGoPref.getInstance(this).getCountry("").toString()
                    )
                    //   requireActivity().showToast("Update comment")
                    // mViewModel.getCommitParam(intent.getStringExtra("cotent_id")!!.toInt())
                }
                is ExceptionHandler.Error -> {
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.consult_send_message->{
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()){
                    et_writeComments.text.isNotEmpty()
                    if(et_writeComments.text.isNotEmpty()){
                        submitCommit()
                    }
                    else{
                        showToast(getString(R.string.please_write))
                    }
                }
                else{
                    startActivity(Intent(this, UserLoginActivity::class.java))
                }





            }
        }
    }

    private fun submitCommit(){
        val loginJsonObject = JsonObject()
        loginJsonObject.addProperty("id", intent.getStringExtra("id")!!.toInt())
        loginJsonObject.addProperty("comment",et_writeComments.text.toString() )

        mViewModel.userAddCommitParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),loginJsonObject)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharaGoPref.getInstance(this).setBACKPRESS("contentDetail").toString()


    }
}
