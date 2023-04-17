package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserLocalProductBinding

import com.cradle.user.userActivity.UserProductDetailsActivity

class AdapterUserLocalProduct(private val context: Context/*, private val wishList: List<UserWishList?>?*/) :
    RecyclerView.Adapter<AdapterUserLocalProduct.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserLocalProductBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_local_product, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // val list = wishList!![position]
        holder.bind(/*list*/)
    }
    inner class MyViewHolder(private val movieListBinding: AdapterUserLocalProductBinding) :
        RecyclerView.ViewHolder(movieListBinding.root) {
        fun bind(/*wishList: UserWishList?*/){
        /*    movieListBinding.localProductList=wishList
            movieListBinding.executePendingBindings()
            var image = wishList!!.metaData!!.images
            if(image!!.size>0){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .override(100, 200)
                    .into(movieListBinding.ivWishlistimage)

            }

             */
            movieListBinding.rlOpenAddtocart.setOnClickListener {
                context.startActivity(
                    Intent(context, UserProductDetailsActivity::class.java))
            }
        }

    }
    override fun getItemCount(): Int {
        return /*wishList!!.size*/12;
    }
}