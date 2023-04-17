package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserMediaCategoryBinding

import com.cradle.model.SectionsItem

class UserMediaSectionAdapter (private val context: Context, private var wishList: List<SectionsItem?>?, var listener:TextBookNow) :
    RecyclerView.Adapter<UserMediaSectionAdapter.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserMediaCategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_media_category, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = wishList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserMediaCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("NewApi")
        fun bind(wishList: SectionsItem?, position: Int){

            if(position==0){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_1))
            }
            if(position==1){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_2))
            }
            if (position==2){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_3))
            }
            if(position==3){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_4))
            }
            if (position==4){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_5))

            }
            if(position==5){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_6))

            }
            mBinding.tvMediaCate.text= wishList!!.sectionName

            mBinding.rlClickCate.setOnClickListener {
                listener.bookSession(position,wishList)
            }
        }


    }
    interface TextBookNow{
        fun bookSession(position: Int,data: SectionsItem)
    }
    override fun getItemCount(): Int {
        return wishList!!.size
    }

}