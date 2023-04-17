package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
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
import com.cts.saharaGoSeller.customui.CustomTextView
import com.google.android.material.tabs.TabLayoutMediator
import com.cradle.R
import com.cradle.databinding.AdapterUserMediaProductBinding
import com.cradle.model.commit.CommentsItem
import com.cradle.user.userActivity.CommentAllListActivity
import com.cradle.user.userActivity.ContentDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import kotlinx.android.synthetic.main.adapter_user_media_product.view.*
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserMediaProductAdapter(
    private val context: Context,
    private val contentJsonArray: ArrayList<JSONObject>,
    private val length: Int
) :
    RecyclerView.Adapter<UserMediaProductAdapter.MyViewHolder>() {
    var adapter: SliderPagerAdapter? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val movieListBinding = DataBindingUtil.inflate<AdapterUserMediaProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_user_media_product, parent, false
        )
        return MyViewHolder(movieListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(contentJsonArray, position)
    }

    inner class MyViewHolder(private val mBinding: AdapterUserMediaProductBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(countries: ArrayList<JSONObject>, position: Int) {

            val jsonObject: JSONObject = countries.get(position)
            mBinding.tvMediaTitle.text = jsonObject.optString("subject")
            mBinding.tvMediaDis.text = jsonObject.optString("shortDescription")
            mBinding.tvView.text = jsonObject.optString("readCount")
            mBinding.tvView.text = jsonObject.optString("readCount")
            mBinding.tvViewAllComments.text =jsonObject.optString("commentCount")
                    .toString() + " " + "Comments"
            mBinding.tvComment.text = jsonObject.optString("commentCount").toString()

            var date = jsonObject.optString("createdOn").toString()
            val string=parseDateToddMMyyyy(date)
            mBinding.tvDate.text = string
            /*if(jsonObject.optString("shortDescription")!!.length>100){
                addReadMore(jsonObject.optString("shortDescription").toString(),mBinding.tvMediaDis)
            }*/
            val file = jsonObject.optJSONArray("files")
            var commentArray = jsonObject.optJSONArray("comments")
            val videoUrl = jsonObject.optString("videoUrl")
            val videoid = jsonObject.optString("videoId")

         val sectionId=   jsonObject.optString("sectionId").toString()
            val bannerList = ArrayList<String>()
            bannerList.clear()
            mBinding.tvViewAllComments.setOnClickListener {
                context.startActivity(
                    Intent(context, CommentAllListActivity::class.java).putExtra("id", jsonObject.optString("id").toString()).putExtra("sectionId","1").putExtra("subject",jsonObject.optString("shortDescription")))

            }
            val flag=SharaGoPref.getInstance(context).getCountryFlag("").toString()
            Glide.with(context).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)

            try {
                var comments = ArrayList<CommentsItem>()
                for (i in 0 until commentArray.length()) {
                    var comment = CommentsItem(
                        commentArray.getJSONObject(i).optString("image"),
                        commentArray.getJSONObject(i).optString("createdBy"),
                        commentArray.getJSONObject(i).optString("comment"),
                        commentArray.getJSONObject(i).optString("createdOn")
                    )
                    comments?.add(comment)
                }
                if (comments.size > 2) {
                    val commitFirst = ArrayList<CommentsItem>()
                    //val commitSecond=ArrayList<CommentsItem>()
                    //  mBinding.tvLoadMore.visibility=View.VISIBLE
                    for (i in 0 until comments.size) {
                        commitFirst.add(comments.get(i)!!)
                    }

                    var manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                    mBinding.recyclerComment.layoutManager = manager

                    var vendorlistadapter = UserBlogSampleCommitAdapter(context, commitFirst)
                    //   earningAdapter =  UserCategoryListAdapter(activity!!,list,this)
                    //  recycler_product_categories_list.adapter = earningAdapter
                    mBinding.recyclerComment.adapter = vendorlistadapter

                } else {
                    //   mBinding.tvLoadMore.visibility=View.GONE
                    mBinding.commitAdapter = UserBlogSampleCommitAdapter(context, comments)

                }
            } catch (e: Exception) {
            }


                for(i in 0 until file!!.length()){
                    val imagePath = file!![i]
                    bannerList.add(imagePath.toString())
                }



            if (videoUrl.isNotEmpty()) {
                SharaGoPref.getInstance(context).setVideoID(videoid.toString())
                bannerList.add(0,videoUrl.toString())
                adapter = SliderPagerAdapter(
                    context,
                    bannerList,
                    "video",
                    bannerList.size,
                    jsonObject.optString("id").toString(),
                    context.getString(R.string.home),
                    jsonObject.optString("sectionId").toString(),"Video"
                ,"")

            } else {
                adapter = SliderPagerAdapter(
                    context,
                    bannerList,
                    "image",
                    bannerList.size,
                    jsonObject.optString("id").toString(),
                    context.getString(R.string.home),jsonObject.optString("sectionId").toString(),
                    "Video",""
                )

            }
            mBinding.photosViewpager.adapter = adapter

            TabLayoutMediator(itemView.tab_layout, itemView.photos_viewpager) { tab, position ->
            }.attach()

            itemView.setOnClickListener {
                SharaGoPref.getInstance(context).setSectionId(jsonObject.optString("id").toString())
                context.startActivity(
                    Intent(context, ContentDetailsActivity::class.java).putExtra(
                        "cotent_id",
                        jsonObject.optString("id").toString()
                    ).putExtra("screen", "home").putExtra("sectionId",jsonObject.optString("sectionId").toString()).putExtra("subject",jsonObject.optString("subject"))
                )

            }



        }
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

    override fun getItemCount(): Int {
        return /*moviesList.size*/length;
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