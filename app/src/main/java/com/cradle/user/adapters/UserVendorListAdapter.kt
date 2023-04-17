package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserVendorListBinding
import com.cradle.model.VendorListItem
import com.cradle.user.userActivity.UserVendorDetailActivity
import com.cradle.utils.MyConstants

class UserVendorListAdapter(
    private val context: Context,
  private val vendorList: List<VendorListItem?>
) :
    RecyclerView.Adapter<UserVendorListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserVendorListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_vendor_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = vendorList[position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserVendorListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(vendorList: VendorListItem?, position: Int){
            mBinding.executePendingBindings()
            mBinding.tvWishListTitle.text=vendorList!!.metaData!!.companyName
            mBinding.tvAddress.text=vendorList!!.metaData!!.streetAddress
            mBinding.rBarProductDetails.rating= vendorList.rating!!
            val image = vendorList.metaData!!.image
            Glide.with(context)
                .load(MyConstants.file_Base_URL+image).error(R.drawable.loading)
                .into(mBinding.ivWishlistimage)


            mBinding.ivWishlistimage.setOnClickListener {
                val i = Intent(context, UserVendorDetailActivity::class.java)
                i.putExtra("vendor_id", vendorList.vendorId)
                context.startActivity(i)
            }
        }
    }
    override fun getItemCount(): Int {
        return vendorList.size
    }

}