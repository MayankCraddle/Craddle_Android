package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterProductbyCategoryBinding


class ProductByCategoryAdapter (private val context: Context/*, private val wishList: List<UserWishList?>?*/) :
    RecyclerView.Adapter<ProductByCategoryAdapter.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterProductbyCategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_productby_category, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  val list = wishList!![position]
        holder.bind(/*list*/)
    }
    inner class MyViewHolder(private val majorExoSegBinding: AdapterProductbyCategoryBinding) :
        RecyclerView.ViewHolder(majorExoSegBinding.root) {
        fun bind(/*wishList: UserWishList?*/){
           /* majorExoSegBinding.productbyCategoryList=wishList
            majorExoSegBinding.executePendingBindings()
            var image = wishList!!.metaData!!.images*/
          /*  if(image!!.size>0){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .override(100, 200)
                    .into(majorExoSegBinding.imgProductImage)

            }*/

            /*   movieListBinding.setOnClickListener {
                     context.startActivity(Intent(context,ViewDetailsActivity::class.java)
                        .putExtra("movieList",countries))
              }*/
        }
    }
    override fun getItemCount(): Int {
        return /*wishList!!.size*/12;
    }
}