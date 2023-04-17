package com.cradle.user.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserMediaCategoryBinding
import com.cradle.model.SectionsItem
import com.cradle.model.media.CatListItem
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import java.util.*
import kotlin.collections.ArrayList

class MediaSectionAdapter (
    private val context: Context,
    private val list: ArrayList<SectionsItem?>?,
    private val textStateAction: TextStateAction
) :
    RecyclerView.Adapter<MediaSectionAdapter.MyViewHolder>() {
    var lastCheckposition = 0
    var checkPos = 0

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val movieListBinding = DataBindingUtil.inflate<AdapterUserMediaCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_user_media_category, parent, false
        )
        return MyViewHolder(movieListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = list!![position]
        holder.bind(listItem!!, position)
    }

    inner class MyViewHolder(private val mBinding: AdapterUserMediaCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(catListItem: SectionsItem, position: Int) {

            mBinding.tvMediaCate.text = catListItem.sectionName
              Glide.with(context).load(MyConstants.file_Base_URL+ catListItem.icon).placeholder(R.drawable.account).into(mBinding.ivCategory)

            mBinding.ivCategory.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            if (checkPos == position) {
                mBinding.rlClickCate.setBackgroundResource(R.color.colour_2)

            } else {
                mBinding.rlClickCate.setBackgroundColor(
                    SharaGoPref.getInstance(context)
                        .getColor(0)!!)
            }
            mBinding.rlClickCate.setOnClickListener {
               // if(position!=-1)
                checkPos = position
                textStateAction.clickOnSection(position, catListItem)
                notifyDataSetChanged()
            }

        }
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.adapterPosition
    }
    override fun getItemCount(): Int {
        return list!!.size
    }

    interface TextStateAction {

        fun clickOnSection(int: Int, catListItem: SectionsItem)
    }






}