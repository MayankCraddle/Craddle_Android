package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.cradle.R
import com.cradle.databinding.AdapterUserWishListBinding
import com.cradle.model.UserWishList
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.showToast


class UserWishListAdapter (private val context: Context, private val wishList: List<UserWishList?>?,private val removeProduct:TextState) :
    RecyclerView.Adapter<UserWishListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserWishListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_wish_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         val list = wishList!![position]
         holder.bind(list,position)
    }
    inner class MyViewHolder(private val movieListBinding: AdapterUserWishListBinding) :
        RecyclerView.ViewHolder(movieListBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(wishList: UserWishList?, position: Int){
            movieListBinding.whishList=wishList

              movieListBinding.executePendingBindings()
            val image = wishList!!.metaData!!.images
            movieListBinding.tvAmount.text= wishList.currency+" "+wishList.discountedPrice.toString()
            movieListBinding.tvPercentOff.text= wishList.discountPercent.toString()+"% OFF"

            if(image!!.size>0){
                val imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .into(movieListBinding.ivWishlistimage)
            }

            movieListBinding.ivWishListSelect.setOnClickListener {
                val jsonObject= JsonObject()
                jsonObject.addProperty("itemId",wishList.itemId)
                removeProduct.removeProductToWishList(jsonObject)
            }

            movieListBinding.rlOpenAddtocart.setOnClickListener {
                if(wishList.orderable.equals("false")){
                    context.showToast(context.getString(R.string.you_do_not))

                }else{
                    removeProduct.addtoCard(wishList,1)
                    val jsonObject= JsonObject()
                    jsonObject.addProperty("itemId",wishList.itemId)
                    removeProduct.removeProductToWishList(jsonObject)
                }

            }
            movieListBinding.rlWishListOpen.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", wishList.itemId)
                i.putExtra("product_id", wishList.productId)
                context.startActivity(i)

            }
        }
    }
    override fun getItemCount(): Int {
        return wishList?.size!!
    }
    interface TextState{
        fun addtoCard(data: UserWishList?, position: Int)
        fun removeProductToWishList(jsonObject: JsonObject)
    }
}