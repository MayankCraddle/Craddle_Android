package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserSubcateBinding
import com.cradle.model.UserSubCategoryListItem
import com.cradle.user.userActivity.UserProductActivity
import com.cradle.user.userActivity.UserSubCateActivity
import com.cradle.utils.MyConstants

class AdapterUserSubCate (private val context: Context, private var wishList: List<UserSubCategoryListItem?>?) :
    RecyclerView.Adapter<AdapterUserSubCate.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserSubcateBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_subcate, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = wishList!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserSubcateBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: UserSubCategoryListItem?){
            mBinding.mUserSubCategoryListItem=wishList
            mBinding.executePendingBindings()
            var image = wishList!!.metaData!!.image
            Glide.with(context)
                .load(MyConstants.file_Base_URL+image)
                .into(mBinding.ivWishlistimage)
            /*  if(image!!.size>0){
                  var imagePath = image[0]
                  Glide.with(context)
                      .load(MyConstants.file_Base_URL+imagePath)
                      .override(100, 200)
                      .into(movieListBinding.ivWishlistimage)
  
              }*/
          /*  mBinding.ivWishlistimage.setOnClickListener {
                context.startActivity(Intent(context, UserAddToCartActivity::class.java))

            }*/

            mBinding.ivWishlistimage.setOnClickListener {
                if(wishList.isSubCategoryAvailable!!){
                    context.startActivity(Intent(context, UserSubCateActivity::class.java).putExtra("cat_id",wishList.id))

                }else{
                    context.startActivity(   Intent(context, UserProductActivity::class.java).putExtra("cat_id",wishList.id))

                }
            }
        }

    }
    override fun getItemCount(): Int {
        return wishList!!.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterdNames: ArrayList<UserSubCategoryListItem>): ArrayList<UserSubCategoryListItem> {
        this.wishList = filterdNames
        notifyDataSetChanged()
        return wishList as ArrayList<UserSubCategoryListItem>
    }
}