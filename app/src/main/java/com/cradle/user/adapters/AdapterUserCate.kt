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
import com.cradle.databinding.AdatperUserCateBinding
import com.cradle.model.CategoryListItem
import com.cradle.user.userActivity.UserProductActivity
import com.cradle.user.userActivity.UserSubCateActivity
import com.cradle.utils.MyConstants

class AdapterUserCate (private val context: Context, private var wishList: List<CategoryListItem?>?) :
    RecyclerView.Adapter<AdapterUserCate.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdatperUserCateBinding>( LayoutInflater.from(parent.context),
            R.layout.adatper_user_cate, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val list = wishList!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdatperUserCateBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: CategoryListItem?){


                mBinding.userCateListResponse=wishList
                mBinding.executePendingBindings()
                val image = wishList!!.metaData!!.image
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+image)
                    .override(100, 200)
                    .into(mBinding.imgProductImage)


    
            mBinding.rlUserCate.setOnClickListener {
                if(wishList.isSubCategoryAvailable!!){
                 context.startActivity(Intent(context, UserSubCateActivity::class.java).putExtra("cat_id",wishList.id))

                }else {
                    context.startActivity(   Intent(context, UserProductActivity::class.java).putExtra("cat_id",wishList.id))
                }

            }
        }

    }
    override fun getItemCount(): Int {
        return wishList!!.size
    }
    fun filterList(filterdNames: ArrayList<CategoryListItem>): ArrayList<CategoryListItem> {
        this.wishList = filterdNames
        notifyDataSetChanged()
        return wishList as ArrayList<CategoryListItem>
    }
}