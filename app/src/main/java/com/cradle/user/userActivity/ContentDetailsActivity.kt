package com.cradle.user.userActivity


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_fragment.LoaderFragment
import com.cradle.databinding.ActivityContentDetailBinding
import com.cradle.model.ContentListItem
import com.cradle.model.commit.CommentsItem
import com.cradle.model.commit.ContentUrl
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.BlogPreviewAdapter
import com.cradle.user.adapters.CotentDetailImageSlider
import com.cradle.user.adapters.UserBlogCommitAdapter
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_content_detail.*
import kotlinx.android.synthetic.main.activity_user_product_details.photos_viewpager
import kotlinx.android.synthetic.main.activity_user_product_details.tab_layout
import java.util.*


class ContentDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityContentDetailBinding
    private lateinit var sharetourl: String
    private lateinit var videoUrl: String
    private lateinit var contentId: String
    private lateinit var shareURl: String
    private lateinit var sectionId: String
    private lateinit var shortDis: String
    private var openLink: String = ""

    //...
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = (application as ApplicationClass).repository
        mBinding = ActivityContentDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)
        iv_back_commit.setOnClickListener(this)
        if (MyHelper.isNetworkConnected(this)) {
            findId()
        } else showToast(getString(R.string.no_internet_connection))


    }

    override fun onBackPressed() {
        super.onBackPressed()
        //   SharaGoPref.getInstance(this).setBACKPRESS("contentDetail")
        SharaGoPref.getInstance(this).setShowList("maltimedia")
        finish()
        //   SharaGoPref.getInstance(context).setShowList(context.getString(R.string.multimedia))

    }

    fun addFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        manager.beginTransaction().detach(fragment).attach(fragment).commit()
    }

    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)

        mBinding.rlSubmitCommit.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SuspiciousIndentation")
    private fun findId() {
        showLoader()

        ll_share_urlDetails.setOnClickListener(this)
        contentId = intent.getStringExtra("cotent_id")!!

        sectionId = intent.getStringExtra("sectionId")!!
        mViewModel.getContentDetail(intent.getStringExtra("cotent_id")!!.toInt())
        Log.e("content_id", intent.getStringExtra("cotent_id")!!.toInt().toString())
        mViewModel.getCommitParam(intent.getStringExtra("cotent_id")!!.toInt())
        loginApiResult()
        rl_submit_commit.setOnClickListener(this)
        llComment.setOnClickListener(this)
        tvReadAll.setOnClickListener(this)
        mBinding.ivFacebook.setOnClickListener {
            shareUrl(sharetourl)

        }

        mBinding.ivInsta.setOnClickListener {
            openWhatsApp(sharetourl)
        }
        mBinding.ivTwitter.setOnClickListener {
            shareTwitterNew(sharetourl)
        }

        mBinding.tvOpenLink.setOnClickListener {
            openLink(openLink)
        }
        mBinding.tvLoadMore.setOnClickListener {
            mBinding.commitAdapter = UserBlogCommitAdapter(this, commitSecond)
            mBinding.tvLoadMore.visibility = View.GONE

        }
        Log.e("setion_id", SharaGoPref.getInstance(this).getSectionId("").toString())
        previewApi()
        statusBarColourChange()

    }

    private fun previewApi() {
        mViewModel.getVAllWithID(
            sectionId,
            "1",
            "10",
            "",
            SharaGoPref.getInstance(this).getCountry("").toString()
        )

    }


    override fun onResume() {
        super.onResume()
        mViewModel.getCommitParam(intent.getStringExtra("cotent_id")!!.toInt())
        mViewModel.lGetCommit.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    Log.e("commitLIST", it.data!!.toString())
                    if (it.data.comments!!.isNotEmpty()) {
                        //mBinding.tvLoadMore.visibility=View.VISIBLE
                        for (i in 0 until it.data.comments.size) {
                            commitSecond.add(it.data.comments.get(i)!!)
                        }
                        mBinding.llComment.visibility = View.VISIBLE
                        mBinding.tvLoadMore.visibility = View.GONE
                        mBinding.tvViewAllComments.text =
                            it.data.comments.size.toString() + " " + "Comments"
                        //    mBinding.commitAdapter = UserBlogCommitAdapter(this, commitFirst)

                    } else {
                        mBinding.tvLoadMore.visibility = View.GONE
                        mBinding.llComment.visibility = View.GONE
                        //   mBinding.commitAdapter = UserBlogCommitAdapter(this, it.data.comments)

                    }

                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back_commit -> {
                onBackPressed()
            }
            R.id.ll_share_urlDetails -> {
                shareUrl(this, shareURl)
            }
            R.id.rl_submit_commit -> {
                //moreDialog()
                startActivity(
                    Intent(this, CommentAllListActivity::class.java)
                        .putExtra("id", contentId).putExtra("subject", shortDis)
                )
            }
            R.id.llComment -> {
                //moreDialog()
                startActivity(
                    Intent(this, CommentAllListActivity::class.java)
                        .putExtra("id", contentId).putExtra("subject", shortDis)
                )
            }
            R.id.tvReadAll -> {
                //moreDialog()
                startActivity(
                    Intent(this, CommentAllListActivity::class.java).putExtra("subject", shortDis)
                        .putExtra("id", contentId)
                )
            }
        }
    }

    //user login result
    var metadata: ContentUrl? = null
    var adapter: CotentDetailImageSlider? = null
    val commitFirst = ArrayList<CommentsItem>()
    val commitSecond = ArrayList<CommentsItem>()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun loginApiResult() {
        mViewModel.lUserContentDetails.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    Log.d("contentDetails", it.data!!.toString())
                    val bannerList = ArrayList<String>()
                    val newBannerList = ArrayList<String>()
                    //  val jsonObject = JSONObject(it.data!!.toString())
                    //val image = it.data.files

                    for (i in 0 until it.data.files!!.size) {

                        bannerList.add(it.data.files.get(i).toString())
                        tv_view_1.text = it.data.readCount.toString()
                        shareURl = it.data.shareURl!!
                    }
                    try {

                        if (it.data.videoUrl!!.isNotEmpty()) {
                            SharaGoPref.getInstance(this).setVideoID(it.data.videoId.toString())
                            bannerList.add(0,it.data.videoUrl.toString())

                            adapter = CotentDetailImageSlider(
                                this,
                                bannerList,
                                "video",
                                bannerList.size,
                                getString(R.string.home), it.data.readCount, it.data.shareURl
                            )

                        } else {
                            //bannerList.reversed()
                            adapter = CotentDetailImageSlider(
                                this,
                                bannerList,
                                "image",
                                bannerList.size,
                                getString(R.string.home),
                                it.data.readCount,
                                it.data.shareURl
                            )

                        }
                    } catch (e: Exception) {
                        //bannerList.reverse()
                        adapter = CotentDetailImageSlider(
                            this,
                            bannerList,
                            "image",
                            bannerList.size,
                            getString(R.string.home),
                            it.data.readCount,
                            it.data.shareURl
                        )

                    }


                    /*  for(i in it.data.files!!){
                           metadata= ContentUrl(it.data.files.toString(),"")
                      }*/
                    metadata = ContentUrl("videl", "http")
                    Log.e("yotube", metadata.toString())


                    //  adapter.setItem(imageList)
                    photos_viewpager.adapter = adapter

                    TabLayoutMediator(tab_layout, photos_viewpager) { tab, position ->
                    }.attach()


                    /*After setting the adapter use the timer */
                    val handler = Handler()
                    val Update = Runnable {
                        if (currentPage == bannerList.size + 1 - 1) {
                            currentPage = 0
                        }
                        photos_viewpager.setCurrentItem(currentPage++, true)
                    }

                    timer = Timer() // This will create a new Thread

                    timer!!.schedule(object : TimerTask() {
                        // task to be scheduled
                        override fun run() {
                            handler.post(Update)
                        }
                    }, DELAY_MS, PERIOD_MS)

                    shortDis = it.data.shortDescription.toString()
                    mBinding.tvShortDescription.text = it.data.shortDescription.toString()
                    SharaGoPref.getInstance(this).setShortDis(it.data.shortDescription.toString())
                    mBinding.tvContentDetail.text = Html.fromHtml(
                        it.data.body,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                    val content = Html.fromHtml(it.data.body, Html.FROM_HTML_MODE_LEGACY)
                    SharaGoPref.getInstance(this).setLogDis(it.data.subject.toString())
                    mBinding.tvProductName.text = it.data.sectionName.toString()
                    mBinding.tvRemainingDay.text = it.data.createdOn
                    mBinding.tvViewCount.text = it.data.readCount.toString()
                    mBinding.tvSubject.text = it.data.subject.toString()
                    mBinding.tvCommitCreate.text = it.data.author.toString()
                    sharetourl = it.data.shareURl.toString()
                    sharetourl = it.data.videoUrl.toString()
                    if (it.data.link != null&& !it.data.link.equals("")) {
                        openLink = it.data.link.toString()
                        ll_openLink.visibility = View.VISIBLE
                    } else {
                        tvOpenLink.visibility = View.GONE
                        ll_openLink.visibility = View.GONE
                    }


                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }

        mViewModel.lAddCommit.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader()
                }
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    showToast("Update comment")
                    mViewModel.getCommitParam(intent.getStringExtra("cotent_id")!!.toInt())
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
        mViewModel.lVAllWithIDReq.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    //    dismissLoader()
                    it.data?.let {
                        LoaderFragment.dismissLoader(supportFragmentManager)
                        // tv_usertoolbartitle.text=it.sectionName
                        val contentList = ArrayList<ContentListItem>()
                        for (i in 0 until it.contentList!!.size) {

                            if (!it.contentList.get(i)!!.id.toString().equals(contentId)) {
                                contentList.add(it.contentList[i]!!)
                            }

                        }
                        mBinding.mBlogPreviewAdapter = BlogPreviewAdapter(this, contentList)
                    }
                }
                is ExceptionHandler.Error -> {
                    LoaderFragment.dismissLoader(this.supportFragmentManager)
                }
            }
        }
    }

    private var dialog: Dialog? = null
    private fun moreDialog() {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_add_commit)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        dialog!!.setCanceledOnTouchOutside(true)

        val rl_add_commit = dialog!!.findViewById(R.id.rl_add_commit) as RelativeLayout
        val edit_subject = dialog!!.findViewById(R.id.edit_subject) as EditText
        val tv_charecter_remaining = dialog!!.findViewById(R.id.tv_charecter_remaining) as TextView
        val ll_commit_dialog = dialog!!.findViewById(R.id.rl_commit_dialog) as RelativeLayout
        rl_add_commit.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        edit_subject.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                tv_charecter_remaining.text =
                    getString(R.string.characters_remaining) + " " + s.length + " " + "/ 150"
                if (s.length > 150) showToast("finsh text limit")
            }
        })

        rl_add_commit.setOnClickListener {
            startActivity(
                Intent(this, CommentAllListActivity::class.java).putExtra(
                    "id",
                    intent.getStringExtra("cotent_id")!!.toInt()
                ).putExtra("subject", shortDis)
            )
            /*     val loginJsonObject = JsonObject()
                 loginJsonObject.addProperty("id", intent.getStringExtra("cotent_id")!!.toInt())
                 loginJsonObject.addProperty("comment",edit_subject.text.toString() )
                 mViewModel.userAddCommitParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),loginJsonObject)
                 dialog!!.dismiss()*/
        }
        ll_commit_dialog.setOnClickListener {
            dialog!!.dismiss()
        }



        dialog!!.show()

    }


    private fun shareUrl(url: String) {

        // See if official Facebook app is found
        val urlToShare = "http://frontsaharago.com/blog/57?sId=3"
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare)
        var facebookAppFound = false
        val matches: List<ResolveInfo> = getPackageManager().queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName)
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$urlToShare"
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }

        startActivity(intent)


    }

    private fun shareTwitterNew(url: String) {
        // See if official Facebook app is found
        val urlToShare = url
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare)
        var facebookAppFound = false
        val matches: List<ResolveInfo> = getPackageManager().queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android")) {
                intent.setPackage(info.activityInfo.packageName)
                facebookAppFound = true
                break
            }
        }
        // As fallback, launch sharer.php in a browser
        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            val sharerUrl = "https://twitter.com/intent/tweet?text=$urlToShare"
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }

        startActivity(intent)


    }

    private fun shareUrl(context: Context, shareURl: String?) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, shareURl)
        context.startActivity(Intent.createChooser(i, "Share URL"))
    }

    private fun openWhatsApp(shareURl: String?) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareURl)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            showToast("Whatsapp have not been installed.")
        }
    }

    private fun openLink(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}
