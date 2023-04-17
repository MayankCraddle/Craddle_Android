package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserMajorExoSegBinding

class AdapterUserMajorExpoSeg (private val context: Context/*, private val wishList: List<UserWishList?>?*/) :
    RecyclerView.Adapter<AdapterUserMajorExpoSeg.MyViewHolder>() {


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserMajorExoSegBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_major_exo_seg, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     //   val list = wishList!![position]
        holder.bind(/*list*/)
    }
    inner class MyViewHolder(private val majorExoSegBinding: AdapterUserMajorExoSegBinding) :
        RecyclerView.ViewHolder(majorExoSegBinding.root) {
        fun bind(/*wishList: UserWishList?*/){
          /*  majorExoSegBinding.majorExpoSegmentList=wishList
            majorExoSegBinding.executePendingBindings()
            var image = wishList!!.metaData!!.images
            if(image!!.size>0){
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