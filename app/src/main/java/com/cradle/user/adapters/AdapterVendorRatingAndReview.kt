package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterVendorRatingReviewBinding
import com.cradle.model.RatingListItem
import com.cradle.utils.localTimeFormet
import com.cradle.utils.parseDateToddMMyyyy



class AdapterVendorRatingAndReview(
    private val context: Context,
    private val ratingListItem: ArrayList<RatingListItem?>?,
) :
    RecyclerView.Adapter<AdapterVendorRatingAndReview.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterVendorRatingReviewBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_vendor_rating_review, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = ratingListItem!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterVendorRatingReviewBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(rating: RatingListItem?, position: Int){
            mBinding.tvTitleRating.text=rating!!.title
            mBinding.tvReview.text= rating.review
            mBinding.rBarProductDetails.rating=rating.rating!!.dec()

            val string= parseDateToddMMyyyy(rating.date)

            mBinding.tvTimedate.text= localTimeFormet(string!!)
        }
    }
    override fun getItemCount(): Int {
        return ratingListItem!!.size
    }
}