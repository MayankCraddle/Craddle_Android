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
import com.cradle.databinding.AdapterNewProductOfVendorBinding
import com.cradle.model.NewProductListOfVendorListItem
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.showToast


class AdapterNewProductOfVendor (
    private val context: Context,
    private val productList: ArrayList<NewProductListOfVendorListItem?>?,private  val addtocart:TextState
) :
    RecyclerView.Adapter<AdapterNewProductOfVendor.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterNewProductOfVendorBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_new_product_of_vendor, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = productList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterNewProductOfVendorBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(list: NewProductListOfVendorListItem?, position: Int){
            mBinding.tvTitle.text=list!!.categoryName
            mBinding.tvDis.text= list.metaData!!.shortDescription
            mBinding.tvPrice.text= list.currency+" "+list.discountedPrice.toString()
            mBinding.tvPercentOff.text= list.discountValue.toString()+"% OFF"
            val file= list.metaData.images

            if(file!!.isNotEmpty()){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    .into(mBinding.ivWishlistimage)

            }
            mBinding.ivWishlistimage.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", list.itemId)
                i.putExtra("product_id", list.productId)
                context.startActivity(i)
            }
            mBinding.rlOpenAddtocart.setOnClickListener {
                if(list.orderable.equals("false")){
                    context.showToast(context.getString(R.string.you_do_not))

                }else{
                    addtocart.addProductToCart(list,1,list.itemId)
                }


            }
        }
    }
    override fun getItemCount(): Int {
        return productList!!.size
    }
    interface TextState{

        fun addProductToCart(data: NewProductListOfVendorListItem?, position: Int, itemId: String?)
    }
}