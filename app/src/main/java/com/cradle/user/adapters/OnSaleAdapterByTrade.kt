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
import com.cradle.databinding.AdapterOnsaleByTradeBinding
import com.cradle.model.trade.ProductListItemByTrade
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants

class OnSaleAdapterByTrade (private val context: Context, private var productList: List<ProductListItemByTrade?>?) :
    RecyclerView.Adapter<OnSaleAdapterByTrade.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterOnsaleByTradeBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_onsale_by_trade, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val list = productList!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterOnsaleByTradeBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: ProductListItemByTrade?){

            mBinding.executePendingBindings()
            mBinding.tvProductName.text=wishList!!.name
            mBinding.tvProductPrice.text=context.getString(R.string.currency)+" "+ wishList.discountedPrice.toString()
            val image = wishList.metaData!!.images
            if(image!!.isNotEmpty()){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).placeholder(R.drawable.loading)
                    .into(mBinding.imgProductImage)
            }
            mBinding.llOpenProductDetail.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", wishList.itemId)
                i.putExtra("product_id", wishList.productId)
                context.startActivity(i)
            }
        }

    }
    override fun getItemCount(): Int {
        if(productList!!.size>6){
            return 6
        }else
            return productList!!.size

    }

}