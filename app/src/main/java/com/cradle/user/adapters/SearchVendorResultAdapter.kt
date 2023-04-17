package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterSearchVendorResultBinding
import com.cradle.model.SearchVendorVendorListItem
import com.cradle.user.userActivity.UserVendorDetailActivity
import com.cradle.utils.MyConstants
import java.util.ArrayList

class SearchVendorResultAdapter(
    private val context: Context,
    private var vendorList: List<SearchVendorVendorListItem?>?
) :
    RecyclerView.Adapter<SearchVendorResultAdapter.MyViewHolder>() {
    //private var list : MutableList<FAQuestion> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSearchVendorResultBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_search_vendor_result, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = vendorList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterSearchVendorResultBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(vendorList: SearchVendorVendorListItem?, position: Int){
            mBinding.executePendingBindings()
            mBinding.tvVendorTitleSearchResult.text=vendorList!!.metaData!!.firstName
            mBinding.tvVendorSearchAddress.text=vendorList!!.metaData!!.streetAddress
            mBinding.rBarProductDetails.rating=vendorList.rating!!.dec()
            val image = vendorList.metaData!!.image

            Glide.with(context)
                .load(MyConstants.file_Base_URL+image).error(R.drawable.loading)
                .into(mBinding.ivVendorSearchResult)


            mBinding.llOpenVendorDetail.setOnClickListener {
                val i = Intent(context, UserVendorDetailActivity::class.java)
                i.putExtra("vendor_id", vendorList.vendorId)
                context.startActivity(i)
            }
        }
    }
    override fun getItemCount(): Int {
        return vendorList!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterdNames: ArrayList<SearchVendorVendorListItem>): ArrayList<SearchVendorVendorListItem> {
        this.vendorList = filterdNames
        notifyDataSetChanged()
        return vendorList as ArrayList<SearchVendorVendorListItem>
    }


}