package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterSimilerProductByDetailsBinding
import com.cradle.model.ProductListItem
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.showToast

class OnSaleAdapter (private val context: Context, private var productList: List<ProductListItem?>?,private val addtocart: TextState) :
    RecyclerView.Adapter<OnSaleAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSimilerProductByDetailsBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_similer_product_by_details, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = productList!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterSimilerProductByDetailsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: ProductListItem?){
            Log.e("varientList",wishList.toString())
            mBinding.executePendingBindings()
            mBinding.tvProductName.text=wishList!!.name
            mBinding.tvShortDesc.text=wishList!!.metaData?.description
            mBinding.tvPrice.text= context.getString(R.string.currency)+" "+wishList.actualPrice.toString()
            mBinding.tvPercentOff.text= wishList.discountValue.toString()+"% OFF"

            val image = wishList.metaData!!.images
            if(image!!.size>0){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .into(mBinding.imgProductImage)
            }
            mBinding.imgProductImage.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id",wishList!!.itemId)
                i.putExtra("product_id", wishList.productId)
                context.startActivity(i)
            }
            mBinding.rlOpenAddtocart.setOnClickListener {
                if(wishList.orderable.equals("false")){
                    context.showToast(context.getString(R.string.you_do_not))

                }else{
                    addtocart.addProductToCart(wishList,wishList.stock!!,wishList.itemId!!)
                }


            }
        }

    }
    override fun getItemCount(): Int {
        return productList!!.size
    }

    interface TextState{
        fun addProductToCart(data: ProductListItem?, position: Int,itemId:String)
    }

}