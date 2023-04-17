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
import com.google.gson.JsonObject
import com.cradle.R
import com.cradle.databinding.AdapterViewAllByTradeBinding
import com.cradle.model.trade.ProductListItemByTrade
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.showToast

class ViewAllByTradeAdapter(
    private val context: Context,
    private var contentJsonArray: List<ProductListItemByTrade?>,private val textState:TextState
) :
    RecyclerView.Adapter<ViewAllByTradeAdapter.MyViewHolder>() {

    var lastCheckposition=-1
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterViewAllByTradeBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_view_all_by_trade, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = contentJsonArray[position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterViewAllByTradeBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(countries: ProductListItemByTrade?, position: Int){
            mBinding.tvTitle.text=countries!!.name
            mBinding.tvAmount.text= context.getString(R.string.currency)+" "+countries.discountedPrice.toString()
            val file= countries.metaData!!.images

            if(file!!.isNotEmpty()){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    .into(mBinding.ivWishlistimage)

            }

            mBinding.rlOpenAddtocart.setOnClickListener {
                if(countries.orderable.equals("false")){
                    context.showToast(context.getString(R.string.you_do_not))

                }else{
                    lastCheckposition=position
                    textState.textStateAction(countries,1)
                }

            }
            mBinding.ivWishlistimage.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", countries.itemId)
                i.putExtra("product_id", countries.productId)
                context.startActivity(i)
            }



        }
    }
    override fun getItemCount(): Int {
        return contentJsonArray!!.size
    }

    interface TextState{
        fun textStateAction(data: ProductListItemByTrade?, position: Int)
        fun addProductToWishList(jsonObject: JsonObject)
    }


}