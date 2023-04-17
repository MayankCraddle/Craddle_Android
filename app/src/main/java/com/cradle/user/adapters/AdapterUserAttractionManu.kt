package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserAttractionMenuBinding

class AdapterUserAttractionManu (private val context: Context/*, private val wishList: List<UserWishList?>?*/) :
    RecyclerView.Adapter<AdapterUserAttractionManu.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserAttractionMenuBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_attraction_menu, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     //   val list = wishList!![position]
        holder.bind(/*list*/)
    }
    inner class MyViewHolder(private val adapterUserAttractionMenuBinding: AdapterUserAttractionMenuBinding) :
        RecyclerView.ViewHolder(adapterUserAttractionMenuBinding.root) {
        fun bind(/*wishList: UserWishList?*/){
          /*  adapterUserAttractionMenuBinding.userAttractionMenuList=wishList
            adapterUserAttractionMenuBinding.executePendingBindings()
            var image = wishList!!.metaData!!.images
            if(image!!.size>0){
                var imagePath = image[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath)
                    .override(100, 200)
                    .into(adapterUserAttractionMenuBinding.imgProductImage)

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