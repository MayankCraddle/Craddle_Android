package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserViewAllBinding
import com.cradle.model.ContentListItem
import com.cradle.user.userActivity.ContentDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref

class AdapterUserViewAll(
    private val context: Context,
    private val contentJsonArray: List<ContentListItem?>?,
) :
    RecyclerView.Adapter<AdapterUserViewAll.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserViewAllBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_view_all, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = contentJsonArray!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserViewAllBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(countries: ContentListItem?, position: Int){
            mBinding.tvTitle.text=countries!!.subject
            mBinding.tvDis.text= countries.shortDescription
            val file= countries.files

            if(file!!.size>0){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    /*.override(100, 100)*/
                    .into(mBinding.ivWishlistimage)

            }

            mBinding.rlOpen.setOnClickListener {
                SharaGoPref.getInstance(context).setSectionId(countries.sectionId.toString())

                context.startActivity(
                    Intent(context,ContentDetailsActivity::class.java).putExtra("cotent_id",countries.id.toString()).putExtra("screen","home"))

            }
        }
    }
    override fun getItemCount(): Int {
        return contentJsonArray!!.size
    }
}