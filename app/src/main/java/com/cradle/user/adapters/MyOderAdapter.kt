package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterMyOrderBinding
import com.cradle.model.orderhistory.OrderListItem
import com.cradle.user.userActivity.OrderDetailActivity
import com.cradle.utils.MyConstants
import java.util.*

class MyOderAdapter(
    private val context: Context,
    private val orderlist: List<OrderListItem?>?
) :
    RecyclerView.Adapter<MyOderAdapter.MyViewHolder>() {
    var imageUrl: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val movieListBinding = DataBindingUtil.inflate<AdapterMyOrderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_my_order, parent, false
        )
        return MyViewHolder(movieListBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = orderlist!![position]
        holder.bind(list, position)
    }

    inner class MyViewHolder(private val mBinding: AdapterMyOrderBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: OrderListItem?, position: Int) {
            imageUrl = ""
            try {
                mBinding.tvOrderName.text = data!!.cartMetaData!!.items!!.get(0)!!.name
                mBinding.tvOrderOnState.text = data.orderState

                val string = com.cradle.utils.parseDateToddMMyyyyNoTime(data.createdOn)

                mBinding.tvCreateOn.text = string
                if (data.orderState!!.equals("Delivered") && data?.isRated == true) {
                    mBinding.rBarProductDetails.visibility = View.VISIBLE
                    mBinding.rBarProductDetails.rating =
                        data!!.cartMetaData!!.items!!.get(0)!!.rating!!
                } else if (data.orderState!!.equals("Delivered") && data?.isRated == false) {
                    mBinding.rBarProductDetails.visibility = View.VISIBLE
                } else {
                    mBinding.rBarProductDetails.visibility = View.GONE
                }
                val image = data.cartMetaData!!.items!!.get(0)!!.metaData!!.images

                if (image!!.size > 0) {
                    val imagePath = image[0]
                    imageUrl = imagePath!!
                    Glide.with(context)
                        .load(MyConstants.file_Base_URL + imagePath)
                        .override(100, 200)
                        .into(mBinding.ivCommit)
                }
            } catch (_: Exception) {

            }
            mBinding.llOpenHistory.setOnClickListener {
                try {
                    val i = Intent(context, OrderDetailActivity::class.java)
                    i.putExtra("orderId", data!!.orderId)
                   /* i.putExtra("name", data!!.addressMetaData!!.firstName)
                    i.putExtra("orderId", data.orderId)
                    i.putExtra(
                        "totalPrice1",
                        data.cartMetaData!!.items!!.get(0)!!.totalPrice.toString()
                    )
                    i.putExtra("orderState", data.orderState)
                    i.putExtra("address", data.addressMetaData!!.streetAddress)
                    i.putExtra("phone_number", data.addressMetaData.phone)
                    i.putExtra("qty", data.cartMetaData.items!!.get(0)!!.quantity.toString())
                    i.putExtra("price", data.cartMetaData.items.get(0)!!.actualPrice.toString())
                    i.putExtra("status", data.orderState)
                    i.putExtra("shippingRate", data.shippingRate)
                    i.putExtra("isRated", data.isRated)
                    i.putExtra("rating", data!!.cartMetaData!!.items!!.get(0)!!.rating!!)
                    i.putExtra("productId", data.cartMetaData.items.get(0)!!.productId!!)
                    i.putExtra("phone_number", data.addressMetaData.phone)
                    i.putExtra("product_name", data.cartMetaData.items.get(0)!!.name)
                    i.putExtra("vendorId", data.vendorId.toString())
                    i.putExtra("itemId", data!!.cartMetaData!!.items!!.get(0)!!.itemId)
                    i.putExtra("itemId", data!!.cartMetaData!!.items!!.get(0)!!.itemId)
                    i.putExtra(
                        "seller",
                        data.vendorMetadata!!.firstName + " " + data.vendorMetadata.lastName
                    )
                    i.putExtra("product_image", imageUrl)*/
                    context.startActivity(i)

                } catch (_: Exception) {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return orderlist!!.size
    }


}