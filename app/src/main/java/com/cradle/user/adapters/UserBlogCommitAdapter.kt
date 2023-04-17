package com.cradle.user.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserBlogCommitBinding
import com.cradle.model.commit.CommentsItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UserBlogCommitAdapter(
    private val context: Context,
    private val wishList: List<CommentsItem?>?
) :
    RecyclerView.Adapter<UserBlogCommitAdapter.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserBlogCommitBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_blog_commit, parent, false)
        return MyViewHolder(movieListBinding)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = wishList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserBlogCommitBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(wishList: CommentsItem?, position: Int){
         //   mBinding.whishList=wishList
         //   movieListBinding.executePendingBindings()
           // var image = wishList!!.metaData!!.images
            mBinding.tvCommitText.text= wishList!!.comment
            mBinding.tvCommitCreate.text= wishList.createdBy

           val string= parseDateToddMMyyyy(wishList.createdOn)

            mBinding.tvRemainingDay.text= parseData(string!!)
            mBinding.view.visibility=View.VISIBLE

        }
    }
    override fun getItemCount(): Int {
        return wishList?.size!!;
    }

    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "dd-MMM-yyyy h:mm a"
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
    fun parseData(string: String):String{
        val dateStr = "Jul 16, 2013 12:08:59 AM"
        val df = SimpleDateFormat("dd-MMM-yyyy h:mm a", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = df.parse(string)
        df.timeZone = TimeZone.getDefault()
        val formattedDate = df.format(date)
        return formattedDate
    }

}