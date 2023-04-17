package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterSerachProductResultBinding
import com.cradle.model.SearchProductProductListItem
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import java.util.ArrayList

class SearchProductResultAdapter(
    private val context: Context,
    private var vendorList: List<SearchProductProductListItem?>?,var addWishList:AddToWishList
) :
    RecyclerView.Adapter<SearchProductResultAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSerachProductResultBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_serach_product_result, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = vendorList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterSerachProductResultBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(vendorList: SearchProductProductListItem?, position: Int){
            mBinding.executePendingBindings()
            mBinding.tvVendorTitleSearchResult.text=vendorList!!.categoryName
            mBinding.tvSeachProDis.text=vendorList!!.metaData!!.description
            mBinding.tvSeachActualPrice.text=context.getString(R.string.currency)+""+vendorList.actualPrice.toString()
            mBinding.tvSeachActualPrice.paintFlags =
                mBinding.tvSeachActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            mBinding.tvSeachActualPrice.text=context.getString(R.string.currency)+vendorList!!.actualPrice.toString()
            mBinding.tvSeachDisPrice.text=context.getString(R.string.currency)+vendorList!!.discountedPrice.toString()
            mBinding.rBarProductDetails.rating=vendorList.rating!!.dec()
            val image = vendorList.metaData!!.images

            mBinding.ivAddToWishList.setOnClickListener {
                if (SharaGoPref.getInstance(context).getLoginToken("")!!.isNotEmpty()){
                    context.showToast("Added Successfully!")
                    addWishList.addWishList(position,vendorList)
                }else{
                    context.startActivity(Intent(context, UserLoginActivity::class.java))
                }

            }
            if(image!!.size>0) {
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL + imagePath)
                    .override(100, 200).error(R.drawable.loading)
                    .into(mBinding.ivVendorSearchResult)

            }
            mBinding.ivVendorSearchResult.setOnClickListener {

                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id",vendorList!!.itemId)
                i.putExtra("product_id", vendorList.productId)
               context.startActivity(i)
            }


        }
    }
    override fun getItemCount(): Int {
        return vendorList!!.size
    }

    fun filterList(filterdNames: ArrayList<SearchProductProductListItem>): ArrayList<SearchProductProductListItem> {
        this.vendorList = filterdNames
        notifyDataSetChanged()
        return vendorList as ArrayList<SearchProductProductListItem>
    }

    interface AddToWishList {
        fun addWishList(position: Int, dataList: SearchProductProductListItem?)
    }

}