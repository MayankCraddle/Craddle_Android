package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserAddtocartBinding
import com.cradle.model.ProductListItem1

class LocalCartListAdapter(private val context: Context, private val wishList: List<ProductListItem1?>?) :
    RecyclerView.Adapter<LocalCartListAdapter.MyViewHolder>() {

    var lastCheckposition=-1

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserAddtocartBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_addtocart, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
           val list = wishList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val adapterUserAddtocartBinding: AdapterUserAddtocartBinding) :
        RecyclerView.ViewHolder(adapterUserAddtocartBinding.root) {

        fun bind(wishList: ProductListItem1?,position: Int){
            var i=0
            adapterUserAddtocartBinding.ivPlus.setOnClickListener {
                lastCheckposition=position
                adapterUserAddtocartBinding.tvQuantity.text= i++.toString()

            }
            adapterUserAddtocartBinding.ivMinus.setOnClickListener {
                lastCheckposition=position
                adapterUserAddtocartBinding.tvQuantity.text= i--.toString()

            }
            /* adapterUserAddtocartBinding.addToCartList=wishList
             adapterUserAddtocartBinding.executePendingBindings()
             var image = wishList!!.metaData!!.images*/
            /*  if(image!!.size>0){
                  var imagePath = image[0]
                  Glide.with(context)
                      .load(MyConstants.file_Base_URL+imagePath)
                      .override(100, 200)
                      .into(adapterUserAddtocartBinding.ivWishlistimage)
  
              }
  
              movieListBinding.rlOpenAddtocart.setOnClickListener {
                  context.startActivity(
                      Intent(context, UserProductDetailsActivity::class.java)
                  )
              }*/
        }
    }
    override fun getItemCount(): Int {
        return wishList!!.size;
    }
}