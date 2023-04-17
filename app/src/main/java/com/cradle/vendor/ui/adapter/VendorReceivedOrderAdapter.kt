package com.cradle.vendor.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterVendorReceivedOrderBinding

class VendorReceivedOrderAdapter (
    private val context: Context
   /* private val contentJsonArray: List<ContentListItem?>?,*/
) :
    RecyclerView.Adapter<VendorReceivedOrderAdapter.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterVendorReceivedOrderBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_vendor_received_order, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  val list = contentJsonArray!![position]
        holder.bind(/*list*/position)
    }
    inner class MyViewHolder(private val mBinding: AdapterVendorReceivedOrderBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(/*countries: ContentListItem?*/position: Int){
     /*       mBinding.tvOrderName.text=countries!!.subject
            mBinding.tvOrderDis.text= countries.shortDescription
            val file= countries.files

            if(file!!.size>0){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    *//*.override(100, 100)*//*
                    .into(mBinding.ivReceivedOrder)

            }*/
            mBinding.ivListingArrow.setOnClickListener {

              /*  context.startActivity(
                    Intent(context, ContentDetailsActivity::class.java).putExtra("cotent_id",countries.id.toString()).putExtra("screen","home"))
*/
            }
        }
    }
    override fun getItemCount(): Int {
        return /*contentJsonArray!!.size*/10
    }
}