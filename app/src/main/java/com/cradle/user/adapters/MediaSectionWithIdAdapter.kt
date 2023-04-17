package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserMediaProductBinding
import com.cradle.model.ContentListItem
import com.cradle.model.commit.CommentsItem
import com.cradle.user.userActivity.CommentAllListActivity
import com.cradle.user.userActivity.ContentDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cts.saharaGoSeller.customui.CustomTextView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.adapter_user_media_product.view.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MediaSectionWithIdAdapter (
    private val context: Context, private val list: List<ContentListItem?>?,private val sendCommit:SendCommit
) :
    RecyclerView.Adapter<MediaSectionWithIdAdapter.MyViewHolder>() {
    private lateinit var mViewModel: MainViewModel
    var adapter:SliderPagerAdapter?=null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserMediaProductBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_media_product, parent, false)

        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val contentlist = list!![position]
            holder.bind(contentlist!!, position)
        }catch (e:Exception){}
    }
    inner class MyViewHolder(private val mBinding: AdapterUserMediaProductBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(contentListItem: ContentListItem, position: Int){
            val bannerList = ArrayList<String>()
            try {
                bannerList.clear()
                if (contentListItem.type.equals("Product")) {
                    mBinding.llProductLay.visibility = View.VISIBLE
                    mBinding.llCaddle.visibility = View.VISIBLE
                    mBinding.llComment.visibility = View.GONE
                    mBinding.tvProductName.text = contentListItem.subject
                    mBinding.tvProductPrice.text = context.getString(R.string.currency) + contentListItem.shortDescription
                    mBinding.rBarProductDetails.rating = contentListItem.body!!.toFloat()


                } else {

                    mBinding.llCaddle.visibility = View.GONE
                    mBinding.llComment.visibility = View.VISIBLE
                    mBinding.llProductLay.visibility = View.GONE
                    mBinding.tvMediaTitle.text = contentListItem.subject
                    mBinding.tvMediaDis.text = contentListItem.shortDescription
                    mBinding.tvView.text = contentListItem.readCount.toString()
                    mBinding.tvViewAllComments.text =
                        contentListItem.commentCount.toString() + " " + "Comments"
                    mBinding.tvDate.text = parseDateToddMMyyyy(contentListItem.createdOn).toString()
                    if (contentListItem.shortDescription!!.length <= 100) {

                    } else {
                        addReadMore(
                            contentListItem.shortDescription.toString(),
                            mBinding.tvMediaDis
                        )

                    }


                    val flag = SharaGoPref.getInstance(context).getCountryFlag("").toString()
                    Glide.with(context).load(MyConstants.file_Base_URL_flag + flag)
                        .into(mBinding.ivOpenCountry)

                }
                val file = contentListItem.files
                if (file!!.isNotEmpty()) {
                    for (i in 0 until file!!.size) {
                        val imagePath = file!![i]
                        bannerList.add(imagePath.toString())
                    }
                }
            }catch (e:Exception){}
            try {
                val videoUrl= contentListItem.videoUrl
                if (contentListItem.videoId!!.isNotEmpty()){
                    SharaGoPref.getInstance(context).setVideoID(contentListItem.videoId.toString())
                    bannerList.add(0,videoUrl.toString())
                    adapter= SliderPagerAdapter(
                        context,
                        bannerList,
                        "video",
                        bannerList.size,
                        contentListItem.id.toString(),
                        context.getString(R.string.home),
                        contentListItem.sectionId.toString(),
                        "Video",
                        ""

                    )

                }else{
                    adapter = SliderPagerAdapter(
                        context,
                        bannerList,
                        "image",
                        bannerList.size,
                        contentListItem.id.toString(),
                        context .getString(R.string.home),
                        contentListItem.sectionId.toString(),
                        contentListItem.type.toString(),
                        contentListItem.priority.toString()
                    )
                }
            }catch
                (e:Exception){

                adapter = SliderPagerAdapter(
                    context,
                    bannerList,
                    "image",
                    bannerList.size,
                    contentListItem.id.toString(),
                    context .getString(R.string.home),
                    contentListItem.sectionId.toString(),
                    contentListItem.type.toString(),
                    contentListItem.priority.toString()
                )
            }

            mBinding.photosViewpager.adapter = adapter

            TabLayoutMediator(itemView.tab_layout, itemView.photos_viewpager) { tab, position ->
            }.attach()
            if(contentListItem.comments!!.size>2){
                val commitFirst=ArrayList<CommentsItem>()
                val commitSecond=ArrayList<CommentsItem>()
                //  mBinding.tvLoadMore.visibility=View.VISIBLE
                for ( i in 0 until contentListItem.comments.size){
                    commitFirst.add(contentListItem.comments.get(i)!!)
                }

                var manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                mBinding.recyclerComment.layoutManager = manager

                var  vendorlistadapter =  UserBlogSampleCommitAdapter(context,commitFirst)
                //   earningAdapter =  UserCategoryListAdapter(activity!!,list,this)
                //  recycler_product_categories_list.adapter = earningAdapter
                mBinding.recyclerComment.adapter = vendorlistadapter

            }else{
                //   mBinding.tvLoadMore.visibility=View.GONE
                mBinding.commitAdapter= UserBlogSampleCommitAdapter(context,contentListItem.comments)

            }
            mBinding.llMainContainer.setOnClickListener {
                context.startActivity(
                    Intent(context, ContentDetailsActivity::class.java).putExtra("cotent_id",contentListItem.id.toString()).putExtra("sectionId",contentListItem.sectionId.toString()))
            }
            mBinding.editSubject.addTextChangedListener(object : TextWatcher {
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
                    //      tv_charecter_remaining.text=getString(R.string.characters_remaining)+" "+s.length+" "+"/ 150"
                    //    if (s.length> 150) context.showToast("finsh text limit")
                }
            })
            mBinding.ivSendMsg.setOnClickListener {
                val loginJsonObject = JsonObject()
                loginJsonObject.addProperty("id", contentListItem.id)
                loginJsonObject.addProperty("comment",mBinding.editSubject.text.toString() )

               // sendCommit.bookSession(loginJsonObject)
            }
            mBinding.tvViewAllComments.setOnClickListener {
          /*     contentListItem.commentCount=2
                var commentsItem=CommentsItem("","","rahul","")
                contentListItem.comments.add(commentsItem)
         */   //    context.showToast(contentListItem.commentCount.toString())
               // sendCommit.bookSession(contentListItem)

               context.startActivity(Intent(context, CommentAllListActivity::class.java).putExtra("id",contentListItem.id.toString()).putExtra("subject",contentListItem.shortDescription).putExtra("contentListItem",contentListItem.toString()))

            }

        }
    }
    interface SendCommit{
        fun bookSession(jsonObject: ContentListItem)
    }

    override fun getItemCount(): Int {
        return /*moviesList.size*/list!!.size;
    }

    private fun addReadMore(text: String, textView: CustomTextView) {
        val ss = SpannableString(text.substring(0, 100) + "... read more")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadLess(text, textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(context.getResources().getColor(R.color.blue))
                } else {
                    ds.setColor(context.getResources().getColor(R.color.blue))
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(ss)
        textView.setMovementMethod(LinkMovementMethod.getInstance())
    }

    private fun addReadLess(text: String, textView: CustomTextView) {
        val ss = SpannableString("$text read less")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadMore(text, textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(context.getResources().getColor(R.color.blue))
                } else {
                    ds.setColor(context.getResources().getColor(R.color.blue))
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(ss)
        textView.setMovementMethod(LinkMovementMethod.getInstance())
    }
    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "MMM dd yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

}
