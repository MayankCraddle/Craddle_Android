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
import com.cradle.databinding.AdapterUserProductListBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.ProductListItem1
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref

class AdapterUserProductList(
    private val context: Context,
    private var contentJsonArray: List<ProductListItem1?>?, private val itemClickListner: ItemClickListner, private val textState: TextState,private val addProductTOWishList:TextState
) :
    RecyclerView.Adapter<AdapterUserProductList.MyViewHolder>() {

    var lastCheckposition=-1
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserProductListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_product_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = contentJsonArray!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterUserProductListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(countries: ProductListItem1?, position: Int){
            mBinding.tvTitle.text=countries!!.name
            mBinding.tvAmount.text= countries.currency+" "+countries.actualPrice.toString()
            val file= countries.metaData!!.images

            if(file!!.isNotEmpty()){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    .into(mBinding.ivWishlistimage)

            }
            mBinding.ivWishListSelect.setOnClickListener {
                var jsonObject= JsonObject()
                jsonObject.addProperty("itemId",countries.itemId)
                addProductTOWishList.addProductToWishList(jsonObject)

                }
            mBinding.ivWishlistimage.setOnClickListener {
                val i = Intent(context, UserProductDetailsActivity::class.java)
                i.putExtra("item_id", countries.itemId)
                i.putExtra("product_id", countries.productId)

               context.startActivity(i)
                }

            mBinding.rlOpenAddtocart.setOnClickListener {
                lastCheckposition=position
                if (SharaGoPref.getInstance(context).getLoginToken("")!!.isNotEmpty())
                    textState.textStateAction(countries,1)
                else
                   context.startActivity(Intent(context, UserLoginActivity::class.java))

            }



        }
    }
    override fun getItemCount(): Int {
        return contentJsonArray!!.size
    }

    interface TextState{
          fun textStateAction(data: ProductListItem1?, position: Int)
          fun addProductToWishList(jsonObject: JsonObject)
    }

    fun filterList(filterdNames: ArrayList<ProductListItem1>): ArrayList<ProductListItem1> {
        this.contentJsonArray = filterdNames
        notifyDataSetChanged()
        return contentJsonArray as ArrayList<ProductListItem1>
    }

}