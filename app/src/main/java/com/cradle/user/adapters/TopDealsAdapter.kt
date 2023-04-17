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
import com.cradle.databinding.AdapterTopDealsBinding
import com.cradle.model.trade.ProductListItemByTrade
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants

class TopDealsAdapter(private val context: Context, private var productList: List<ProductListItemByTrade?>?) :
    RecyclerView.Adapter<TopDealsAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterTopDealsBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_top_deals, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = productList!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterTopDealsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: ProductListItemByTrade?){

            mBinding.executePendingBindings()
            mBinding.tvProductName.text=wishList!!.name
            mBinding.tvPrice.text= context.getString(R.string.currency)+" "+wishList.discountedPrice.toString()
            val image = wishList.metaData!!.images
            if(image!!.size>0){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .into(mBinding.imgProductImage)
            }
            mBinding.llOpenProductDetail.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id",wishList!!.itemId)
                i.putExtra("product_id", wishList.productId)
                context.startActivity(i)
            }
        }

    }
    override fun getItemCount(): Int {
        if(productList!!.size>4){
            return 4
        }else
            return productList!!.size

    }

}