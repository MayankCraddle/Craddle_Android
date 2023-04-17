package com.cradle.user.adapters

import android.annotation.SuppressLint
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
import com.cradle.databinding.AdapterUserAddtocartBinding
import com.cradle.model.cart.ItemsItem
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.Utility

class UserAddToCartAdapter (private val context: Context, private val wishList: List<ItemsItem?>?,var updateQtyListenenr:ServerUpdateQty
                            , var deleteItemListener:ServerDeleteCart) :
    RecyclerView.Adapter<UserAddToCartAdapter.MyViewHolder>() {

    var lastCheckposition=-1

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserAddtocartBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_addtocart, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = wishList!![position]
        holder.bind(list,position,wishList)
    }
    inner class MyViewHolder(private val adapterUserAddtocartBinding: AdapterUserAddtocartBinding) :
        RecyclerView.ViewHolder(adapterUserAddtocartBinding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(wishList: ItemsItem?, position: Int, wishList1: List<ItemsItem?>) {
            var i = 0
            var minQty =0

           try {
               if(wishList!!.variantMetadata!!.minQuantity!=null){

                   minQty= wishList.variantMetadata!!.minQuantity!!
                   if(minQty> wishList.quantity!!.toInt()){
                       adapterUserAddtocartBinding.tvQuantity.text = minQty.toString()
                   }else{
                       adapterUserAddtocartBinding.tvQuantity.text = wishList.quantity.toString()
                   }
               }else{
                   adapterUserAddtocartBinding.tvQuantity.text = wishList.quantity.toString()


               }
           }catch (e:Exception){
               minQty=wishList!!.quantity!!.toInt()
               adapterUserAddtocartBinding.tvQuantity.text = wishList!!.quantity.toString()

           }


            adapterUserAddtocartBinding.tvProductName.text = wishList!!.name
            adapterUserAddtocartBinding.tvNoOfPieces.text ="No of Pieces " +wishList.metaData!!.noOfPieces.toString()
            adapterUserAddtocartBinding.tvShortDescription.text =
                wishList.metaData?.shortDescription.toString()
            adapterUserAddtocartBinding.tvProductAmount.text =  context.getString(R.string.currency) + " " +wishList.discountedPrice.toString()


        //    adapterUserAddtocartBinding.tvQuantity.text = wishList.quantity.toString()
           // adapterUserAddtocartBinding.tvQuantity.text = minQty
            /*if (!wishList.variant.toString().equals("null")){
                adapterUserAddtocartBinding.tvProductQuantity.text= wishList.variant.toString()
            }*/
            //  adapterUserAddtocartBinding.tvProductQuantity.text= wishList.currency+" "+wishList.discountedPrice

            adapterUserAddtocartBinding.ivPlus.setOnClickListener {
                lastCheckposition = position

                if(minQty> wishList.quantity!!.toInt()){
                    if ((minQty + 1) <= wishList.stock!!.toInt()) {
                        wishList.quantity = (minQty + 1)
                        notifyDataSetChanged()
                        if (wishList.quantity!! == minQty) {
                            deleteItemListener.serverDeleteItem(position, wishList)
                            // dataList!!.removeAt(position)
                            notifyItemRemoved(position)
                        } else {
                            updateQtyListenenr.serverUpdateQty(position, wishList1)
                        }
                    } else {
                        Utility.toastMessage(context, "Stock Limit finish.")

                    }
                }else{
                    if ((wishList.quantity!!.toInt() + 1) <= wishList.stock!!.toInt()) {
                        wishList.quantity = (wishList.quantity!! + 1)
                        notifyDataSetChanged()
                        if (wishList.quantity!! == 0) {
                            deleteItemListener.serverDeleteItem(position, wishList)
                            // dataList!!.removeAt(position)
                            notifyItemRemoved(position)
                        } else {
                            updateQtyListenenr.serverUpdateQty(position, wishList1)
                        }
                    } else {
                        Utility.toastMessage(context, "Stock Limit finish.")

                    }
                }

            }

            adapterUserAddtocartBinding.ivMinus.setOnClickListener {
                Log.e("Quantity",wishList.quantity.toString()+","+minQty)
                if (wishList.quantity!! > minQty) {
                    wishList.quantity = (wishList.quantity!!.toInt() - 1)
                    notifyDataSetChanged()
                    updateQtyListenenr.serverUpdateQty(position, wishList1)

                } else if (wishList.quantity == minQty) {
                    deleteItemListener.serverDeleteItem(position, wishList)
                }
                lastCheckposition = position
                /*  if(wishList.quantity==0){
                      deleteItemListener.serverDeleteItem(position,wishList)
                      // wishList!!.removeAt(position)
                  }else{
                      notifyDataSetChanged()
                      updateQtyListenenr.serverUpdateQty(position,wishList1)
                  }*/
            }
            adapterUserAddtocartBinding.llMovetoCart.setOnClickListener {
                deleteItemListener.moveToWishList(position,wishList)
            }
            adapterUserAddtocartBinding.ivDeleteCartItem.setOnClickListener {
                deleteItemListener.serverDeleteItem(position, wishList)
            }


            adapterUserAddtocartBinding.executePendingBindings()
            var image = wishList.metaData!!.images
            if (image!!.size > 0) {
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL + imagePath).placeholder(R.drawable.loading)
                    .override(100, 200)
                    .into(adapterUserAddtocartBinding.ivAddtocart)

            }

            adapterUserAddtocartBinding.ivAddtocart.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", wishList!!.itemId)
                i.putExtra("product_id", wishList.productId)
                context.startActivity(i)
            }
        }
    }

    override fun getItemCount(): Int {
        return wishList!!.size;
    }

    interface ServerUpdateQty {
        fun serverUpdateQty(position: Int, dataList: List<ItemsItem?>)
    }

    interface ServerDeleteCart {
        fun serverDeleteItem(position: Int, dataList: ItemsItem?)
        fun moveToWishList(position: Int, dataList: ItemsItem?)
    }
}